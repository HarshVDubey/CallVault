package dev.callvault.app.feature.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.callvault.app.data.local.database.CallVaultDatabase
import dev.callvault.app.data.repository.AudioImportManager
import dev.callvault.app.data.repository.RecordingRepositoryImpl
import dev.callvault.app.domain.model.Recording
import dev.callvault.app.domain.repository.RecordingRepository
import dev.callvault.app.domain.usecase.ImportRecordingUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class HomeUiState(
    val recordings: List<Recording> = emptyList(),
    val isImporting: Boolean = false,
    val importResultMessage: String? = null
)

class HomeViewModel(
    private val repository: RecordingRepository,
    private val importRecordingUseCase: ImportRecordingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeRecordings().collect { recordings ->
                _uiState.update { it.copy(recordings = recordings) }
            }
        }
    }

    fun importAudio(sourceUri: String) {
        if (_uiState.value.isImporting) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isImporting = true, importResultMessage = null)
            }

            val resultMessage = try {
                val importedRecording = withContext(Dispatchers.IO) {
                    importRecordingUseCase(sourceUri)
                }
                "\"" + importedRecording.title + "\" was added to your library."
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (exception: Exception) {
                exception.message ?: "Unable to import this audio file."
            }

            _uiState.update {
                it.copy(isImporting = false, importResultMessage = resultMessage)
            }
        }
    }

    fun dismissImportResult() {
        _uiState.update { it.copy(importResultMessage = null) }
    }
}

class HomeViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {
    private val appContext = context.applicationContext

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        val database = CallVaultDatabase.getDatabase(appContext)
        val repository = RecordingRepositoryImpl(database.recordingDao())
        val importManager = AudioImportManager(appContext)
        val importRecordingUseCase = ImportRecordingUseCase(
            repository = repository,
            importAudio = { sourceUri -> importManager.importAudio(Uri.parse(sourceUri)) },
            deleteImportedFile = { relativePath -> importManager.deleteImportedFile(relativePath) }
        )
        return HomeViewModel(repository, importRecordingUseCase) as T
    }
}

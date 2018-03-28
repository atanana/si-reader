package atanana.com.sireader.viewmodels

import android.Manifest
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.R
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.files.ParseFileUseCase
import com.tbruyelle.rxpermissions2.RxPermissions

class PacksViewModel constructor(
        private val openFileHandler: OpenFileHandler,
        private val parseFileUseCase: ParseFileUseCase,
        private val rxPermissions: RxPermissions
) : ViewModel() {

    private val bus = SingleLiveEvent<Action>()

    val liveBus: LiveData<Action>
        get() = bus

    fun fabClicked() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        val intent = openFileHandler.openFileIntent()
                        bus.value = if (intent != null) {
                            ActivityForResultMessage(intent, OPEN_FILE_REQUEST_CODE)
                        } else {
                            ResourceTextMessage(R.string.no_file_managers_installed)
                        }
                    } else {
                        bus.value = ResourceTextMessage(R.string.no_permissions_to_read_files)
                    }
                }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPEN_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val uri = openFileHandler.getUri(data)
            parseFileUseCase.process(uri)
                    .subscribe({}, { error ->
                        bus.value = when (error) {
                            is ParseFileException -> ResourceTextMessage(R.string.cannot_parse_file)
                            is CannotSaveInDatabaseException -> ResourceTextMessage(R.string.cannot_save_questions)
                            else -> ResourceTextMessage(R.string.unknown_error)
                        }
                    })
        }
    }
}
package atanana.com.sireader.viewmodels

import android.app.Activity.RESULT_OK
import android.content.Intent
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.R
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.usecases.ParseFileUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FilesListActivityViewModel @Inject constructor(
        private val openFileHandler: OpenFileHandler,
        private val parseFileUseCase: ParseFileUseCase
) : BaseViewModel() {

    fun fabClicked() {
        val intent = openFileHandler.openFileIntent()
        bus.value = if (intent != null) {
            ActivityForResultMessage(intent, OPEN_FILE_REQUEST_CODE)
        } else {
            ResourceTextMessage(R.string.no_file_managers_installed)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPEN_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val uri = openFileHandler.getUri(data)
            addDisposable(
                    parseFileUseCase.process(uri)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({}, { error ->
                                bus.value = when (error) {
                                    is ParseFileException -> ResourceTextMessage(R.string.cannot_parse_file)
                                    is CannotSaveInDatabaseException -> ResourceTextMessage(R.string.cannot_save_questions)
                                    else -> ResourceTextMessage(R.string.unknown_error)
                                }
                            })
            )
        }
    }
}
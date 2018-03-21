package atanana.com.sireader.viewmodels

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

class PacksViewModel constructor(
        private val openFileHandler: OpenFileHandler,
        private val parseFileUseCase: ParseFileUseCase
) : ViewModel() {

    private val bus = SingleLiveEvent<Action>()

    val liveBus: LiveData<Action>
        get() = bus

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
            val file = openFileHandler.getFile(data)
            try {
                parseFileUseCase.process(file)
            } catch (e: ParseFileException) {
                bus.value = ResourceTextMessage(R.string.cannot_parse_file)
            } catch (e: CannotSaveInDatabaseException) {
                bus.value = ResourceTextMessage(R.string.cannot_save_questions)
            }
        }
    }
}
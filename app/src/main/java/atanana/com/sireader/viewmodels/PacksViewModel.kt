package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import atanana.com.sireader.R
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler

class PacksViewModel constructor(private val openFileHandler: OpenFileHandler) : ViewModel() {
    private val bus = MutableLiveData<Action>()

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
}
package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import atanana.com.sireader.R
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler

class PacksViewModel constructor(private val openFileHandler: OpenFileHandler) : ViewModel() {
    private val toastLiveData = MutableLiveData<TextMessage>()

    val toastData: LiveData<TextMessage>
        get() = toastLiveData

    private val activityForResultLiveData = MutableLiveData<ActivityForResultMessage>()

    val activityForResultData: LiveData<ActivityForResultMessage>
        get() = activityForResultLiveData

    fun fabClicked() {
        val intent = openFileHandler.openFileIntent()
        if (intent != null) {
            activityForResultLiveData.value = ActivityForResultMessage(intent, OPEN_FILE_REQUEST_CODE)
        } else {
            toastLiveData.value = ResourceTextMessage(R.string.no_file_managers_installed)
        }
    }
}
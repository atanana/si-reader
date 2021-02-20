package atanana.com.sireader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    protected val bus = SingleLiveEvent<Action>()

    val liveBus: LiveData<Action>
        get() = bus
}
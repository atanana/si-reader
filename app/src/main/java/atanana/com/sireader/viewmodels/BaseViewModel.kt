package atanana.com.sireader.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _bus = Channel<Action>(Channel.BUFFERED)
    val bus: Flow<Action> = _bus.receiveAsFlow()

    protected fun sendAction(action: Action) {
        viewModelScope.launch {
            _bus.send(action)
        }
    }
}
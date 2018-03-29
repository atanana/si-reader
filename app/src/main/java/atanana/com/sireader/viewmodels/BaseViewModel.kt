package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    protected val bus = SingleLiveEvent<Action>()

    val liveBus: LiveData<Action>
        get() = bus

    protected fun addDisposable(disposable: Disposable) = this.disposable.add(disposable)

    override fun onCleared() {
        disposable.clear()
    }
}
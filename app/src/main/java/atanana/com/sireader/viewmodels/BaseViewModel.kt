package atanana.com.sireader.viewmodels

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) = this.disposable.add(disposable)

    override fun onCleared() {
        disposable.clear()
    }
}
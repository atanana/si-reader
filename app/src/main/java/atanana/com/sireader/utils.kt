package atanana.com.sireader

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

fun doIo(action: () -> Unit) {
    Completable.fromAction(action)
            .subscribeOn(Schedulers.io())
            .subscribe()
}
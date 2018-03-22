package atanana.com.sireader.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.files.ParseFileUseCase
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject

class PacksViewModelFactory @Inject constructor(
        private val openFileHandler: OpenFileHandler,
        private val parseFileUseCase: ParseFileUseCase,
        private val rxPermissions: RxPermissions
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacksViewModel::class.java)) {
            return PacksViewModel(openFileHandler, parseFileUseCase, rxPermissions) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
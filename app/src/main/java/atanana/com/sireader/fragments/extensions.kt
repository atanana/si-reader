package atanana.com.sireader.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> Fragment.createViewModel(noinline provider: (() -> VM)? = null): VM {
    val factory = if (provider != null) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
        }
    } else {
        defaultViewModelProviderFactory
    }

    return ViewModelProvider(viewModelStore, factory).get(VM::class.java)
}
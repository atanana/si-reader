package atanana.com.sireader.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import atanana.com.sireader.R

fun FragmentManager.openFragment(fragment: BaseFragment<*>, addToBackStack: Boolean = true) {
    val transaction = beginTransaction()
        .setCustomAnimations(
            if (addToBackStack) R.anim.enter_from_right else 0,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        .replace(R.id.fragment, fragment, fragment.transactionTag)
    if (addToBackStack) {
        transaction.addToBackStack(fragment.transactionTag)
    }
    transaction.commit()
}

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
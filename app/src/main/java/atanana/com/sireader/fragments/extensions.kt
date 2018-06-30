package atanana.com.sireader.fragments

import android.support.v4.app.FragmentManager
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

fun FragmentManager.hasFragment(): Boolean = findFragmentById(R.id.fragment) != null
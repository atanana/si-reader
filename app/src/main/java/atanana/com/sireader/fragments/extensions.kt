package atanana.com.sireader.fragments

import android.support.v4.app.FragmentManager
import atanana.com.sireader.R

fun FragmentManager.openFragment(fragment: BaseFragment) {
    beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.fragment, fragment, fragment.transactionTag)
            .addToBackStack(fragment.transactionTag)
            .commit()
}
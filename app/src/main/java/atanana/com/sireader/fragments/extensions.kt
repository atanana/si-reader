package atanana.com.sireader.fragments

import android.support.v4.app.FragmentManager
import atanana.com.sireader.R

fun FragmentManager.openFragment(fragment: BaseFragment) {
    beginTransaction()
            .replace(R.id.fragment, fragment, fragment.transactionTag)
            .addToBackStack(fragment.transactionTag)
            .commit()
}
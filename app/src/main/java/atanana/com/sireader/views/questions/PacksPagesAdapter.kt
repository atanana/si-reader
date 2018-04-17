package atanana.com.sireader.views.questions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.fragments.PackFragment

class PacksPagesAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    var packs = emptyList<PackEntity>()

    override fun getItem(position: Int): Fragment {
        return PackFragment.newInstance(packs[position].id)
    }

    override fun getCount(): Int = packs.size
}
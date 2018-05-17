package atanana.com.sireader.screens.packspager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.screens.pack.PackFragment

class PacksPagesAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    var packs = emptyList<PackEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment {
        return PackFragment.newInstance(packs[position].id)
    }

    override fun getCount(): Int = packs.size
}
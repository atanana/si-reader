package atanana.com.sireader.screens.packspager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.screens.pack.PackFragment

class PacksPagesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var packs = emptyList<PackEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = packs.size

    override fun createFragment(position: Int): Fragment {
        val packId = packs[position].id
        return PackFragment.newInstance(packId)
    }
}
package atanana.com.sireader.screens.fileinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.databinding.ItemPackBinding

class FileInfoAdapter(
    private val selectPack: (Int) -> Unit
) : RecyclerView.Adapter<PackViewHolder>() {

    var packs = emptyList<PackItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPackBinding.inflate(inflater, parent, false)
        return PackViewHolder(binding, selectPack)
    }

    override fun getItemCount(): Int = packs.size

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        holder.bind(packs[position])
    }
}
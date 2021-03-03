package atanana.com.sireader.screens.fileinfo

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.R
import atanana.com.sireader.databinding.ItemPackBinding
import atanana.com.sireader.views.invisible

class PackViewHolder(
    private val binding: ItemPackBinding,
    private val selectPack: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        val accentColor = ContextCompat.getColor(itemView.context, R.color.accent)
        DrawableCompat.setTint(binding.star.drawable, accentColor)
    }

    fun bind(item: PackItem) {
        binding.packTitle.text = item.pack.indexedTitle
        itemView.setOnClickListener { selectPack(item.pack.id) }
        binding.star.invisible(!item.lastRead)
    }
}
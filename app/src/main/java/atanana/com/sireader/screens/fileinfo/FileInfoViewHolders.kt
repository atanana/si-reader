package atanana.com.sireader.screens.fileinfo

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.databinding.ItemFileInfoBinding
import atanana.com.sireader.databinding.ItemPackBinding
import atanana.com.sireader.views.invisible
import atanana.com.sireader.views.optionalText

sealed class FileViewHolder(item: View) : RecyclerView.ViewHolder(item)

class PackViewHolder(private val binding: ItemPackBinding, private val selectPack: (Int) -> Unit) : FileViewHolder(binding.root) {

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

class FileInfoViewHolder(private val binding: ItemFileInfoBinding) : FileViewHolder(binding.root) {

    fun bind(fileEntity: QuestionFileEntity) {
        binding.fileTitle.text = fileEntity.title
        binding.fileName.text = fileEntity.filename
        binding.fileNotes.optionalText(fileEntity.notes)
        binding.fileEditors.optionalText(fileEntity.editor)
    }
}
package atanana.com.sireader.screens.fileslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.R
import atanana.com.sireader.databinding.ItemFileBinding
import atanana.com.sireader.views.invisible

class FilesListAdapter(private val clickListener: FileClickListener) : RecyclerView.Adapter<FilesListAdapter.ViewHolder>() {

    var files = emptyList<FileItem>()
        set(value) {
            val diff = DiffUtil.calculateDiff(DiffCallback(files, value), false)
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFileBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(files[position])
    }

    inner class ViewHolder(private val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            val accentColor = ContextCompat.getColor(itemView.context, R.color.accent)
            DrawableCompat.setTint(binding.star.drawable, accentColor)
        }

        fun bind(item: FileItem) {
            val file = item.entity
            binding.fileTitle.text = file.title
            binding.fileName.text = file.filename
            itemView.isSelected = item.isSelected
            binding.star.invisible(!item.lastRead)
            itemView.setOnClickListener { clickListener.onClick(file.id) }
            itemView.setOnLongClickListener {
                clickListener.onLongClick(file.id)
                true
            }
        }
    }

    interface FileClickListener {
        fun onClick(fileId: Int)
        fun onLongClick(fileId: Int)
    }
}

private class DiffCallback(
    private val oldFiles: List<FileItem>,
    private val newFiles: List<FileItem>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFiles[oldItemPosition].entity.id == newFiles[newItemPosition].entity.id
    }

    override fun getOldListSize(): Int = oldFiles.size

    override fun getNewListSize(): Int = newFiles.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFiles[oldItemPosition] == newFiles[newItemPosition]
    }
}
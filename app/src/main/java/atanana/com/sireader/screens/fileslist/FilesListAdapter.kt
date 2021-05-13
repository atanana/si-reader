package atanana.com.sireader.screens.fileslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.R
import atanana.com.sireader.databinding.ItemFileBinding
import atanana.com.sireader.views.invisible

class FilesListAdapter(private val clickListener: FileClickListener) : ListAdapter<FileItem, FilesListAdapter.ViewHolder>(Callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFileBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
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

private object Callback : DiffUtil.ItemCallback<FileItem>() {

    override fun areItemsTheSame(oldItem: FileItem, newItem: FileItem): Boolean =
        oldItem.entity.id == newItem.entity.id

    override fun areContentsTheSame(oldItem: FileItem, newItem: FileItem): Boolean =
        oldItem == newItem
}
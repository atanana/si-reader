package atanana.com.sireader.screens.fileslist

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R

class FilesListAdapter(
        private val clickListener: FileClickListener
) : RecyclerView.Adapter<FilesListAdapter.ViewHolder>() {

    var files = emptyList<FileItem>()
        set(value) {
            val diff = DiffUtil.calculateDiff(DiffCallback(files, value), false)
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(files[position])
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val fileTitle: TextView = item.findViewById(R.id.file_title)
        private val fileName: TextView = item.findViewById(R.id.file_name)

        fun bind(item: FileItem) {
            val file = item.entity
            fileTitle.text = file.title
            fileName.text = file.filename
            itemView.isSelected = item.isSelected
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
package atanana.com.sireader.views.files

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.SiReaderException
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.views.files.FileInfoAdapter.ViewHolder.FileInfoViewHolder
import atanana.com.sireader.views.files.FileInfoAdapter.ViewHolder.PackViewHolder
import atanana.com.sireader.views.gone

class FileInfoAdapter(
        private val selectPack: (Int) -> Unit
) : RecyclerView.Adapter<FileInfoAdapter.ViewHolder>() {
    companion object {
        const val TYPE_FILE_INFO = 0
        const val TYPE_PACK = 1
    }

    var packs = emptyList<PackEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var info: QuestionFileEntity? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_FILE_INFO -> FileInfoViewHolder(inflateView(parent, R.layout.item_file_info))
            TYPE_PACK -> PackViewHolder(inflateView(parent, R.layout.item_pack), selectPack)
            else -> throw SiReaderException("Unknown view type!")
        }
    }

    private fun inflateView(parent: ViewGroup, resource: Int): View {
        return LayoutInflater.from(parent.context)
                .inflate(resource, parent, false)
    }

    override fun getItemCount(): Int = packs.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is PackViewHolder -> holder.bind(packs[position - 1])
            is FileInfoViewHolder -> info?.let { holder.bind(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_FILE_INFO else TYPE_PACK
    }

    sealed class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        class PackViewHolder(item: View, private val selectPack: (Int) -> Unit) : ViewHolder(item) {
            private val packTitle: TextView = item.findViewById(R.id.pack_title)

            fun bind(pack: PackEntity) {
                packTitle.text = pack.topic
                itemView.setOnClickListener { selectPack(pack.id) }
            }
        }

        class FileInfoViewHolder(item: View) : ViewHolder(item) {
            private val fileTitle: TextView = item.findViewById(R.id.file_title)
            private val fileName: TextView = item.findViewById(R.id.file_name)
            private val fileNotes: TextView = item.findViewById(R.id.file_notes)
            private val fileEditors: TextView = item.findViewById(R.id.file_editors)

            fun bind(fileEntity: QuestionFileEntity) {
                fileTitle.text = fileEntity.title
                fileName.text = fileEntity.filename
                fileNotes.text = fileEntity.notes
                fileNotes.gone(fileEntity.notes.isNullOrBlank())
                fileEditors.text = fileEntity.editor
                fileEditors.gone(fileEntity.editor.isNullOrBlank())
            }
        }
    }
}
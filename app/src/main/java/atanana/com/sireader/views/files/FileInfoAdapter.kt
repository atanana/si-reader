package atanana.com.sireader.views.files

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.SiReaderException
import atanana.com.sireader.database.PackEntity

class FileInfoAdapter : RecyclerView.Adapter<FileInfoAdapter.ViewHolder>() {
    companion object {
        const val TYPE_FILE_INFO = 0
        const val TYPE_PACK = 1
    }

    var packs = emptyList<PackEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_FILE_INFO -> FileInfoViewHolder(inflateView(parent, R.layout.item_file_info))
            TYPE_PACK -> PackViewHolder(inflateView(parent, R.layout.item_pack))
            else -> throw SiReaderException("Unknown view type!")
        }
    }

    private fun inflateView(parent: ViewGroup, resource: Int): View {
        return LayoutInflater.from(parent.context)
                .inflate(resource, parent, false)
    }

    override fun getItemCount(): Int = packs.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_FILE_INFO -> {
            }
            TYPE_PACK -> {
                val pack = packs[position - 1]
                (holder as PackViewHolder).bind(pack)
            }
            else -> throw SiReaderException("Unknown view type!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_FILE_INFO else TYPE_PACK
    }

    open class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    class PackViewHolder(item: View) : ViewHolder(item) {
        private val packTitle: TextView = item.findViewById(R.id.pack_title)
        private val packAuthor: TextView = item.findViewById(R.id.pack_author)
        private val packNotes: TextView = item.findViewById(R.id.pack_notes)

        fun bind(pack: PackEntity) {
            packTitle.text = pack.topic
            packAuthor.text = pack.author
            packNotes.text = pack.notes
        }
    }

    class FileInfoViewHolder(item: View) : ViewHolder(item)
}
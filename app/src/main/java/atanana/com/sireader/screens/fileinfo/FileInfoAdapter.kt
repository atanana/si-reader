package atanana.com.sireader.screens.fileinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import atanana.com.sireader.SiReaderException
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.databinding.ItemFileInfoBinding
import atanana.com.sireader.databinding.ItemPackBinding

class FileInfoAdapter(
    private val selectPack: (Int) -> Unit
) : RecyclerView.Adapter<FileViewHolder>() {

    companion object {
        const val TYPE_FILE_INFO = 0
        const val TYPE_PACK = 1
    }

    var packs = emptyList<PackItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var info: QuestionFileEntity? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FILE_INFO -> {
                val binding = ItemFileInfoBinding.inflate(inflater, parent, false)
                FileInfoViewHolder(binding)
            }
            TYPE_PACK -> {
                val binding = ItemPackBinding.inflate(inflater, parent, false)
                PackViewHolder(binding, selectPack)
            }
            else -> throw SiReaderException("Unknown view type!")
        }
    }

    override fun getItemCount(): Int = packs.size + 1

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        when (holder) {
            is PackViewHolder -> holder.bind(packs[position - 1])
            is FileInfoViewHolder -> info?.let { holder.bind(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_FILE_INFO else TYPE_PACK
    }
}
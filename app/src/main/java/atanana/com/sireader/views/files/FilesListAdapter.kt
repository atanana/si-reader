package atanana.com.sireader.views.files

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFileEntity

class FilesListAdapter(
        private val selectFile: (Int) -> Unit
) : RecyclerView.Adapter<FilesListAdapter.ViewHolder>() {

    var files = emptyList<QuestionFileEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        holder.fileTitle.text = file.title
        holder.fileName.text = file.filename
        holder.item.setOnClickListener { selectFile(file.id) }
    }

    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val fileTitle: TextView = item.findViewById(R.id.file_title)
        val fileName: TextView = item.findViewById(R.id.file_name)
    }
}
package atanana.com.sireader.views.packs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFileEntity

class PacksListAdapter : RecyclerView.Adapter<PacksListAdapter.ViewHolder>() {
    var packs = emptyList<QuestionFileEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pack, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = packs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pack = packs[position]
        holder.packName.text = pack.title
        holder.packFile.text = pack.filename
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val packName: TextView = item.findViewById(R.id.pack_name)
        val packFile: TextView = item.findViewById(R.id.pack_file)
    }
}
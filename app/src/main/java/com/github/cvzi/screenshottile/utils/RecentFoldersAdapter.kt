package com.github.cvzi.screenshottile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import com.github.cvzi.screenshottile.R

/**
 * A single recent folder item
 */
class RecentFolder(private val value: String, val dataIndex: Int) {
    val uri: Uri
        get() = Uri.parse(value)

    override fun toString(): String {
        return "RecentFolder('$value' #$dataIndex)"
    }
}


/**
 * RecentFoldersAdapter
 */
class RecentFoldersAdapter(
    private var context: Context,
    private var data: ArrayList<RecentFolder>
) :
    RecyclerView.Adapter<RecentFoldersAdapter.ViewHolder>() {

    var onTextClickListener: OnItemClickListener? = null
    var onDeleteClickListener: OnItemClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: Array<RecentFolder>) {
        this.data.clear()
        this.data.addAll(newData.filter {
            val docDir = DocumentFile.fromTreeUri(context, it.uri)
            docDir?.canWrite() ?: false
        })
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.textView)
            val deleteButton: ImageButton = view.findViewById(R.id.imageButtonDelete)
            val imageView: View = view.findViewById(R.id.imageView)
            textView.setOnClickListener {
                if (adapterPosition == layoutPosition && adapterPosition != -1) {
                    onTextClickListener?.invoke(it, adapterPosition)
                }
            }
            imageView.setOnClickListener {
                if (adapterPosition == layoutPosition && adapterPosition != -1) {
                    onTextClickListener?.invoke(it, adapterPosition)
                }
            }
            deleteButton.setOnClickListener {
                if (adapterPosition == layoutPosition && adapterPosition != -1) {
                    onDeleteClickListener?.invoke(it, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recent_folder_item, viewGroup, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = niceFullPathFromUri(data[position].uri)
    }

    override fun getItemCount() = data.size
}
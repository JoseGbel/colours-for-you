package com.remcode.coloursforyou.presentation

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remcode.coloursforyou.R
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.utils.capitalize

class ColourListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ColourListAdapter.ColourViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var colours = emptyList<Colour>() // Cached copy of words

    inner class ColourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colourName : TextView = itemView.findViewById(R.id.textView_recyclerview)
        val colourImage : ImageView = itemView.findViewById(R.id.image_view_recyclerview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColourViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return ColourViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ColourViewHolder, position: Int) {
        val current = colours[position]
        holder.colourName.text = current.name.capitalize()
        holder.colourImage.setColorFilter(Color.parseColor(current.hexColour))
    }

    internal fun setColours(colours: List<Colour>) {
        this.colours = colours
        notifyDataSetChanged()
    }

    override fun getItemCount() = colours.size
}
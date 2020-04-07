package com.example.catfact.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.catfact.R
import com.example.catfact.model.CatFact

class CatsIdsAdapter : RecyclerView.Adapter<CatsIdsAdapter.CatsIdsViewHolder>() {

    private var catFacts: List<CatFact> = ArrayList()

    private var itemListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsIdsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cat_id_item, parent, false)

        return CatsIdsViewHolder(itemView)
    }

    override fun getItemCount(): Int = catFacts.size

    override fun onBindViewHolder(holder: CatsIdsViewHolder, position: Int) {
        val catFact = catFacts[position]

        holder.setIcon(CAT_ICON_URL)
        holder.setId(catFact.id)
    }

    fun loadCatFacts(catFacts: List<CatFact>) {
        this.catFacts = catFacts
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemListener = listener
    }

    inner class CatsIdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var catIdTextView = itemView.findViewById<TextView>(R.id.cat_id_text)
        private var catIconImageView = itemView.findViewById<ImageView>(R.id.cat_icon)

        init {
            setOnClickListener()
        }

        private fun setOnClickListener() {
            itemView.setOnClickListener {
                val position = adapterPosition
                val catFact = catFacts[position]

                itemListener?.onItemClick(catFact)
            }
        }

        fun setIcon(url: String) {
            Glide
                .with(itemView)
                .load(url)
                .centerCrop()
                .into(catIconImageView)
        }

        fun setId(id: String) {
            catIdTextView.text = id
        }
    }

    interface OnItemClickListener {
        fun onItemClick(catFact: CatFact)
    }

    companion object {
        private const val CAT_ICON_URL = "https://user-images.githubusercontent.com/17621860/78683891-2109dc80-78f0-11ea-8e4c-5bf93db6d967.png"
    }
}

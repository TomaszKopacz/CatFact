package com.example.catfact.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        holder.setIcon()
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
        private var catIconImageVuew = itemView.findViewById<ImageView>(R.id.cat_icon)

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

        fun setIcon() {

        }

        fun setId(id: String) {
            catIdTextView.text = id
        }

    }

    interface OnItemClickListener {
        fun onItemClick(catFact: CatFact)
    }
}

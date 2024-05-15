package com.samsung.millioner.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samsung.millioner.R
import com.samsung.millioner.model.ThemeListModel

class ThemeListAdapter(private val onItemClickedListner: OnItemClickedListner) :
    RecyclerView.Adapter<ThemeListAdapter.ThemeListViewHolder>() {

    private var themeListModels: List<ThemeListModel>? = null

    fun setThemeListModels(themeListModels: List<ThemeListModel>?) {
        this.themeListModels = themeListModels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.each_theme, parent, false)
        return ThemeListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return themeListModels?.size ?: 0
    }

    override fun onBindViewHolder(holder: ThemeListViewHolder, themeId: Int) {
        val model: ThemeListModel = themeListModels!![themeId]
        holder.title.text = model.title
        Log.d("Model", "Title: ${model.title}, Image: ${model.image}, themeList: $themeListModels")
        Glide.with(holder.itemView).load(model.image).into(holder.themeImage)
    }



    inner class ThemeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val title: TextView
        val themeImage: ImageView
        private val constraintLayout: ConstraintLayout

        init {
            title = itemView.findViewById<TextView>(R.id.themeTitleList)
            themeImage = itemView.findViewById<ImageView>(R.id.themeImageList)
            constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.constraintLayout)
            constraintLayout.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onItemClickedListner.onItemClick(adapterPosition)
        }
    }

    interface OnItemClickedListner {
        fun onItemClick(themeId: Int)
    }

}
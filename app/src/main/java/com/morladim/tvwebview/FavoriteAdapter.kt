package com.morladim.tvwebview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 *
 * @Author 5k5k
 * @Date 2022/12/25
 */
class FavoriteAdapter(
    private val favoriteList: ArrayList<Favorite>,
    val listener: ItemListener
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.setListener(listener)
        holder.setData(favoriteList[position])
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    fun setHistoryList(mHistoryList: List<Favorite>?) {
        this.favoriteList.clear()
        this.favoriteList.addAll(mHistoryList!!)
    }

    interface ItemListener {

        fun onItemClick(item: Favorite)
    }

    class FavoriteHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val imageView: ImageView = view.findViewById<View>(R.id.image) as ImageView
        private val title: TextView = view.findViewById<View>(R.id.text) as TextView
        private var data: Favorite? = null
        fun setData(item: Favorite) {
            title.text = item.name
            imageView.loadImage(item.image)
            data = item
            view.setOnClickListener(this)
        }

        private var listener: ItemListener? = null
        fun setListener(listener: ItemListener) {
            this.listener = listener
        }

        override fun onClick(p0: View?) {
            data?.let {
                listener?.onItemClick(it)
            }
        }

        init {
            view.setOnClickListener(this)
        }
    }

}
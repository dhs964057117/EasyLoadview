package com.dhs.tools.loadview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhs.tools.loadview.R
import com.dhs.tools.shimmer.ShimmerImageView
import com.dhs.tools.shimmer.ShimmerTextView

class PersonAdapter : RecyclerView.Adapter<SimpleRcvViewHolder?>() {
    private var list: MutableList<String> = ArrayList()
    private var defaultSize: Int = 10
    fun notifyDataChanged(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRcvViewHolder {
        return SimpleRcvViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SimpleRcvViewHolder, position: Int) {
        if (list.isNotEmpty()) {
            (holder.getView<View>(R.id.img_news) as ShimmerImageView).hideLoading()
            (holder.getView<View>(R.id.tv_title) as ShimmerTextView).hideLoading()
            (holder.getView<View>(R.id.tv_content) as ShimmerTextView).hideLoading()
            (holder.getView<View>(R.id.tv_time) as ShimmerTextView).hideLoading()
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            defaultSize
        } else {
            list.size
        }
    }
}

package com.dhs.tools.loadview.adapter

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleRcvViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    private val views = SparseArray<View?>()

    fun <V : View?> getView(resId: Int): V? {
        var v = views[resId]
        if (null == v) {
            v = itemView.findViewById(resId)
            views.put(resId, v)
        }
        return v as V?
    }
}
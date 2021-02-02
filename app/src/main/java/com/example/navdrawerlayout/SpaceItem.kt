package com.example.navdrawerlayout

import android.view.View
import android.view.ViewGroup

class SpaceItem(private val spaceDp: Int) : DrawerItem<SpaceItem.ViewHolder?>() {
    override fun createViewHolder(parent: ViewGroup?): ViewHolder {
        val context = parent!!.context
        val v = View(context)
        val height = (context.resources.displayMetrics.density * spaceDp).toInt()
        v.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        return ViewHolder(v)
    }

    override val isSelectable: Boolean
        get() = false

    inner class ViewHolder(itemView: View) : DrawerAdapter.ViewHolder(itemView)

    override fun bindViewHolder(holder: ViewHolder?) {
    }

}
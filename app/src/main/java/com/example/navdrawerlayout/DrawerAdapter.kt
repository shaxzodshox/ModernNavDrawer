package com.example.navdrawerlayout

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DrawerAdapter(protected val items: List<DrawerItem<DrawerAdapter.ViewHolder>>)
    : RecyclerView.Adapter<DrawerAdapter.ViewHolder>(){

    private var viewTypes: MutableMap<Class<out DrawerItem<*>>, Int?> = HashMap()
    private var holderFactories: SparseArray<DrawerItem<*>> = SparseArray()
    private var listener: OnItemSelectedListener? = null

    init {
        processViewTypes()
    }

    private fun processViewTypes(){
        var type = 0
        for(item in items){
            if(!viewTypes.containsKey(item.javaClass)){
                viewTypes[item.javaClass] = type
                holderFactories.put(type, item)
                type++
            }
        }
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var drawerAdapter: DrawerAdapter? = null
        override fun onClick(v: View?) {
            drawerAdapter!!.setSelected(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = holderFactories[viewType].createViewHolder(parent)
        holder?.drawerAdapter = this
        return holder!!
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].bindViewHolder(holder)
    }

    fun setSelected(position: Int){
        val newChecked = items[position]
        if(!newChecked.isSelectable){
            return
        }
        for(i in items.indices){
            val item = items[i]
            if(item.isChecked){
                item.setItemChecked(false)
                notifyItemChanged(i)
                break
            }
        }
        newChecked.setItemChecked(true)
        notifyItemChanged(position)
        if(listener != null){
            listener!!.onItemSelected(position)
        }
    }

    fun setListener(listener: OnItemSelectedListener){
        this.listener = listener
    }

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }
}
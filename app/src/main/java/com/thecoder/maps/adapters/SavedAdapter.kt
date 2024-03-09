package com.thecoder.maps.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.thecoder.maps.R
import com.thecoder.maps.models.MySavedItem

class SavedAdapter():Adapter<SavedAdapter.MyHolder>() {

    lateinit var list:ArrayList<MySavedItem>
    lateinit var clickListener: ClickListener

    lateinit var inflater: LayoutInflater

    constructor(context: Context,list: ArrayList<MySavedItem>,clickListener: ClickListener):this(){

        inflater=LayoutInflater.from(context)
        this.list=list
        this.clickListener=clickListener
    }

    class  MyHolder(view:View):RecyclerView.ViewHolder(view){
        val tv_address=view.findViewById<TextView>(R.id.tv_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        return MyHolder(inflater.inflate(R.layout.item_saved,parent,false))
    }

    override fun getItemCount(): Int {

        return list.size
    }

    fun updateData(list:ArrayList<MySavedItem>){

        this.list.clear()
        this.list=list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.apply {
            tv_address.text=list[position].address
            itemView.setOnClickListener {
                clickListener.onItemClicked(list[position])
            }
        }
    }

    interface ClickListener{
        fun onItemClicked(item:MySavedItem)
    }
}
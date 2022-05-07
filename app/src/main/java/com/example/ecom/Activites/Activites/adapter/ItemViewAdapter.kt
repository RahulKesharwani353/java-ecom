package com.example.ecom.Activites.Activites.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecom.Activites.Activites.modleclass.HomeAdModel
import com.example.ecom.Activites.Activites.modleclass.ItemViewModel
import com.example.ecom.R

class ItemViewAdapter(
    val context : Context,
    val ItemViewlList: List<ItemViewModel>) :
    RecyclerView.Adapter<ItemViewAdapter.viewHolder>() {


    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        fun setData(ads: HomeAdModel, position: Int){
//
//        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {

        val view : View= LayoutInflater.from(parent.context).inflate(R.layout.item_card_view,parent,false)
        return viewHolder(view)

    }

    override fun getItemCount(): Int {
        return ItemViewlList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val ad= ItemViewlList[position]
//        holder.setData(ad, position)

    }

}
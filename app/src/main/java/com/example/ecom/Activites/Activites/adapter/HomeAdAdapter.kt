package com.example.ecom.Activites.Activites.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecom.Activites.Activites.modleclass.HomeAdModel
import com.example.ecom.R

class HomeAdAdapter(
    val context : Context,
    val HomeAdModelList: List<HomeAdModel>) :
    RecyclerView.Adapter<HomeAdAdapter.viewHolder>() {


    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        fun setData(ads: HomeAdModel, position: Int){
//
//        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {

        val view : View= LayoutInflater.from(parent.context).inflate(R.layout.home_ad_layout,parent,false)
        return viewHolder(view)

    }

    override fun getItemCount(): Int {
        return HomeAdModelList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val ad= HomeAdModelList[position]
//        holder.setData(ad, position)

    }

}
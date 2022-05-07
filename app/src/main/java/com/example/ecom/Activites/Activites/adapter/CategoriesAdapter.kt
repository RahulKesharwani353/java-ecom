package com.example.ecom.Activites.Activites.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecom.Activites.Activites.CategoryShopingActivity
import com.example.ecom.Activites.Activites.modleclass.CategoriesModel
import com.example.ecom.Activites.Activites.modleclass.HomeAdModel
import com.example.ecom.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.categories_item.view.*

class CategoriesAdapter(
    val context : Context,
     val categoriesModelList: List<CategoriesModel>) :
     RecyclerView.Adapter<CategoriesAdapter.viewHolder>() {


    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
          var categoriesIcon: ImageView= itemView.findViewById(R.id.categories_icon)
          var categoriesName: TextView= itemView.findViewById(R.id.categories_name)



          fun setData(categories: CategoriesModel, position: Int){

              itemView.categories_name.text= categories.title
          }

     }

     override fun onCreateViewHolder(
         parent: ViewGroup,
         viewType: Int
     ): viewHolder {

         val view : View= LayoutInflater.from(parent.context).inflate(R.layout.categories_item,parent,false)
         return viewHolder(view)

     }

     override fun getItemCount(): Int {
         return categoriesModelList.size
     }

     override fun onBindViewHolder(holder: viewHolder, position: Int) {

         val categories= categoriesModelList[position]
         holder.setData(categories, position)

         holder.itemView.setOnClickListener {

             it.setOnClickListener {
                     Toast.makeText(context, categories.title, Toast.LENGTH_SHORT).show()
                 val intent = Intent(context, CategoryShopingActivity::class.java)
                 intent.putExtra("selectedCategory", categories.title)
                 context.startActivity(intent)
             }
         }

     }

 }
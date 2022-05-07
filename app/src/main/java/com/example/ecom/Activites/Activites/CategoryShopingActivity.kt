package com.example.ecom.Activites.Activites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecom.Activites.Activites.modleclass.ProductInfo
import com.example.ecom.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_category_shoping.*


class CategoryShopingActivity : AppCompatActivity() {

    lateinit var userRef: DatabaseReference
     var str = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_shoping)

        val selectedCategory = intent.extras!!.getString("selectedCategory")
        shopping_categories_title.text= selectedCategory


        userRef = FirebaseDatabase.getInstance().reference.child("products").child(selectedCategory!!)
        shopping_categories_view.layoutManager = GridLayoutManager(applicationContext,2)

    }

    override fun onStart() {
        super.onStart()
        var options: FirebaseRecyclerOptions<ProductInfo?>? = null
        if (str == "") {
            options =
                FirebaseRecyclerOptions.Builder<ProductInfo>().setQuery(userRef, ProductInfo::class.java)
                    .build()
        } else {
            options = FirebaseRecyclerOptions.Builder<ProductInfo>().setQuery(
                userRef.startAt(str).endAt("\uf8ff".also { str = it }),
                ProductInfo::class.java
            ).build()
        }


        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<ProductInfo, ProductViewHolder > =
            object :
                FirebaseRecyclerAdapter<ProductInfo, ProductViewHolder>(
                    options
                ) {
                 override fun onBindViewHolder(
                    holder: ProductViewHolder,
                    position: Int,
                    moodel: ProductInfo
                ) {
                     shopping_categories_progress_bar.visibility= GONE
                     holder.productName.text = moodel.name
                     holder.productPrice.text = moodel.price
                     Glide.with(this@CategoryShopingActivity).load(moodel.image).into(holder.productImageView)
                    holder.itemView.setOnClickListener {
                        val Product_id = getRef(position).key
                        val intent = Intent(this@CategoryShopingActivity, ItemDescriptionActivity::class.java)
                        intent.putExtra("product_id", Product_id)
                        intent.putExtra("product_image", moodel.image)
                        intent.putExtra("product_name", moodel.name)
                        intent.putExtra("product_price", moodel.price)
                        startActivity(intent)
                    }
                 }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): ProductViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_card_view, parent, false)
                    return ProductViewHolder(view)
                }
            }

        shopping_categories_view.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()

    }

class ProductViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var productName: TextView = itemView.findViewById(R.id.itemViewName)
    var productPrice: TextView = itemView.findViewById(R.id.itemViewPrice)
    var productImageView: ImageView = itemView.findViewById(R.id.itemViewImage)
    var cardView: CardView = itemView.findViewById(R.id.itemCardView)

}
}
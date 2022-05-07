package com.example.ecom.Activites.Activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecom.Activites.Activites.modleclass.ProductInfo
import com.example.ecom.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    lateinit var userRef: DatabaseReference
    var str = ""
    lateinit var currentUser: String
    lateinit var Product_id: String
     var totalPrice: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        currentUser= FirebaseAuth.getInstance().currentUser!!.uid

        userRef = FirebaseDatabase.getInstance().reference.child("Cart").child(currentUser)
        cart_recyclerView.layoutManager = LinearLayoutManager(this)


        cart_place_order.setOnClickListener {
            val intent = Intent(this, BuyNowActivity::class.java)
            intent.putExtra("totalPrice",totalPrice.toString())
            startActivity(intent)
        }

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


        val firebaseRecyclerAdapter: FirebaseRecyclerAdapter<ProductInfo, ProductViewHolder> =
            object :
                FirebaseRecyclerAdapter<ProductInfo, ProductViewHolder>(
                    options
                ) {
                override fun onBindViewHolder(
                    holder: ProductViewHolder,
                    position: Int,
                    moodel: ProductInfo
                ) {
//                    progressDialog.dismiss()
                    progress_bar.visibility = GONE
                    var quan = moodel.quantity
                    holder.productName.text = moodel.name
                    holder.productPrice.text = moodel.price
                    holder.quantView.text= quan.toString()
                    totalPrice += (moodel.price.toInt()*quan)
                    Glide.with(this@CartActivity).load(moodel.image).into(holder.productImageView)

                    cart_total_price.text=totalPrice.toString()


//                    holder.itemView.setOnClickListener {
////                        val intent = Intent(this@CartActivity, ItemDescriptionActivity::class.java)
////                        intent.putExtra("product_id", Product_id)
////                        intent.putExtra("product_image", moodel.image)
////                        intent.putExtra("product_name", moodel.name)
////                        intent.putExtra("product_price", moodel.price)
////                        startActivity(intent)
//
//                        Toast.makeText(this@CartActivity, "ok ok", Toast.LENGTH_SHORT).show()
//                    }


                    /*-----------------------------------------------------------------*/
                    holder.plusBtn.setOnClickListener {

                        if(quan<5)
                        {
                            totalPrice -= (moodel.price.toInt()*quan)
                            Product_id = getRef(position).key!!
                            quan++
                            val progressDialog = ProgressDialog(this@CartActivity)
                            progressDialog.setCanceledOnTouchOutside(false)
                            progressDialog.setMessage("Hold On...")
                            progressDialog.show()
                            userRef.child(Product_id).child("quantity").setValue(quan).addOnCompleteListener {
                                if (it.isSuccessful)
                                    progressDialog.dismiss()
                            }

                        }
                    }

                    holder.minusBtn.setOnClickListener {

                        if(quan>0)
                        {
                                totalPrice -= (moodel.price.toInt() * quan)
                                Product_id = getRef(position).key!!
                                quan--
                            val progressDialog = ProgressDialog(this@CartActivity)
                            progressDialog.setCanceledOnTouchOutside(false)
                            progressDialog.setMessage("Hold On...")
                            progressDialog.show()
                                userRef.child(Product_id).child("quantity").setValue(quan).addOnCompleteListener {
                                    if(it.isSuccessful)
                                        progressDialog.dismiss()
                                }

                            if (quan==0)
                            {
                                userRef.child(Product_id).removeValue()
                            }

                        }
                    }


                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): ProductViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_card_view_v2, parent, false)
                    return ProductViewHolder(view)
                }
            }

        cart_recyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()



    }

    class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var plusBtn : Button = itemView.findViewById(R.id.plus_btn)
        var minusBtn = itemView.findViewById<Button>(R.id.minus_btn)
        var quantView : TextView= itemView.findViewById(R.id.count_view)
        var productName: TextView = itemView.findViewById(R.id.itemViewName2_0)
        var productPrice: TextView = itemView.findViewById(R.id.itemViewPrice2_0)
        var productImageView: ImageView = itemView.findViewById(R.id.itemViewImage2_0)
        var cardView: CardView = itemView.findViewById(R.id.itemCardView2_0)

    }
}
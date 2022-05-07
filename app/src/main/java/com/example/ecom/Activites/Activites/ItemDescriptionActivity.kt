package com.example.ecom.Activites.Activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ecom.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_item_description.*

class ItemDescriptionActivity : AppCompatActivity() {

    private  var selectedItemId: String? = null
    private  var selectedItemName: String? = null
    private  var selectedItemImage: String? = null
    private  var selectedItemPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_description)

         selectedItemImage = intent.extras!!.getString("product_image")
         selectedItemName = intent.extras!!.getString("product_name")
         selectedItemPrice = intent.extras!!.getString("product_price")
         selectedItemId = intent.extras!!.getString("product_id")


        selected_item_Name.text= selectedItemName
        selected_item_Price.text= selectedItemPrice
        Glide.with(this).load(selectedItemImage).into(selected_item_Image)


    }


    override fun onStart() {
        super.onStart()

        buy_now_btn.setOnClickListener {
            Toast.makeText(this, selectedItemId , Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BuyNowActivity::class.java)
            startActivity(intent)
        }

        add_to_cart_btn.setOnClickListener {
           addToCart()
        }

    }

    private fun addToCart(){

        val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                        val userRef= FirebaseAuth.getInstance().currentUser!!.uid

                            val hashMap : MutableMap<String,Any>
                                    = HashMap<String,Any>()
                            hashMap.put("name",selectedItemName!!)
                            hashMap.put("image",selectedItemImage!!)
                            hashMap.put("price",selectedItemPrice!!)
                            hashMap.put("quantity",1)
                            rootRef.child("Cart").child(userRef).child(selectedItemId!!).updateChildren(hashMap).
                            addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(this@ItemDescriptionActivity, "Added Successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    // If sign in fails, display a message to the user.

                                    val e = task.exception.toString()
                                    Toast.makeText(
                                        this@ItemDescriptionActivity, e,
                                        Toast.LENGTH_SHORT
                                    ).show()
//                updateUI(null)
                                }


                            }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}
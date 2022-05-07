package com.example.ecom.Activites.Activites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecom.R
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_buy_now.*
import java.util.*


class BuyNowActivity : AppCompatActivity() {

    lateinit var productRef : DatabaseReference
    lateinit var userRef: String
     lateinit var orderNo : String
    lateinit var totalPrice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_now)

        totalPrice= intent.extras!!.getString("totalPrice")!!
        productRef= FirebaseDatabase.getInstance().reference.child("Orders")
        userRef= FirebaseAuth.getInstance().currentUser!!.uid

    }

    override fun onStart() {
        super.onStart()

        place_order_confirm_btn.setOnClickListener {
            placeOrder()
        }

    }

    private fun placeOrder()
    {
        if(place_order_name.text.toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show()
            return
        }
        if(place_order_phoneNumber.text.toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show()
            return
        }
        if(place_order_pinCode.text.toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter Pin Code", Toast.LENGTH_SHORT).show()
            return
        }
        if(place_order_completeAddress.text.toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show()
            return
        }
        if(place_order_city.text.toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter city", Toast.LENGTH_SHORT).show()
            return
        }
        if(place_order_state.text.toString().isEmpty())
        {
            Toast.makeText(this, "Please Enter State", Toast.LENGTH_SHORT).show()
            return
        }

      storeProductInformation()

    }




    private fun storeProductInformation() {

        val name = place_order_name.text.toString()
        val phnNo = place_order_phoneNumber.text.toString()
        val add = place_order_completeAddress.text.toString()+","+ place_order_city.text.toString()+","+place_order_state.text.toString()+","+place_order_pinCode.text.toString()


        productRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                orderNo = dataSnapshot.child("orderNo").value.toString()


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

        productRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {

                var orderValue= orderNo.toInt()
                orderValue+=1
                val hashMap : MutableMap<String,Any>
                        = HashMap<String,Any>()
                hashMap.put("name", name)
                hashMap.put("phoneNumber",phnNo)
                hashMap.put("address",add)
                hashMap.put("totalPrice",totalPrice)
                productRef.child(userRef).child(orderValue.toString()).updateChildren(hashMap).
                addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this@BuyNowActivity, "Order Placed", Toast.LENGTH_SHORT).show()
                        productRef.child("orderNo").setValue(orderValue)
                        moveFirebaseRecord()

                    } else {
                        // If sign in fails, display a message to the user.

                        val e = task.exception.toString()
                        Toast.makeText(
                            this@BuyNowActivity, e,
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

    fun moveFirebaseRecord() {

        val fromPath= FirebaseDatabase.getInstance().reference.child("Cart").child(userRef)

        fromPath.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productRef.child(userRef).child(orderNo).child("OrderedItems").setValue(dataSnapshot.value
                ).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        fromPath.removeValue()
                        finish()
//                        val i = Intent(this@BuyNowActivity,LoginActivity::class.java)
//                        startActivity(i)
                    }
                }
            }

            fun onCancelled(firebaseError: FirebaseError?) {
                println("Copy failed")
            }
        })
    }

}
package com.example.ecom.Activites.Activites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecom.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admincatagory.*


class AdminCatagoryActivity : AppCompatActivity() {
    private lateinit var  mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admincatagory)

        mAuth = FirebaseAuth.getInstance()
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()

        }

        add_menWear.setOnClickListener {
            addItem("mens")
        }

        add_womenWear.setOnClickListener {
            addItem("womens")
        }

        add_fashion.setOnClickListener {
            addItem("fashion")
        }

        add_laptop.setOnClickListener {
            addItem("laptop")
        }

        add_mobile.setOnClickListener {
            addItem("mobile")
        }

        add_camera.setOnClickListener {
            addItem("camera")
        }

        add_shoes.setOnClickListener {
            addItem("shoes")
        }
        add_earphone.setOnClickListener {
            addItem("earphone")
        }

        add_watches.setOnClickListener {
            addItem("watch")
        }

        add_books.setOnClickListener {
            addItem("books")
        }

        add_others.setOnClickListener {
            addItem("other")
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(this, "Loging out", Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseAuth.getInstance().signOut() 
        Toast.makeText(this, "Loging out", Toast.LENGTH_SHORT).show()
        finishAffinity()

    }

    private fun addItem(category: String) {
       // Toast.makeText(this, category, Toast.LENGTH_SHORT).show()
        var i= Intent(this,AdminAddItemActivity::class.java)
        i.putExtra("categories",category)
        startActivity(i)

    }
}
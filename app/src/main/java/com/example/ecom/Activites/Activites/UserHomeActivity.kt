package com.example.ecom.Activites.Activites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecom.Activites.Activites.adapter.CategoriesAdapter
import com.example.ecom.Activites.Activites.adapter.HomeAdAdapter
import com.example.ecom.Activites.Activites.adapter.ItemViewAdapter
import com.example.ecom.Activites.Activites.modleclass.HomeAdModel
import com.example.ecom.Activites.Activites.modleclass.ItemViewModel
import com.example.ecom.Activites.Activites.modleclass.Supplier
import com.example.ecom.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_home.*
import kotlinx.android.synthetic.main.nav_height.*


class UserHomeActivity : AppCompatActivity()  {

   private lateinit var currentUser : String
    private lateinit var rootRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        val toolbar: Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle =
            ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()



        cart_btn.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }



        nav_view.setNavigationItemSelectedListener {
            when (it.getItemId()) {
                R.id.nav_home -> Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()

                R.id.nav_myCart -> startActivity(Intent(this,CartActivity::class.java))
                R.id.nav_myOrders -> Toast.makeText(this, "myOrders", Toast.LENGTH_SHORT).show()

                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this,LoginActivity::class.java))
                    Toast.makeText(this, "Loging out", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
        categoriesRecyclerView.layoutManager=layoutManager

        val adapter = CategoriesAdapter(this,Supplier.category)
        categoriesRecyclerView.adapter= adapter


        /*-------------------------------------Adv Banner---------------------------*/
        val AdlayoutManager = LinearLayoutManager(this)
        AdlayoutManager.orientation=LinearLayoutManager.HORIZONTAL
        HomeAdRecyclerView.layoutManager= AdlayoutManager

        val adAdapter =
            HomeAdAdapter(
                this,
                HomeAdModel.ad_Supplier.home_ad
            )
        HomeAdRecyclerView.adapter= adAdapter

        /*-------------------------------------Item View---------------------------*/
        val itemViewlayoutManager = GridLayoutManager(this,2)
        ItemViewRecyclerView.layoutManager= itemViewlayoutManager

        val ItemViewAdapter =
            ItemViewAdapter(
                this,
                ItemViewModel.ItemView_Supplier.home_item
            )
        ItemViewRecyclerView.adapter= ItemViewAdapter





    }

    override fun onStart() {
        super.onStart()

        currentUser= FirebaseAuth.getInstance().currentUser!!.uid
        rootRef= FirebaseDatabase.getInstance().reference.child("User").child(currentUser)
        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("name").value.toString()
                username.text= name
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}


package com.example.ecom.Activites.Activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.ecom.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.android.synthetic.main.activity_login.*

class AdminLoginActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var username: TextInputEditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)


        mAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.admin_email_signin)
        password= findViewById(R.id.admin_password_signin)
        username= findViewById(R.id.admin_username_signin)

        switch_admin_signup_btn.setOnClickListener {
            var i = Intent(this, AdminRegisterActivity::class.java)
            startActivity(i)
        }
            admin_signin_btn.setOnClickListener {
            signin()
        }

    }

    private fun signin() {

        val user_email: String = email.text.toString()
        val user_password: String = password.text.toString()
        val user_name: String= username.text.toString()
        if(user_email.isEmpty())
        {
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches())
        {
            Toast.makeText(this, "Email is not Valid", Toast.LENGTH_SHORT).show()
            return
        }
        if (user_password.isEmpty())
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            return
        }
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Signing In")
        progressDialog.setMessage("please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        val userRef: DatabaseReference= FirebaseDatabase.getInstance().reference

        userRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                   if (snapshot.child("Admin").child(user_name).exists())
                   {
                       Toast.makeText(this@AdminLoginActivity, "database kamm krr raha h", Toast.LENGTH_SHORT).show()
                       mAuth.signInWithEmailAndPassword(user_email, user_password)
                           .addOnCompleteListener(
                               this@AdminLoginActivity
                           ) { task ->
                               if (task.isSuccessful) {
                                   // Sign in success, update UI with the signed-in user's information
                                   Toast.makeText(this@AdminLoginActivity, "Welcome", Toast.LENGTH_SHORT).show()
                                   val user = mAuth.currentUser
                                   startActivity(Intent(this@AdminLoginActivity,AdminCatagoryActivity::class.java))
                                   progressDialog.dismiss()
                                   finishAffinity()
                                   //updateUI(user)
                               } else {
                                   // If sign in fails, display a message to the user.
                                   val e = task.exception.toString()
                                   progressDialog.dismiss()
                                   Toast.makeText(
                                       this@AdminLoginActivity, "Authentication failed: $e",
                                       Toast.LENGTH_SHORT
                                   ).show()
                                   //updateUI(null)
                               }

                           }
                   }
                    else{
                       progressDialog.dismiss()
                       Toast.makeText(this@AdminLoginActivity, "Seller not exist", Toast.LENGTH_SHORT).show()
                   }
                }
            }
        )



    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this,UserHomeActivity::class.java))
            finish()
        }
        else
        {


        }
    }


}

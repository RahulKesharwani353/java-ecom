package com.example.ecom.Activites.Activites

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecom.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email_signin)
        password= findViewById(R.id.password_signin)

        switch_signup_btn.setOnClickListener {
            var i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
        }
        signin_btn .setOnClickListener {
            signin()
        }
        admin_btn.setOnClickListener {
            startActivity(Intent(this,AdminLoginActivity::class.java))

        }

    }

    private fun signin() {

        val user_email: String = email.text.toString()
        val user_password: String = password.text.toString()
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

        progressDialog.setTitle("Singing In")
        progressDialog.setMessage("please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()


        mAuth.signInWithEmailAndPassword(user_email, user_password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                    val user = mAuth.currentUser
                    startActivity(Intent(this,UserHomeActivity::class.java))
                    progressDialog.dismiss()
                    finishAffinity()
                  //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    val e = task.exception.toString()
                    Toast.makeText(
                        this, "Authentication failed: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                  //updateUI(null)
                }

            }
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
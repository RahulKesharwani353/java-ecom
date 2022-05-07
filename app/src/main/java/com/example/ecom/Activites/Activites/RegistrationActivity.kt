package com.example.ecom.Activites.Activites

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecom.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var name: TextInputEditText
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()
        name= findViewById(R.id.Name_signup)
        email = findViewById(R.id.email_signup)
        password= findViewById(R.id.password_signup)
        confirmPassword= findViewById(R.id.confirm_password_signup)

        signup_btn.setOnClickListener {
            register()
        }
    }//onCreate


    private fun register() {
        val user_name: String = name.text.toString()
        val user_email: String = email.text.toString()
        val user_password: String = password.text.toString()
        val conf_password: String = confirmPassword.text.toString()
        if(user_name.isEmpty())
        {
            Toast.makeText(this, "enter username", Toast.LENGTH_SHORT).show()
            return
        }
        if(user_email.isEmpty())
        {
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show()
            return
        }
        if (user_password != conf_password)
        {
            Toast.makeText(this, "entered Password and Confirmation Password not matches", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches())
        {
            Toast.makeText(this, "Email is not Valid", Toast.LENGTH_SHORT).show()
            return
        }
        if (user_password.length<6)
        {
        Toast.makeText(this, "Password should be of min 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Singing Up")
        progressDialog.setMessage("please wait")
        progressDialog.show()

/*this is for authentication*/



        val rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()

        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val User : String= "User"


                            mAuth.createUserWithEmailAndPassword(user_email, user_password)
                                .addOnCompleteListener(
                                    this@RegistrationActivity
                                ) { task ->
                                    val userRef= FirebaseAuth.getInstance().currentUser!!.uid
                                    if (!(dataSnapshot.child(User).child(userRef).exists()))
                                    {
                                        val hashMap : MutableMap<String,Any>
                                                = HashMap<String,Any>()
                                        hashMap.put("name",user_name)
                                        hashMap.put("email",user_email)
                                        hashMap.put("password",user_password)
                                        Toast.makeText(this@RegistrationActivity, hashMap.toString(), Toast.LENGTH_SHORT).show()
                                        rootRef.child(User).child(userRef).updateChildren(hashMap).
                                        addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(this@RegistrationActivity, "Register Successfully", Toast.LENGTH_SHORT).show()
                                        progressDialog.dismiss()
                                        startActivity(Intent(this@RegistrationActivity,UserHomeActivity::class.java))
                                        finishAffinity()
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressDialog.dismiss()

                                        val e = task.exception.toString()
                                        Toast.makeText(
                                            this@RegistrationActivity, e,
                                            Toast.LENGTH_SHORT
                                        ).show()
//                updateUI(null)
                                    }


                                }

                        }
                        else
                            Toast.makeText(this@RegistrationActivity, "Error", Toast.LENGTH_SHORT).show()
                    }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })


/*-------------------FireBase Auth-------------*/
//    mAuth.createUserWithEmailAndPassword(user_email, user_password)
//        .addOnCompleteListener(
//            this
//        ) { task ->
//            if (task.isSuccessful) {
//                // Sign in success, update UI with the signed-in user's information
//                Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
//                progressDialog.dismiss()
//                startActivity(Intent(this,UserHomeActivity::class.java))
//                finishAffinity()
//            } else {
//                // If sign in fails, display a message to the user.
//                progressDialog.dismiss()
//                Toast.makeText(
//                    this, "Authentication failed.",
//                    Toast.LENGTH_SHORT
//                ).show()
////                updateUI(null)
//            }
//
//
//        }


}//Regidter
}
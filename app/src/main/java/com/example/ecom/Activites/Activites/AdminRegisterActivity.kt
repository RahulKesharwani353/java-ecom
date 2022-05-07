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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_register.*
import kotlinx.android.synthetic.main.activity_registration.*

class AdminRegisterActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var name: TextInputEditText
    lateinit var progressBar: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_register)
            mAuth = FirebaseAuth.getInstance()
            name= findViewById(R.id.admin_name_signup)
            email = findViewById(R.id.admi_email_signup)
            password= findViewById(R.id.admin_password_signup)
            confirmPassword= findViewById(R.id.admin_confirm_password_signup)

            admin_createAccount.setOnClickListener {
                adminRegister()
            }
        }//onCreate


        private fun adminRegister() {
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
            progressDialog.setTitle("Creating Seller")
            progressDialog.setMessage("please wait")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

/*this is for authentication*/



            val rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()

            rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot)
                {
                    val User : String= "Admin"
                    if (!(dataSnapshot.child(User).child(user_name).exists()))
                    {
                        val hashMap : MutableMap<String,Any>
                                = HashMap<String,Any>()
                        hashMap.put("name",user_name)
                        hashMap.put("email",user_email)
                        hashMap.put("password",user_password)
                        rootRef.child(User).child(user_name).updateChildren(hashMap).
                        addOnCompleteListener { task ->
                            if (task.isSuccessful)
                            {
                                mAuth.createUserWithEmailAndPassword(user_email, user_password)
                                    .addOnCompleteListener(
                                        this@AdminRegisterActivity
                                    ) { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(this@AdminRegisterActivity, "Register Successfully", Toast.LENGTH_SHORT).show()
                                            progressDialog.dismiss()
                                            startActivity(Intent(this@AdminRegisterActivity,AdminCatagoryActivity::class.java))
                                            finishAffinity()
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            progressDialog.dismiss()

                                            val e = task.exception.toString()
                                            Toast.makeText(
                                                this@AdminRegisterActivity, e,
                                                Toast.LENGTH_SHORT
                                            ).show()
//                updateUI(null)
                                        }


                                    }

                            }
                            else{
                                Toast.makeText(this@AdminRegisterActivity, "Error", Toast.LENGTH_SHORT).show()
                                progressDialog.dismiss()
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(this@AdminRegisterActivity, " $user_name Already A Seller", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
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

        }//Regid


}
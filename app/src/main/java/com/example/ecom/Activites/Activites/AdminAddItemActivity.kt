package com.example.ecom.Activites.Activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecom.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_admin_add_item.*
import java.text.SimpleDateFormat
import java.util.*

class AdminAddItemActivity : AppCompatActivity() {
    private lateinit var productName: TextInputEditText
    private lateinit var productDescription: TextInputEditText
    private lateinit var productPrice: TextInputEditText
    private lateinit var imageUri: Uri
    private lateinit var productRandomKey: String
    private lateinit var downloadImageUrl: String
    private lateinit var productRef: StorageReference
    private lateinit var productDataRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_item)

        val addCategoryItem = intent.extras!!.getString("categories")
        addCategoryText.text = addCategoryItem

        productName = findViewById(R.id.addProductName)
        productDescription = findViewById(R.id.addProductDescription)
        productPrice = findViewById(R.id.addProductPrice)
        productRef = FirebaseStorage.getInstance().reference.child("products")
        productDataRef =
            FirebaseDatabase.getInstance().reference.child("products").child(addCategoryItem!!)




        addProduct_btn.setOnClickListener {
            addProduct()
        }

        addProductImage.setOnClickListener {
            val i = Intent()
            i.action = Intent.ACTION_GET_CONTENT
            i.type = "image/*"
            startActivityForResult(i, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataIntent)

        if (requestCode == 1 && resultCode == RESULT_OK && dataIntent != null) {
           imageUri = dataIntent.data!!
            addProductImage.setImageURI(imageUri)
        }
    }

    private fun addProduct() {
        var progressDialog = ProgressDialog(this)
        var name = productName.text.toString()
        var description = productDescription.text.toString()
        var price = productPrice.text.toString()

        if (imageUri == null) {
            Toast.makeText(this, "upload image", Toast.LENGTH_SHORT).show()

        }
        else if (description.isEmpty() || name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "please enter all fields", Toast.LENGTH_SHORT).show()

        } else {

            progressDialog.setTitle("Adding")
            progressDialog.setMessage("please wait")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            storeProductInformation()
        }
    }

    @SuppressLint("SimpleDateFormat")

    private fun storeProductInformation() {
        val progressDialog = ProgressDialog(this)

        val saveCurrentDate: String =
            SimpleDateFormat("MM dd, yyyy").format(Calendar.getInstance().time)
        val saveCurrentTime: String =
            SimpleDateFormat("HH:mm:ss a").format(Calendar.getInstance().time)
        productRandomKey = saveCurrentDate + saveCurrentTime

        val filePath = productRef.child("$productRandomKey.jpg")
        val uploadTask: UploadTask = filePath.putFile(imageUri)
        uploadTask.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) throw task.exception!!
            downloadImageUrl = filePath.downloadUrl.toString()
            filePath.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isComplete) {
                downloadImageUrl = task.result.toString()
                val profileMap =
                    HashMap<String, Any>()
                profileMap["admin"] = FirebaseAuth.getInstance().currentUser!!.uid
                profileMap["name"] = productName.text.toString()
                profileMap["description"] = productDescription.text.toString()
                profileMap["price"] = productPrice.text.toString()
                profileMap["image"] = downloadImageUrl

                productDataRef.child(productRandomKey)
                    .updateChildren(profileMap)
                    .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
//                            val intent =
//                                Intent(this, AdminCatagoryActivity::class.java)
//                            startActivity(intent)
                            finish()
                            progressDialog.dismiss()
                            Toast.makeText(
                                this,
                                "Added....",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
}

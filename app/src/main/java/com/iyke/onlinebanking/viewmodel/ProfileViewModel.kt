package com.iyke.onlinebanking.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentEditProfileBinding
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.utils.Constants.EMAIL
import com.iyke.onlinebanking.utils.Constants.IMAGES
import com.iyke.onlinebanking.utils.Constants.NAME
import com.iyke.onlinebanking.utils.Constants.PHONE_NUMBER
import com.iyke.onlinebanking.utils.Constants.PROFILE
import com.iyke.onlinebanking.utils.Constants.USERS
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*


class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    var filePath: Uri? = null


    private val context = getApplication<Application>().applicationContext
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageReference: StorageReference = storage.reference


    val name = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()


    val sh: SharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
    private val firebaseEmail = sh.getString(Constants.EMAIL, "")

    val PICK_IMAGE_REQUEST = 71


    init {
        name.value = ""
        phoneNumber.value = ""
    }

    private fun updateUserData(progressDialog :android.app.ProgressDialog) {


        val user = hashMapOf(
            NAME to  name.value.toString(),
            PHONE_NUMBER to phoneNumber.value.toString(),
            PROFILE to filePath.toString()
        )

        db.collection(USERS).document(firebaseEmail!!).update(user as Map<String, Any>)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(context, "updated successfully", Toast.LENGTH_SHORT).show()


            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context, "failed to update user", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    fun profileNavigation(view: View) {
        when (view.id) {
            R.id.profileUpdate -> uploadImage(view)
        }
    }

    private fun uploadImage(view: View) {
        val progressDialog =  android.app.ProgressDialog(view.context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        if (filePath != null) {
            val ref: StorageReference =
                storageReference.child(IMAGES + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    updateUserData(progressDialog)

                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(context, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                        .totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }else{
            updateUserData(progressDialog)
        }
    }
}
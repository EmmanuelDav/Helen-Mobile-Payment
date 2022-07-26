package com.iyke.onlinebanking.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentEditProfileBinding
import com.iyke.onlinebanking.viewmodel.ProfileViewModel
import java.io.IOException


class EditProfileFragment : Fragment() {

    private var profileViewModel: ProfileViewModel? = null
    lateinit var v: FragmentEditProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        v.profileViewModel = profileViewModel
        v.exitEdit.setOnClickListener { findNavController().popBackStack() }
        v.changeImage.setOnClickListener { chooseImage() }
        return v.root
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            profileViewModel!!.PICK_IMAGE_REQUEST,
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == profileViewModel!!.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            profileViewModel!!.filePath = data.data
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, profileViewModel!!.filePath)
                context?.let { Glide.with(it).load(bitmap).into(v.changeImage) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
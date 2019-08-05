package com.sa.alarm.home.profile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.dialog_select_camera.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.etEmail
import kotlinx.android.synthetic.main.toolbar_main.view.ivBack
import kotlinx.android.synthetic.main.toolbar_main.view.tvTitle
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import android.graphics.Bitmap
import android.net.Uri
import java.io.ByteArrayOutputStream
import android.content.ContentValues
import com.sa.alarm.utils.PermissionUtils

class ProfileFragment : BaseFragment() {
    companion object {
        private var profileInstance: ProfileFragment? = null
        val TAG: String = "ProfileFragment"
        var dialog: Dialog? = null

        fun getInstance(): ProfileFragment {
            if (profileInstance == null) {
                profileInstance = ProfileFragment()
            }
            return profileInstance as ProfileFragment
        }
    }

    private val ACTION_PICK_GALLERY: Int = 10
    private val ACTION_PICK_CAMERA: Int = 11
    private val REQUEST_GALLARY = 101
    private val REQUEST_CAMERA = 102
    private var cameraPermission = android.Manifest.permission.CAMERA
    private var storagePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private lateinit var imageUri: Uri
    private lateinit var profileViewModel: ProfileViewmodel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG, "onCreateViews")
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        toolbar.ivBack.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate();
        }

        profileViewModel.getUser().observe(this, Observer { user ->
            etEmail.setText(user.email)
            etName.setText(user.displayName)
            tvName.setText(user.displayName)

            Glide.with(this)
                .load(user.photoUrl)
                .into(civProfile)
        })

        btnSubmitPofile.setOnClickListener {
            if (isValid() && !profileViewModel.getProgresBar().value!!) {
                profileViewModel.submitData(etName.text.toString())
            }
        }

        profileViewModel.getProgresBar().observe(this, Observer { isLoading ->
                        if (isLoading) pgbProfile.visibility = View.VISIBLE else pgbProfile.visibility = View.GONE
        })

        camera.setOnClickListener {
            dialog = Dialog(context!!)
            dialog?.setContentView(R.layout.dialog_select_camera);

            dialog?.tvSelectGallary?.setOnClickListener {
                if (PermissionUtils.hasPermissions(context, storagePermission)) {
                    val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, ACTION_PICK_GALLERY)
                } else {
                    ActivityCompat.requestPermissions(activity!!, arrayOf(storagePermission), REQUEST_GALLARY);
                }
            }

            dialog?.tvSelectCamera?.setOnClickListener {
                if (PermissionUtils.hasPermissions(context, cameraPermission, storagePermission)) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "image")
                    values.put(MediaStore.Images.Media.DESCRIPTION, "timestamp " + System.currentTimeMillis())
                    imageUri =
                        activity!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, ACTION_PICK_CAMERA)

                } else {
                    ActivityCompat.requestPermissions(
                        activity!!,
                        arrayOf(cameraPermission, storagePermission),
                        REQUEST_CAMERA
                    );
                }
            }

            val window = dialog?.getWindow()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            dialog?.show()
        }
    }

    private fun isValid(): Boolean {
        if (etName.text.toString().isEmpty()) {
            etName.error = resources.getString(R.string.error_email_empty)
            return false
        }
        return true
    }

    private fun init() {
        toolbar.tvTitle.setText(resources.getText(R.string.title_edit_profile))
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewmodel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            dialog?.dismiss()
            if (requestCode == ACTION_PICK_GALLERY && data != null) {
                val targetUri = data.data
                val bitmap = BitmapFactory.decodeStream(activity!!.contentResolver.openInputStream(targetUri!!));
                civProfile.setImageBitmap(bitmap)
                profileViewModel.updateProfile(targetUri)
            }
            if (requestCode == ACTION_PICK_CAMERA) {
                val targetBitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
                val uri = getImageUri(context!!, targetBitmap!!);
                civProfile.setImageBitmap(targetBitmap)

                profileViewModel.updateProfile(uri)
            }
        }
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
}
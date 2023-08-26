package com.ziss.storyapp.presentation.ui.activities

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ziss.storyapp.MainActivity
import com.ziss.storyapp.R
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.databinding.ActivityAddStoryBinding
import com.ziss.storyapp.presentation.viewmodels.AuthViewModel
import com.ziss.storyapp.presentation.viewmodels.StoryViewModel
import com.ziss.storyapp.presentation.viewmodels.ViewModelFactory
import com.ziss.storyapp.utils.ResultState
import com.ziss.storyapp.utils.createCustomTempFile
import com.ziss.storyapp.utils.toFile
import java.io.File

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var factory: ViewModelFactory

    private lateinit var currentPhotoPath: String

    private val authViewModel: AuthViewModel by viewModels { factory }
    private val storyViewModel: StoryViewModel by viewModels { factory }

    private var imageFile: File? = null
    private var token: String? = null

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val myFile = File(currentPhotoPath)
                imageFile = myFile
                setPreviewImage(myFile)
            }
        }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val selectedImage = it?.data?.data as Uri
                val file = selectedImage.toFile(this@AddStoryActivity)
                imageFile = file
                setPreviewImage(file)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarLayout.toolbar)
        supportActionBar?.title = getString(R.string.new_story_title)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        factory = ViewModelFactory.getInstance(dataStore)

        fetchToken()
        playAnimation()

        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.buttonAdd.setOnClickListener(this)

        binding.ivPreview.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> setUploadButtonEnable() }
        binding.edAddDescription.addTextChangedListener { setUploadButtonEnable() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.not_getting_permissions),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_camera -> startTakePhoto()
            R.id.btn_gallery -> startGallery()
            R.id.button_add -> uploadStory()
        }
    }

    private fun allPermissionGranted() = REQUIRE_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun playAnimation() {
        val viewObjects = listOf(
            binding.frame,
            binding.ivPreview,
            binding.btnCamera,
            binding.btnGallery,
            binding.edAddDescription,
            binding.buttonAdd
        )

        val objectAnimators = viewObjects.map {
            ObjectAnimator.ofFloat(it, View.ALPHA, 1f).apply {
                duration = 100
                startDelay = 50
            }
        }

        AnimatorSet().apply {
            playSequentially(objectAnimators)
            start()
        }
    }

    private fun setPreviewImage(file: File) {
        binding.frame.visibility = View.INVISIBLE
        binding.ivPreview.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.ivPreview.clipToOutline = true
        binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
    }

    private fun showLoading(loading: Boolean = true) {
        binding.buttonAdd.isClickable = !loading

        if (loading) {
            binding.buttonAdd.visibility = View.INVISIBLE
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            binding.buttonAdd.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.GONE
        }
    }

    private fun setUploadButtonEnable() {
        val isEnabled = imageFile != null && !binding.edAddDescription.text.isNullOrEmpty()
        Log.d("ENABLED", isEnabled.toString())
        binding.buttonAdd.isEnabled = isEnabled
    }

    private fun fetchToken() {
        authViewModel.getToken().observe(this) { token = it }
    }

    private fun uploadStory() {
        if (binding.edAddDescription.text.isNullOrEmpty()) {
            binding.edAddDescription.error = getString(R.string.empty_desc_alert)
            return
        }

        val description = binding.edAddDescription.text.toString()

        if (token != null) {
            storyViewModel.addStory(token!!, imageFile!!, description).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> showLoading()
                    is ResultState.Success -> {
                        showLoading(false)

                        if (!result.data.error) {
                            MainActivity.start(this)
                            finish()
                        } else {
                            Snackbar.make(binding.root, result.data.message, Snackbar.LENGTH_LONG)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                                .setBackgroundTint(this.getColor(R.color.orange)).show()
                        }
                    }

                    is ResultState.Failed -> {
                        showLoading(false)
                        Log.d("Error", result.message)
                    }
                }
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, getString(R.string.camera_authority), it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"

            val chooser = Intent.createChooser(this, getString(R.string.choose_a_picture))
            launcherGallery.launch(chooser)
        }
    }

    companion object {
        private val REQUIRE_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        fun start(context: Context) {
            val intent = Intent(context, AddStoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
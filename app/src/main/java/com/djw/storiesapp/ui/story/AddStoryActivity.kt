package com.djw.storiesapp.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.djw.storiesapp.R
import com.djw.storiesapp.databinding.ActivityAddStoryBinding
import com.djw.storiesapp.ui.StoryViewModelFactory
import com.djw.storiesapp.ui.signin.SigninActivity
import com.djw.storiesapp.createTempFile
import com.djw.storiesapp.reduceFileImage
import com.djw.storiesapp.uriToFile
import com.djw.storiesapp.data.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var storyViewModel: AddStoryViewModel
    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, resources.getString(R.string.no_permission), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        this.title = resources.getString(R.string.app_nameAddStory)

        setupViewModel()
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun setupViewModel() {
        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        storyViewModel = ViewModelProvider(this, factory).get(
            AddStoryViewModel::class.java
        )

        storyViewModel.getToken().observe(this) { token ->
            if (token.isEmpty()) {
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
            } else {
                this.token = token
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_image))
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.djw.storiesapp.ui.story",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.previewImage.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImage.setImageBitmap(result)
        }
    }

    private fun uploadImage(){
        if (getFile != null){
            val edAddDescText = binding.edAddDescription.text.toString()
            if (edAddDescText.isEmpty()) {
                binding.edAddDescription.error = resources.getString(R.string.input_message, "Description")
            } else {
                val description = edAddDescText.toRequestBody("text/plain".toMediaType())
                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                storyViewModel.addNew(token, imageMultipart, description).observe(this) { result ->
                    if (result != null){
                        when(result){
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                                finish()
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this@AddStoryActivity, resources.getString(R.string.no_image), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLayout.visibility = View.VISIBLE
        } else {
            binding.progressBarLayout.visibility = View.GONE
        }
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
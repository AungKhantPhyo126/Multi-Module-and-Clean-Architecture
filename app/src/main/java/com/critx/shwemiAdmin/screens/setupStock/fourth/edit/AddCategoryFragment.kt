package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.critx.common.ui.getAlertDialog
import com.critx.shwemiAdmin.databinding.FragmentAddCategoryBinding
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class AddCategoryFragment:Fragment() {
    private lateinit var binding:FragmentAddCategoryBinding
    private val viewModel by viewModels<AddCategoryViewModel>()

    private var isFrequentlyUsed = 0
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var launchChooseImage1: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage2: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage3: ActivityResultLauncher<Intent>
    private lateinit var launchChooseVideo: ActivityResultLauncher<Intent>
    private lateinit var launchChooseGif: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    viewModel.selectedImgUri = getRealPathFromUri(requireContext(),it)?.let { it1 ->
                        File(
                            it1
                        )
                    }
                }
                if (result.data != null && result?.data?.data != null) {
                    val selectedImageUri: Uri? = result.data!!.data
                    binding.ivImage1.setImageURI(selectedImageUri)
                }
            }

        }
        launchChooseImage2 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {
                        viewModel.selectedImgUri = getRealPathFromUri(requireContext(),it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
                        binding.ivImage2.setImageURI(selectedImageUri)
                    }
                }

            }
        launchChooseImage3 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {
                        viewModel.selectedImgUri = getRealPathFromUri(requireContext(),it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
                        binding.ivImage3.setImageURI(selectedImageUri)
                    }
                }

            }

        launchChooseVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {

                        viewModel.selectedVideoUri = getRealPathFromUri(requireContext(),it)?.let { it1 ->

                            File(
                                it1
                            )
                        }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
//                        binding.ivVideo.setVideoURI(selectedImageUri)
//                        binding.ivVideo.pause()
                        val bmThumbnail = getRealVideoPathFromUri(requireContext(),
                                result.data!!.data!!
                            ).let {
                                val thumbnail = ThumbnailUtils.createVideoThumbnail(File(it!!),
                                    Size(500,500), CancellationSignal()
                                )
                            binding.ivVideo.setImageBitmap(thumbnail)
                        }
                    }
                }
            }

        launchChooseGif =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {

                        viewModel.selectedVideoUri = getRealPathFromUri(requireContext(),it)?.let { it1 ->

                            File(
                                it1
                            )
                        }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
//                        binding.ivVideo.setVideoURI(selectedImageUri)
//                        binding.ivVideo.pause()
                        val bmThumbnail = getRealVideoPathFromUri(requireContext(),
                            result.data!!.data!!
                        ).let {
                            val thumbnail = ThumbnailUtils.createVideoThumbnail(File(it!!),
                                Size(500,500), CancellationSignal()
                            )
                            binding.ivGif.setImageBitmap(thumbnail)
                        }


                    }
                }

            }

        readStoragePermissionlauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddCategoryBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()

        binding.ivImage1.setOnClickListener {
            uploadImageClick("iv1")
        }
        binding.ivImage2.setOnClickListener {
            uploadImageClick("iv2")
        }
        binding.ivImage3.setOnClickListener {
            uploadImageClick("iv3")
        }
        binding.ivVideo.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()){
                chooseVideo()
            }else{
                requestPermission()
            }
        }
        binding.ivGif.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()){
                chooseGif()
            }else{
                requestPermission()
            }
        }
    }

    fun uploadImageClick(viewName:String){
        if (isReadExternalStoragePermissionGranted()){
            when(viewName){
                "iv1"->chooseImage1()
                "iv2"->chooseImage2()
                "iv3"->chooseImage3()
            }
        }else{
            requestPermission()
        }
    }

    fun chooseImage1() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        launchChooseImage1.launch(pickIntent)
    }
    fun chooseImage2() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        launchChooseImage2.launch(pickIntent)
    }
    fun chooseImage3() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        launchChooseImage3.launch(pickIntent)
    }
    fun chooseVideo() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "video/*"
        launchChooseVideo.launch(pickIntent)
    }
    fun chooseGif() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "video/*"
        launchChooseGif.launch(pickIntent)
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(){
        readStoragePermissionlauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


}

fun getRealVideoPathFromUri(context: Context, contentUri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        cursor?.getString(column_index)
    } finally {
        if (cursor != null) {
            cursor.close()
        }
    }
}
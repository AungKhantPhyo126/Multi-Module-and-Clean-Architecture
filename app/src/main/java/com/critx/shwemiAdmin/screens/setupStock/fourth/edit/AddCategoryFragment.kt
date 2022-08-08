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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentAddCategoryBinding
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@AndroidEntryPoint
class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private val viewModel by activityViewModels<AddCategoryViewModel>()
    private val args by navArgs<AddCategoryFragmentArgs>()

    private var isFrequentlyUsed = 0
    private var orderToGs = 0
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
                        viewModel.selectedImgUri1 =
                            getRealPathFromUri(requireContext(), it)?.let { it1 ->
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
                        viewModel.selectedImgUri2 =
                            getRealPathFromUri(requireContext(), it)?.let { it1 ->
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
                        viewModel.selectedImgUri3 =
                            getRealPathFromUri(requireContext(), it)?.let { it1 ->
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

                        viewModel.selectedVideoUri =
                            getRealPathFromUri(requireContext(), it)?.let { it1 ->

                                File(
                                    it1
                                )
                            }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
//                        binding.ivVideo.setVideoURI(selectedImageUri)
//                        binding.ivVideo.pause()
                        val bmThumbnail = getRealVideoPathFromUri(
                            requireContext(),
                            result.data!!.data!!
                        ).let {
                            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                                File(it!!),
                                Size(500, 500), CancellationSignal()
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

                        viewModel.selectedGifUri =
                            getRealPathFromUri(requireContext(), it)?.let { it1 ->

                                File(
                                    it1
                                )
                            }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
//                        binding.ivVideo.setVideoURI(selectedImageUri)
//                        binding.ivVideo.pause()
                        val bmThumbnail = getRealVideoPathFromUri(
                            requireContext(),
                            result.data!!.data!!
                        ).let {
                            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                                File(it!!),
                                Size(500, 500), CancellationSignal()
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
            if (isReadExternalStoragePermissionGranted()) {
                chooseVideo()
            } else {
                requestPermission()
            }
        }
        binding.ivGif.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseGif()
            } else {
                requestPermission()
            }
        }

        binding.btnConfirm.setOnClickListener {
            uploadFile()
        }
        binding.mcvChooseDesign.setOnClickListener {
            findNavController().navigate(AddCategoryFragmentDirections.actionAddCategoryFragmentToDesignListFragment())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //createGroup
                launch {
                    viewModel.createJewelleryCategoryState.collectLatest {
                        if (it.createLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (!it.createSuccessLoading.isNullOrEmpty()) {
                            requireContext().showSuccessDialog("Group Created") {
                                findNavController().popBackStack()
                            }
                        }
                        if (it.calculateKPYSuccessLoading != null) {
                            viewModel.calculatedKPYtoGram = it.calculateKPYSuccessLoading
                        }
                    }
                }



                launch {
                    viewModel.event.collectLatest { event ->
                        when (event) {
                            is UiEvent.ShowErrorSnackBar -> {
                                snackBar?.dismiss()
                                snackBar = Snackbar.make(
                                    binding.root,
                                    event.message,
                                    Snackbar.LENGTH_LONG
                                )
                                snackBar?.show()
                            }
                        }
                    }
                }
            }
        }


    }


    fun uploadImageClick(viewName: String) {
        if (isReadExternalStoragePermissionGranted()) {
            when (viewName) {
                "iv1" -> chooseImage1()
                "iv2" -> chooseImage2()
                "iv3" -> chooseImage3()
            }
        } else {
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

    fun requestPermission() {
        readStoragePermissionlauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun uploadFile() {
        viewModel.calculateKPYtoGram(
            binding.edtK.text.toString().toDouble(),
            binding.edtP.text.toString().toDouble(),
            binding.edtY.text.toString().toDouble(),
        )
        var photo1: MultipartBody.Part? = null
        var photo2: MultipartBody.Part? = null
        var photo3: MultipartBody.Part? = null
        var video: MultipartBody.Part? = null
        var gif: MultipartBody.Part? = null


        viewModel.selectedImgUri1?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo1 = MultipartBody.Part.createFormData("image", it.name, requestBody)
        }
        viewModel.selectedImgUri2?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo2 = MultipartBody.Part.createFormData("image", it.name, requestBody)
        }
        viewModel.selectedImgUri3?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo3 = MultipartBody.Part.createFormData("image", it.name, requestBody)
        }
        viewModel.selectedVideoUri?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            video = MultipartBody.Part.createFormData("video", it.name, requestBody)
        }
        viewModel.selectedGifUri?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            gif = MultipartBody.Part.createFormData("video", it.name, requestBody)
        }
        val photoList = mutableListOf(photo1!!, photo2!!, photo3!!)
//        val videoList = mutableListOf(video!!)
        val designList = mutableListOf<RequestBody>()
        viewModel.selectedDesignIds?.let { list ->
            list.forEach {
               designList.add(it.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))
            }
        }

        val name = binding.edtEnterCategory.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val specification = binding.edtSpecification.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val avgWeigh = binding.edtAvgWeight.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val avgWastage = viewModel.calculatedKPYtoGram.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())


        viewModel.createJewelleryCategory(
            args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            args.groupInfo.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            isFrequentlyUsed.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            name,
            avgWeigh,
            avgWastage,
            photoList,
            video!!,
            specification,
            designList,
            orderToGs.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            )
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
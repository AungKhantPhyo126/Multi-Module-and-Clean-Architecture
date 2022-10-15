package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentOutsideBinding
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.convertBitmapToFile
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class OutSideFragment : Fragment() {
    private lateinit var binding: FragmentOutsideBinding
    private val viewModel by viewModels<OutSideViewModel>()
    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    var photo: MultipartBody.Part? = null
    private lateinit var loadingDialog: AlertDialog
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOutsideBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                var selectedImage: Bitmap?
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {
                        val imageStream: InputStream =
                            requireContext().contentResolver?.openInputStream(it)!!
                        selectedImage = BitmapFactory.decodeStream(imageStream)
//                        selectedImage = getResizedBitmap(
//                            selectedImage!!,
//                            500
//                        );// 400 is for example, replace with desired size
                        Log.i("imageResize", selectedImage?.width.toString())
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.setSelectedImgUri(SelectedImage(file!!, selectedImage!!))
                    }
                }

            }
        readStoragePermissionlauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) chooseImage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        binding.layoutBtnGroup.btnSave.setOnClickListener {
            if (photo != null) {
                viewModel.saveOusideSample(
                    binding.edtStockName.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.edtWeigh.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.edtSpecification.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    photo!!
                )
            }else{
                Toast.makeText(requireContext(),"Please upload a photo",Toast.LENGTH_LONG).show()
            }

        }
        binding.ivOutside.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }

        viewModel.selectedImgUri.observe(viewLifecycleOwner) {
            if (it != null) {
                val requestBody = convertBitmapToFile(
                    it.file.name,
                    it.bitMap,
                    requireContext()
                ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                binding.ivOutside.setImageBitmap(it.bitMap)
                photo = MultipartBody.Part.createFormData("image", it.file.name, requestBody)
            }
        }
//
        viewModel.saveOutsideSample.observe(viewLifecycleOwner){
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!){

                    }
                    viewModel.resetSaveOutSideSample()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }
    }
    fun chooseImage() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        launchChooseImage.launch(pickIntent)
    }

    fun clearText() {
        binding.edtSpecification.text?.clear()
        binding.edtWeigh.text?.clear()
        binding.edtStockName.text?.clear()
    }

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        readStoragePermissionlauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
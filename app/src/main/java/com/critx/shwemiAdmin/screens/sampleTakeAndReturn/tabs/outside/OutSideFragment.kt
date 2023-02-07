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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentOutsideBinding
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.GIVE_GOLD_STATE
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.ORDER_STOCK_STATE
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.SampleTakeAndReturnDirections
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class OutSideFragment : Fragment() {
    private lateinit var binding: FragmentOutsideBinding
    private val viewModel by viewModels<OutSideViewModel>()
    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var loadingDialog: AlertDialog
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var snackBar: Snackbar? = null


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
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    if (data != null && data.data != null) {
                        getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                            viewModel.selectedImgUri = File(path)
                        }
                        binding.ivOutside.setImageURI(data.data)
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

        /**sampleReturn**/
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            if (checkedButtonId == binding.radioButtonSampleTake.id) {
                binding.layoutSampleTakeOutside.isVisible = true
                binding.layoutSampleReturn.root.isVisible = false
                binding.layoutBtnReturnSample.root.isVisible = false
            } else if (checkedButtonId == binding.radioButtonSampleReturn.id) {
                viewModel.getOutSideSampleList()
                binding.layoutSampleTakeOutside.isVisible = false
                binding.layoutSampleReturn.root.isVisible = true
                binding.layoutBtnReturnSample.root.isVisible = true

            }
        }
        binding.layoutBtnReturnSample.btnSampleReturn.setOnClickListener {
            viewModel.returnSample(viewModel.getSelectedOutsideSample())
        }
        val sampleReturnRecyclerAdapter = SampleReturnInventoryRecyclerAdapter( {
            viewModel.selectSampleForReturn(it)
        },{
            findNavController().navigate(SampleTakeAndReturnDirections.actionGlobalPhotoViewFragment(it))
        })
        binding.layoutSampleReturn.rvSampleReturnInventory.adapter = sampleReturnRecyclerAdapter

        viewModel.getOutsideSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    sampleReturnRecyclerAdapter.submitList(it.data)
                    sampleReturnRecyclerAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.returnSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!){
                        viewModel.getOutSideSampleList()
                    }
                    viewModel.resetReturnSampleLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        /**  **/

//        binding.layoutBtnGroup.btnAddToHandedList.isEnabled =
//            sharedViewModel.sampleTakeScreenUIState != GIVE_GOLD_STATE ||
//                    sharedViewModel.sampleTakeScreenUIState != ORDER_STOCK_STATE
        binding.layoutBtnGroup.btnSave.setOnClickListener {
            if (viewModel.selectedImgUri != null) {
                var photo: MultipartBody.Part? = null
                var requestBody1: RequestBody? = null
                viewModel.selectedImgUri?.let {
                    requestBody1 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    photo = MultipartBody.Part.createFormData(
                        "image",
                        it.name,
                        requestBody1!!
                    )
                }
                viewModel.saveOusideSample(
                    binding.edtStockName.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.edtWeigh.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.edtSpecification.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    photo!!
                )
            } else {
                Toast.makeText(requireContext(), "Please upload a photo", Toast.LENGTH_LONG).show()
            }

        }
        binding.ivOutside.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }

        viewModel.addToHandedListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {

                    }
                    viewModel.resetAddtoHandleListLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        binding.layoutBtnGroup.btnClear.setOnClickListener {
            clearText()
        }
//
        viewModel.saveOutsideSample.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog("The sample was successfully taken.") {
                        resetForm()
                    }
                    viewModel.resetSaveOutSideSample()
//                    binding.layoutBtnGroup.btnAddToHandedList.isEnabled = true
//                    binding.layoutBtnGroup.btnAddToHandedList.setOnClickListener { view ->
//                        viewModel.addToHandedList(listOf(it.data!!))
//                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
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
    fun resetForm(){
        viewModel.selectedImgUri = null
        binding.ivOutside.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
        binding.edtStockName.setText("")
        binding.edtWeigh.setText("")
        binding.edtSpecification.setText("")
    }
}
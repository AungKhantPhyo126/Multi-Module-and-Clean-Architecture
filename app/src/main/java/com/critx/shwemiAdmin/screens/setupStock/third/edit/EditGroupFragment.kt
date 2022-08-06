package com.critx.shwemiAdmin.screens.setupStock.third.edit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentNewGroupBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

const val IMAGE_PICK_CODE = 100


@AndroidEntryPoint
class EditGroupFragment : Fragment() {
    private lateinit var binding: FragmentNewGroupBinding
    private val viewModel by viewModels<EditGroupViewModel>()
    private val args by navArgs<EditGroupFragmentArgs>()
    private var isFrequentlyUsed = 0
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage =
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
                    binding.ivGroupImage.setImageURI(selectedImageUri)
                }
            }

        }

        readStoragePermissionlauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) chooseImage()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNewGroupBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        isFrequentlyUsed = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        binding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, ischecked ->
            isFrequentlyUsed = if (ischecked) 1 else 0

        }
        binding.ivGroupImage.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()){
                chooseImage()
            }else{
                requestPermission()
            }
        }

        binding.btnAdd.setOnClickListener {
            uploadFile()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //createGroup
                launch {
                    viewModel.createJewelleryGroupState.collectLatest {
                        if (it.createGroupLoading){
                            loadingDialog.show()
                        }else loadingDialog.dismiss()
                        if (!it.createSuccessLoading.isNullOrEmpty()) {
                            requireContext().showSuccessDialog("Group Created") {
                                findNavController().popBackStack()
                            }
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



        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun uploadFile() {
        var photo: MultipartBody.Part? = null
        viewModel.selectedImgUri?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo = MultipartBody.Part.createFormData("image", it.name, requestBody)
        }
        val name = binding.edtGroupName.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModel.createGroup(
            photo!!,
            args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            isFrequentlyUsed.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            name

        )
    }

    fun chooseImage() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        launchChooseImage.launch(pickIntent)
    }

    fun getRealPathFromUri(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
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

    private fun isReadExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(){
        readStoragePermissionlauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
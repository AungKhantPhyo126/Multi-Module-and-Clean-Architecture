package com.critx.shwemiAdmin.screens.setupStock.third.edit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.*
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentNewGroupBinding
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseGroupViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


const val IMAGE_PICK_CODE = 100
const val CREATE_GROUP = "create group"
const val EDIT_GROUP = "edit group"
const val CREATEED_GROUP_ID = "created-group-id"


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

    private val sharedViewModel by activityViewModels<ChooseGroupViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedImgUri = File(path)
                    }
                    binding.ivGroupImage.loadImageWithGlideWithUri(data.data)
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
        checkEditOrAddnewAndBind()

        binding.ivRemove.setOnClickListener {
            viewModel.selectedImgUri = null
            binding.ivGroupImage.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
        }

        if (args.groupInfo != null) {
            binding.cbFrequentlyUsed.isChecked = args.groupInfo!!.isFrequentlyUse
            binding.btnAdd.text = "Save"
            binding.ivGroupImage.loadImageWithGlide(args.groupInfo!!.imageUrl)

        } else {
            binding.btnAdd.text = "Create & Select"
        }
        isFrequentlyUsed = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        binding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, ischecked ->
            isFrequentlyUsed = if (ischecked) 1 else 0
        }
        binding.ivGroupImage.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }

        binding.btnAdd.setOnClickListener {
            if (binding.btnAdd.text == "Create & Select") {
                uploadFile(CREATE_GROUP)
            } else {
                uploadFile(EDIT_GROUP)
            }

        }
        viewModel.editJewelleryGroupState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()) {
                        findNavController().popBackStack()
                    }
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
        viewModel.createJewelleryGroupState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog("Group Created") {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            CREATEED_GROUP_ID,
                            it.data
                        )
                        findNavController().popBackStack()
                    }
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

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun checkEditOrAddnewAndBind() {
        args.groupInfo?.let {
            binding.edtGroupName.setText(it.name)
        }
    }

    fun uploadFile(actionType: String) {

        if (binding.edtGroupName.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Fill required Fields", Toast.LENGTH_LONG)
        } else {
            val name = binding.edtGroupName.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            if (actionType == CREATE_GROUP) {
                var imageToUpload: MultipartBody.Part? = null
                var requestBody :RequestBody?= null
                viewModel.selectedImgUri?.let {
                    requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    imageToUpload = MultipartBody.Part.createFormData(
                        "image",
                        it.name,
                        requestBody!!
                    )
                }
                if (imageToUpload == null){
                    Toast.makeText(requireContext(),"Please select an image",Toast.LENGTH_LONG).show()
                }else{
                    viewModel.createGroup(
                        imageToUpload!!,
                        args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        isFrequentlyUsed.toString()
                            .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                        name
                    )
                }

            } else {
                var imageToUpload: MultipartBody.Part? = null
                var requestBody :RequestBody?= null
                viewModel.selectedImgUri?.let {
                    requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    imageToUpload = MultipartBody.Part.createFormData(
                        "image",
                        it.name,
                        requestBody!!
                    )
                }
                viewModel.editGroup(
                    imageToUpload,
                    args.groupInfo!!.id,
                    args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    isFrequentlyUsed.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    name
                )
            }
        }
    }

    fun chooseImage() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        launchChooseImage.launch(pickIntent)
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

fun getRealPathFromUri(context: Context, contentUri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(columnIndex)
    } finally {
        cursor?.close()
    }
}

fun persistImage(bitmap: Bitmap, name: String, context: Context): File {
    val filesDir: File = context.filesDir
    val imageFile = File(filesDir, "$name.jpg")
    val os: OutputStream
    try {
        os = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
    } catch (e: Exception) {
        Log.e("gg", "Error writing bitmap", e)
    }
    return imageFile
}
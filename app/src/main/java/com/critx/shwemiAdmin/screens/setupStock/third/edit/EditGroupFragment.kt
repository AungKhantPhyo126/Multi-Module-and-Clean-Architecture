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
import com.critx.common.ui.getBitMapWithGlide
import com.critx.common.ui.loadImageWithGlide
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentNewGroupBinding
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.convertBitmapToFile
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.getResizedBitmap
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseGroupViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
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
    var photo: MultipartBody.Part? = null

    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>

    private val sharedViewModel by activityViewModels<ChooseGroupViewModel>()


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
                        selectedImage = getResizedBitmap(
                            selectedImage!!,
                            500
                        );// 400 is for example, replace with desired size
                        binding.ivGroupImage.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.selectedImgUri = SelectedImage(file!!, selectedImage!!)
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



        if (args.groupInfo != null) {
            binding.cbFrequentlyUsed.isChecked = args.groupInfo!!.isFrequentlyUse
            binding.btnAdd.text = "Save"

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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //image original
                launch {
                    args.groupInfo?.imageUrl?.let {
                        withContext(Dispatchers.IO) {
                            val originalImage = getBitMapWithGlide(it, requireContext())
                            val fileName: String = it.substring(it.lastIndexOf('/') + 1)
                            val file = persistImage(originalImage, fileName, requireContext())
                            val requestBody =
                                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            photo =
                                MultipartBody.Part.createFormData("image", file.name, requestBody)
                        }
                    }
                }


                //editgroup
                launch {
                    viewModel.editJewelleryGroupState.collectLatest {
                        if (it.editGroupLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (!it.editSuccessLoading.isNullOrEmpty()) {
                            requireContext().showSuccessDialog("Group Updated") {
//                                sharedViewModel.selectedChooseGroupUIModel = ChooseGroupUIModel(

//                                )
                                findNavController().popBackStack()
                            }
                        }
                    }
                }

                //createGroup
                launch {
                    viewModel.createJewelleryGroupState.collectLatest {
                        if (it.createGroupLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.createSuccessLoading != null) {
                            requireContext().showSuccessDialog("Group Created") {
                                sharedViewModel.selectedChooseGroupUIModel = it.createSuccessLoading
                                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                    CREATEED_GROUP_ID,
                                    it.createSuccessLoading!!.id
                                )
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

    fun checkEditOrAddnewAndBind() {
        args.groupInfo?.let {
            binding.edtGroupName.setText(it.name)
            binding.ivGroupImage.loadImageWithGlide(it.imageUrl)
        }
    }

    fun uploadFile(actionType: String) {

        viewModel.selectedImgUri?.let {
            val requestBody = convertBitmapToFile(
                it.file.name,
                it.bitMap,
                requireContext()
            ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo = MultipartBody.Part.createFormData("image", it.file.name, requestBody)
        }

        val name = binding.edtGroupName.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (actionType == CREATE_GROUP) {
            viewModel.createGroup(
                photo!!,
                args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                isFrequentlyUsed.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                name
            )
        } else {

            viewModel.editGroup(
                photo!!,
                args.groupInfo!!.id,
                args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                isFrequentlyUsed.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                name
            )
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
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        cursor?.getString(column_index)
    } finally {
        if (cursor != null) {
            cursor.close()
        }
    }
}

private fun persistImage(bitmap: Bitmap, name: String, context: Context): File {
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
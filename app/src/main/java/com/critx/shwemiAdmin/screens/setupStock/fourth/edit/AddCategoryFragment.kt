package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.*
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentAddCategoryBinding
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.screens.setupStock.third.edit.persistImage
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
import java.io.*


const val CREATE_CATEGORY = "create category"
const val EDIT_CATEGORY = "edit category"
const val CREATED_CATEGORY_ID = "created-category-id"

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

    var photo1: MultipartBody.Part? = null
    var photo2: MultipartBody.Part? = null
    var photo3: MultipartBody.Part? = null
    var video: MultipartBody.Part? = null
    var selectedGif: MultipartBody.Part? = null
    var selectedRecommendCat: MutableList<Int> = mutableListOf()

    var image1: Bitmap? = null
    var image2: Bitmap? = null
    var image3: Bitmap? = null
    var gif: Bitmap? = null
    var file1: File? = null
    var file2: File? = null
    var file3: File? = null
    var fileGif: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.setSelectedImgUri1(null)
            viewModel.setSelectedImgUri2(null)
            viewModel.setSelectedImgUri3(null)
            viewModel.setSelectedGif(null)
            findNavController().popBackStack()
        }

        launchChooseImage1 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                var selectedImage: Bitmap?
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {
                        val imageStream: InputStream =
                            requireContext().contentResolver?.openInputStream(it)!!
                        selectedImage = BitmapFactory.decodeStream(imageStream)
                        selectedImage = getResizedBitmap(
                            selectedImage!!,
                            600
                        );// 400 is for example, replace with desired size
//                        binding.ivImage1.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.setSelectedImgUri1(SelectedImage(file!!, selectedImage!!))
                    }
                }

            }
        launchChooseImage2 =
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
//                        binding.ivImage2.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.setSelectedImgUri2(SelectedImage(file!!, selectedImage!!))
                    }
                }

            }
        launchChooseImage3 =
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
//                        binding.ivImage3.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.setSelectedImgUri3(SelectedImage(file!!, selectedImage!!))
                    }
                }

            }

        launchChooseVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {

                        getRealPathFromUri(requireContext(), it)?.let { it1 ->

                            viewModel.setSelectedVideo(
                                File(
                                    it1
                                )
                            )
                        }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
//                        binding.ivVideo.setVideoURI(selectedImageUri)
//                        binding.ivVideo.pause()
                        getRealVideoPathFromUri(
                            requireContext(),
                            result.data!!.data!!
                        ).let {
                            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                                File(it!!),
                                Size(500, 500), CancellationSignal()
                            )
//                            binding.ivVideo.setImageBitmap(thumbnail)
                        }
                    }
                }
            }

        launchChooseGif =
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
//                        binding.ivGif.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.setSelectedGif(SelectedImage(file!!, selectedImage!!))
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



        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<List<Int>>(
            "selected recommend categories"
        )
            ?.observe(viewLifecycleOwner) {
                viewModel.setSelectedRecommendCat(it)
            }

        if (args.category != null) {
            binding.cbFrequentlyUsed.isChecked = args.groupInfo!!.isFrequentlyUse
            binding.btnConfirm.text = "Save"
            binding.edtEnterCategory.setText(args.category!!.name)
            binding.edtAvgWeight.setText(args.category!!.avgWeightPerUnitGm.toString())
            binding.edtSpecification.setText(args.category!!.specification)
            val imageList = args.category!!.imageUrlList
//            if ( findNavController().previousBackStackEntry?.destination?.id == R.id.chooseCategoryFragment){
//                when(args.category!!.imageUrlList.size){
//
//                    1->{
//                        if (viewModel.selectedImgUri1.value == null){
//                            binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])
//
//                        }
//                    }
//                    2->{
//                        if(viewModel.selectedImgUri1.value == null) {
//
//                            binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])
//
//                        }
//                        if(viewModel.selectedImgUri2.value == null) {
//
//                            binding.ivImage2.loadImageWithGlide(args.category!!.imageUrlList[1])
//
//                        }
//
//                        }
//                    3->{
//                        if(viewModel.selectedImgUri1.value == null) {
//                            binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])
//                        }
//                        if(viewModel.selectedImgUri2.value == null) {
//
//                            binding.ivImage2.loadImageWithGlide(args.category!!.imageUrlList[1])
//                        }
//                        if(viewModel.selectedImgUri3.value == null) {
//
//                            binding.ivImage3.loadImageWithGlide(args.category!!.imageUrlList[2])
//                        }
//                    }
//                    4->{
//                        if(viewModel.selectedImgUri1.value == null) {
//
//                            binding.ivImage1.loadImageWithGlideReady(args.category!!.imageUrlList[0])
//
//                            val bm = getBitMapWithGlide(imageList[0],requireContext())
//                            setPhoto(imageList,0,binding.ivImage1,bm)
//                        }
//                        if(viewModel.selectedImgUri2.value == null) {
//                            binding.ivImage2.loadImageWithGlide(args.category!!.imageUrlList[1])
//
//                            val bm = binding.ivImage2.drawable.toBitmap()
//                            setPhoto(imageList,1,binding.ivImage2,bm)
//
//                        }
//                        if(viewModel.selectedImgUri3.value == null) {
//
//                            binding.ivImage3.loadImageWithGlide(args.category!!.imageUrlList[2])
//                            val bm = binding.ivImage3.drawable.toBitmap()
//                            setPhoto(imageList,2,binding.ivImage3,bm)
//
//                        }
//                        if(viewModel.selectedGifUri.value == null){
//
//                            binding.ivGif.loadImageWithGlide(args.category!!.imageUrlList[3])
//                            val bm = binding.ivGif.drawable.toBitmap()
//
//                            setPhoto(imageList,3,binding.ivGif,bm)
//
//                        }
////                    }
//                }
//
//            }
        } else {
            binding.btnConfirm.text = "Create & Select"
        }

        viewModel.selectedImgUri1?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivImage1.setImageBitmap(selectedItem.bitMap)
                val requestBody = convertBitmapToFile(
                    selectedItem.file.name,
                    selectedItem.bitMap,
                    requireContext()
                ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo1 = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }


        }
        viewModel.selectedImgUri2?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivImage2.setImageBitmap(selectedItem.bitMap)
                val requestBody = convertBitmapToFile(
                    selectedItem.file.name,
                    selectedItem.bitMap,
                    requireContext()
                ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo2 = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }
        }
        viewModel.selectedImgUri3?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivImage3.setImageBitmap(selectedItem.bitMap)
                val requestBody = convertBitmapToFile(
                    selectedItem.file.name,
                    selectedItem.bitMap,
                    requireContext()
                ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo3 = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }
        }

        viewModel.selectedGifUri?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivGif.setImageBitmap(selectedItem.bitMap)
                val requestBody = convertBitmapToFile(
                    selectedItem.file.name,
                    selectedItem.bitMap,
                    requireContext()
                ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                selectedGif = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }

        }
        viewModel.selectedVideoUri?.observe(viewLifecycleOwner) { selectedItem ->
            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                selectedItem,
                Size(500, 500), CancellationSignal()
            )
            binding.ivVideo.setImageBitmap(thumbnail)
            val requestBody = selectedItem.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            video = MultipartBody.Part.createFormData("video", selectedItem.name, requestBody)
        }
        viewModel.selectedRecommendCat.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                selectedRecommendCat.clear()
                selectedRecommendCat.addAll(result)
            }
        }


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
            if (binding.btnConfirm.text == "Create & Select") {
                uploadFile(CREATE_CATEGORY)
            } else {
                uploadFile(EDIT_CATEGORY)
            }
        }
        binding.mcvRecommendAhtal.setOnClickListener {
            findNavController().navigate(
                AddCategoryFragmentDirections.actionAddCategoryFragmentToRecommendStockFragment(
                    selectedRecommendCat.map { it.toString() }.toTypedArray(),
                    args.category?.id
                )
            )
        }
        binding.mcvChooseDesign.setOnClickListener {
            findNavController().navigate(AddCategoryFragmentDirections.actionAddCategoryFragmentToDesignListFragment())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    if (args.category != null) {
                        val imageList = args.category!!.imageUrlList
//            if ( findNavController().previousBackStackEntry?.destination?.id == R.id.chooseCategoryFragment){
                        when (args.category!!.imageUrlList.size) {

                            1 -> {
                                if (viewModel.selectedImgUri1.value == null) {
                                    binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])

                                }
                            }
                            2 -> {
                                if (viewModel.selectedImgUri1.value == null) {

                                    binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])

                                }
                                if (viewModel.selectedImgUri2.value == null) {

                                    binding.ivImage2.loadImageWithGlide(args.category!!.imageUrlList[1])

                                }

                            }
                            3 -> {
                                if (viewModel.selectedImgUri1.value == null) {
                                    binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])
                                }
                                if (viewModel.selectedImgUri2.value == null) {

                                    binding.ivImage2.loadImageWithGlide(args.category!!.imageUrlList[1])
                                }
                                if (viewModel.selectedImgUri3.value == null) {

                                    binding.ivImage3.loadImageWithGlide(args.category!!.imageUrlList[2])
                                }
                            }
                            4 -> {
                                if (viewModel.selectedImgUri1.value == null) {

                                    binding.ivImage1.loadImageWithGlide(args.category!!.imageUrlList[0])
                                    withContext(Dispatchers.IO) {
                                        val bm = getBitMapWithGlide(imageList[0], requireContext())
                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                }
                                if (viewModel.selectedImgUri2.value == null) {
                                    binding.ivImage2.loadImageWithGlide(args.category!!.imageUrlList[1])
                                    withContext(Dispatchers.IO) {
                                        val bm = getBitMapWithGlide(imageList[1], requireContext())
                                        setPhoto(imageList, 1, binding.ivImage2, bm)
                                    }
                                }
                                if (viewModel.selectedImgUri3.value == null) {
                                    binding.ivImage3.loadImageWithGlide(args.category!!.imageUrlList[2])
                                    withContext(Dispatchers.IO) {
                                        val bm = getBitMapWithGlide(imageList[2], requireContext())
                                        setPhoto(imageList, 2, binding.ivImage3, bm)
                                    }
                                }
                                if (viewModel.selectedGifUri.value == null) {

                                    binding.ivGif.loadImageWithGlide(args.category!!.imageUrlList[3])
                                    withContext(Dispatchers.IO) {
                                        val bm = getBitMapWithGlide(imageList[3], requireContext())
                                        setPhoto(imageList, 3, binding.ivGif, bm)
                                    }
                                }
//                    }
                            }

                        }
                    }
                }

                //createCategory
                launch {
                    viewModel.createJewelleryCategoryState.collectLatest {
                        if (it.createLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()

                        if (it.createSuccessLoading != null) {
                            requireContext().showSuccessDialog("Category Created") {
                                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                    CREATED_CATEGORY_ID,
                                    it.createSuccessLoading!!.id
                                )
                                it.createSuccessLoading= null
                                findNavController().popBackStack()
                            }
                        }
                    }
                }

                //editCategory
                launch {
                    viewModel.editJewelleryCategoryState.collectLatest {
                        if (it.editLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.editSuccessLoading != null) {
                            requireContext().showSuccessDialog("Category Updated") {
                                it.editSuccessLoading = null
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
        pickIntent.type = "image/*"
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

    fun uploadFile(actionType: String) {


        if (binding.edtEnterCategory.text.isNullOrEmpty() ||
            binding.edtSpecification.text.isNullOrEmpty() ||
            binding.edtAvgWeight.text.isNullOrEmpty() ||
            binding.edtK.text.isNullOrEmpty() ||
            binding.edtP.text.isNullOrEmpty() ||
            binding.edtY.text.isNullOrEmpty() ||
            viewModel.selectedDesignIds == null ||
            viewModel.selectedRecommendCat == null
        ) {
            Toast.makeText(requireContext(),"Fill The Required Fields",Toast.LENGTH_LONG).show()
        } else {
            val photoList = mutableListOf<MultipartBody.Part?>(photo1, photo2, photo3, selectedGif)
            val photoToUpload = photoList.filterNotNull() as MutableList

//        val videoList = mutableListOf(video!!)
            val designList = mutableListOf<RequestBody>()
            viewModel.selectedDesignIds?.let { list ->
                list.forEach {
                    designList.add(
                        it.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                }
            }

            val name = binding.edtEnterCategory.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val specification = binding.edtSpecification.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val avgWeigh = binding.edtAvgWeight.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val avgKyat = binding.edtK.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val avgPae = binding.edtP.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val avgYwae = binding.edtY.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val recommendCat = mutableListOf<RequestBody>()
            selectedRecommendCat.let { list ->
                list.forEach {
                    recommendCat.add(
                        it.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                }
            }

            if (actionType == CREATE_CATEGORY) {
                viewModel.createJewelleryCategory(
                    args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.groupInfo.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    isFrequentlyUsed.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    name,
                    avgWeigh,
                    photoToUpload,
                    video!!,
                    specification,
                    designList,
                    orderToGs.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    avgKyat,
                    avgPae,
                    avgYwae,
                    recommendCat
                )
            } else {
                viewModel.editJewelleryCategory(
                    args.category!!.id,
                    args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.groupInfo.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    isFrequentlyUsed.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    name,
                    avgWeigh,
                    photoToUpload,
                    video!!,
                    specification,
                    designList,
                    orderToGs.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    avgKyat,
                    avgPae,
                    avgYwae,
                    recommendCat
                )
            }

        }
    }

    private fun setPhoto(imageList: List<String>, index: Int, imageView: ImageView, bm: Bitmap) {
        val fileName: String =
            imageList[index].substring(imageList[index].lastIndexOf('/') + 1)
        val resizedBitmap = getResizedBitmap(bm, 600)
        val file = persistImage(resizedBitmap!!, fileName, requireContext())
        val requestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        when (index) {
            0 -> {
                photo1 = MultipartBody.Part.createFormData("images[]", file.name, requestBody)
            }
            1 -> {
                photo2 = MultipartBody.Part.createFormData("images[]", file.name, requestBody)
            }
            2 -> {
                photo3 = MultipartBody.Part.createFormData("images[]", file.name, requestBody)
            }
            3 -> {
                selectedGif = MultipartBody.Part.createFormData("images[]", file.name, requestBody)
            }
        }

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
        cursor?.close()
    }

}

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
    var width = 600
    var height = 600
//    width = (width*Resources.getSystem().displayMetrics.density).toInt()
//    height = (height*Resources.getSystem().displayMetrics.density).toInt()
//    val bitmapRatio = width.toFloat() / height.toFloat()
//    if (bitmapRatio > 1) {
//        width = maxSize
//        height = (width / bitmapRatio).toInt()
//    } else {
//        height = maxSize
//        width = (height * bitmapRatio).toInt()
//    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun convertBitmapToFile(fileName: String, bitmap: Bitmap, context: Context): File {
    //create a file to write bitmap data
    val file = File(context.cacheDir, fileName)
    file.createNewFile()

    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
    val bitMapData = bos.toByteArray()

    //write the bytes in file
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}

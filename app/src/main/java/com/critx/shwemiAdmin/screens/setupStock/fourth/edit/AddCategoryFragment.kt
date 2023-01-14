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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.critx.common.ui.*
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.DialogChooseDesignBinding
import com.critx.shwemiAdmin.databinding.DialogChooseJewelleryTypeBinding
import com.critx.shwemiAdmin.databinding.FragmentAddCategoryBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.ChooseCategoryFragmentDirections
import com.critx.shwemiAdmin.screens.setupStock.fourth.JewelleryCategoryRecyclerAdapter
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseGroupFragmentDirections
import com.critx.shwemiAdmin.screens.setupStock.third.ImageRecyclerAdapter
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.screens.setupStock.third.edit.persistImage
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private lateinit var adapter: ImageRecyclerAdapter
    private lateinit var categoryAdapter: JewelleryCategoryRecyclerAdapter

    private var isFrequentlyUsed = 0
    private var orderToGs = 0
    private var withGem = 0
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var launchChooseImage1: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage2: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage3: ActivityResultLauncher<Intent>
    private lateinit var launchChooseVideo: ActivityResultLauncher<Intent>
    private lateinit var launchChooseGif: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>

//    var selectedRecommendCat: MutableList<Int> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.selectedImgUri1 = null
            viewModel.selectedImgUri2 = null
            viewModel.selectedImgUri3 = null
            viewModel.selectedGifUri = null
            viewModel.selectedVideoUri = null
            viewModel.resetLiveDataForBackPress()
            findNavController().popBackStack()
        }

        launchChooseImage1 = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedImgUri1 = File(path)
                    }
                    binding.ivImage1.setImageURI(data.data)
                }
            }
        }
        launchChooseImage2 = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedImgUri2 = File(path)
                    }
                    binding.ivImage2.setImageURI(data.data)
                }
            }
        }
        launchChooseImage3 = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedImgUri3 = File(path)
                    }
                    binding.ivImage3.setImageURI(data.data)

                }
            }
        }
        launchChooseGif = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedGifUri = File(path)
                    }
                    binding.ivGif.setImageURI(data.data)
                }
            }
        }
        launchChooseVideo = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedVideoUri = File(path)
                        binding.ivVideo.getThumbnail(path)
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
        binding.ibBack.setOnClickListener {
            viewModel.selectedImgUri1 = null
            viewModel.selectedImgUri2 = null
            viewModel.selectedImgUri3 = null
            viewModel.selectedGifUri = null
            viewModel.selectedVideoUri = null
            viewModel.resetLiveDataForBackPress()
            findNavController().popBackStack()
        }
        binding.includeRecommendCategory.btnAdd.setOnClickListener {
            showChooseRelatedCategoryDialog()
        }
        binding.btnConfirm.setOnClickListener {

        }
        loadingDialog = requireContext().getAlertDialog()
        isFrequentlyUsed = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        binding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, ischecked ->
            isFrequentlyUsed = if (ischecked) 1 else 0

        }
        orderToGs = if (binding.cbOrderToGs.isChecked) 1 else 0
        binding.cbOrderToGs.setOnCheckedChangeListener { compoundButton, ischecked ->
            orderToGs = if (ischecked) 1 else 0
        }

        withGem = if (binding.cbHasGem.isChecked) 1 else 0
        binding.cbHasGem.setOnCheckedChangeListener { compoundButton, ischecked ->
            withGem = if (ischecked) 1 else 0
        }


//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<List<Int>>(
//            "selected recommend categories"
//        )
//            ?.observe(viewLifecycleOwner) {
//                viewModel.setSelectedRecommendCat(it)
//            }

        if (args.category != null) {
            viewModel.getRelatedCat(args.category!!.id)
            viewModel.addDesignWithList(args.category!!.designsList.toMutableList())
            binding.cbFrequentlyUsed.isChecked = args.category!!.isFrequentlyUse
            binding.cbHasGem.isChecked = args.category!!.withGem
            binding.cbOrderToGs.isChecked = args.category!!.orderToGs
            binding.btnConfirm.text = "Save"
            binding.edtEnterCategory.setText(args.category!!.name)
            binding.edtAvgWeight.setText(args.category!!.avgWeightPerUnitGm.toString())
            binding.edtSpecification.setText(args.category!!.specification)
            binding.edtK.setText(args.category!!.avgKPYUiModel.kyat.toString())
            binding.edtP.setText(args.category!!.avgKPYUiModel.pae.toString())
            binding.edtY.setText(args.category!!.avgKPYUiModel.ywae.toString())
            val imageList = args.category!!.imageUrlList
            repeat(imageList.size) {
                if (imageList[it].endsWith(".gif")) {
                    binding.ivGif.loadImageWithGlide(imageList[it])
                    setOriginalImage(imageList[it], 3)
                } else {
                    setOriginalImage(imageList[it], it)
                }
            }
            if (args.category!!.video.isNullOrEmpty().not()) {
                args.category!!.video?.let { binding.ivVideo.getThumbnail(it) }
            }

        } else {
            binding.btnConfirm.text = "Create & Select"
        }
        viewModel.designInCatLiveDataLiveData.observe(viewLifecycleOwner) {
            binding.includeChooseDesign.chipGroup.removeAllViews()
            for (item in it) {
                val chip = requireContext().createChip(item.name)
                chip.id = item.id.toInt()
                chip.isCheckable = false
                chip.isCloseIconVisible = true
                chip.setOnCloseIconClickListener {
                    viewModel.removeDesignByItem(item)
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        "Design Removed",
                        Snackbar.LENGTH_LONG
                    ).setAction("Undo") {
                        viewModel.addDesignByItem(item)
                    }
                    snackBar?.show()
                }
                binding.includeChooseDesign.chipGroup.addView(chip)
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
        binding.ivRemove1.setOnClickListener {
            binding.ivImage1.setImageDrawable(requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_picture))
            viewModel.selectedImgUri1 = null
        }
        binding.ivRemove2.setOnClickListener {
            binding.ivImage2.setImageDrawable(requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_picture))
            viewModel.selectedImgUri2 = null

        }
        binding.ivRemove3.setOnClickListener {
            binding.ivImage3.setImageDrawable(requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_picture))
            viewModel.selectedImgUri3 = null

        }
        binding.ivRemoveGif.setOnClickListener {
            binding.ivGif.setImageDrawable(requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_gif))
            viewModel.selectedGifUri = null

        }
        binding.ivRemoveVideo.setOnClickListener {
            binding.ivVideo.setImageDrawable(requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_video))
            viewModel.selectedVideoUri = null

        }

        binding.btnConfirm.setOnClickListener {
            if (binding.btnConfirm.text == "Create & Select") {
                uploadFile(CREATE_CATEGORY)
            } else {
                uploadFile(EDIT_CATEGORY)
            }

        }
        val relatedCatRecyclerAdapter = RelatedCatRecyclerAdapter { item ->
            viewModel.removeRelatedCat(item)
            snackBar?.dismiss()
            snackBar = Snackbar.make(
                binding.root,
                "Related Category Removed",
                Snackbar.LENGTH_LONG
            ).setAction("Undo") {
                viewModel.addRelatedCat(item)
            }
            snackBar?.show()
        }
        binding.includeRecommendCategory.rvCatItems.adapter = relatedCatRecyclerAdapter
        viewModel.getRelatedCats.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()

                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    relatedCatRecyclerAdapter.submitList(it.data)
                    relatedCatRecyclerAdapter.notifyDataSetChanged()
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

        binding.includeChooseDesign.btnAdd.setOnClickListener {
            showChooseDesignListDialog()
        }

        viewModel.createJewelleryCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog("Category Created") {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            CREATED_CATEGORY_ID,
                            it.data
                        )
                        viewModel.selectedImgUri1 = null
                        viewModel.selectedImgUri2 = null
                        viewModel.selectedImgUri3 = null
                        viewModel.selectedGifUri = null
                        viewModel.selectedVideoUri = null
                        viewModel.resetCreateLiveData()
                        viewModel.resetLiveDataForBackPress()
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
        viewModel.editJewelleryCategoryState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog("Category Updated") {
                        viewModel.selectedImgUri1 = null
                        viewModel.selectedImgUri2 = null
                        viewModel.selectedImgUri3 = null
                        viewModel.selectedGifUri = null
                        viewModel.selectedVideoUri = null
                        viewModel.resetEditLiveData()
                        viewModel.resetLiveDataForBackPress()
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
    }

    fun setOriginalImage(imageUrl: String, placeHolder: Int) {
        var bm: Bitmap?
        when (placeHolder) {
            0 -> {
                binding.ivImage1.loadImageWithGlide(imageUrl)
            }
            1 -> {
                binding.ivImage2.loadImageWithGlide(imageUrl)
            }
            2 -> {
                binding.ivImage3.loadImageWithGlide(imageUrl)
            }


        }
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                bm = getBitMapWithGlide(imageUrl, requireContext())
                val fileName: String =
                    imageUrl.substring(imageUrl.lastIndexOf('/') + 1)
                when (placeHolder) {
                    0 -> {
                        viewModel.selectedImgUri1 = persistImage(
                            bm!!,
                            fileName,
                            requireContext()
                        )
                    }
                    1 -> {
                        viewModel.selectedImgUri2 = persistImage(
                            bm!!,
                            fileName,
                            requireContext()
                        )
                    }
                    2 -> {
                        viewModel.selectedImgUri3 = persistImage(
                            bm!!,
                            fileName,
                            requireContext()
                        )
                    }
                    3 -> {
                        viewModel.selectedGifUri = persistImage(
                            bm!!,
                            fileName,
                            requireContext()
                        )
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
            binding.edtY.text.isNullOrEmpty()

        ) {
            Toast.makeText(requireContext(), "Fill The Required Fields", Toast.LENGTH_LONG).show()
        } else {
            var photo1: MultipartBody.Part? = null
            var requestBody1: RequestBody? = null
            viewModel.selectedImgUri1?.let {
                requestBody1 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo1 = MultipartBody.Part.createFormData(
                    "images[]",
                    it.name,
                    requestBody1!!
                )
            }

            var photo2: MultipartBody.Part? = null
            var requestBody2: RequestBody? = null
            viewModel.selectedImgUri2?.let {
                requestBody2 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo2 = MultipartBody.Part.createFormData(
                    "images[]",
                    it.name,
                    requestBody2!!
                )
            }

            var photo3: MultipartBody.Part? = null
            var requestBody3: RequestBody? = null
            viewModel.selectedImgUri3?.let {
                requestBody3 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo3 = MultipartBody.Part.createFormData(
                    "images[]",
                    it.name,
                    requestBody3!!
                )
            }

            var selectedGif: MultipartBody.Part? = null
            var requestBody4: RequestBody? = null
            viewModel.selectedGifUri?.let {
                requestBody4 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                selectedGif = MultipartBody.Part.createFormData(
                    "images[]",
                    it.name,
                    requestBody4!!
                )
            }

            var video: MultipartBody.Part? = null
            var requestBody5: RequestBody? = null
            viewModel.selectedVideoUri?.let {
                requestBody5 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                video = MultipartBody.Part.createFormData(
                    "video",
                    it.name,
                    requestBody5!!
                )
            }


            val photoList = mutableListOf<MultipartBody.Part?>(photo1, photo2, photo3, selectedGif)
            val photoToUpload = photoList.filterNotNull() as MutableList

//        val videoList = mutableListOf(video!!)
            val designList = mutableListOf<RequestBody>()
            viewModel.designInCatLiveDataLiveData.value?.let { list ->
                list.forEach {
                    designList.add(
                        it.id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
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
            viewModel.getRelatedCats.value!!.data?.forEach { item ->
                recommendCat.add(
                    item.id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            }


            if (actionType == CREATE_CATEGORY) {
                viewModel.createJewelleryCategory(
                    args.type.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.quality.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    args.groupInfo.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    isFrequentlyUsed.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    withGem.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    name,
                    avgWeigh,
                    photoToUpload,
                    video,
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
                    withGem.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    name,
                    avgWeigh,
                    photoToUpload,
                    video,
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

    fun showChooseRelatedCategoryDialog() {

        var firstCatId = ""
        var secondCatId = ""
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater: LayoutInflater = LayoutInflater.from(builder.context)
        val relatedCatDialogBinding = DialogChooseJewelleryTypeBinding.inflate(
            inflater, ConstraintLayout(builder.context), false
        )
        builder.setView(relatedCatDialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)

        var frequentUse = 0
        relatedCatDialogBinding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, isChecked ->
            frequentUse = if (isChecked) 1 else 0
        }

        /** choose jewellery type */
        viewModel.getJewelleryType()
        viewModel.jewelleryTypeState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    relatedCatDialogBinding.chipGroup.removeAllViews()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        chip.setOnClickListener {
                            firstCatId = chip.id.toString()
                            relatedCatDialogBinding.tvFirstCat.text = item.name
                            relatedCatDialogBinding.ivFirst.isVisible = true
                            viewModel.getJewelleryQuality()
                        }
                        relatedCatDialogBinding.chipGroup.addView(chip)
                    }

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        relatedCatDialogBinding.root,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }

        /** choose Jewellery Quality*/
        viewModel.jewelleryQualityLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    relatedCatDialogBinding.chipGroup.removeAllViews()
                    relatedCatDialogBinding.ivBack.isVisible = true
                    relatedCatDialogBinding.ivBack.setOnClickListener {
                        relatedCatDialogBinding.ivSecond.isVisible = false
                        relatedCatDialogBinding.tvSecondCat.isVisible = false
                        viewModel.getJewelleryType()
                    }
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        relatedCatDialogBinding.chipGroup.addView(chip)
                        if (item.name == "18K" || item.name == "စိန်ထည်" || item.name == "အခေါက်ထည်") {
                            chip.setOnClickListener {
                                Toast.makeText(requireContext(), "Choose other", Toast.LENGTH_LONG)
                                    .show()
                            }
                        } else {
                            chip.setOnClickListener {
                                relatedCatDialogBinding.tvSecondCat.text = item.name
                                relatedCatDialogBinding.ivSecond.isVisible = true
                                relatedCatDialogBinding.tvSecondCat.isVisible = true

                                secondCatId = item.id
                                viewModel.getJewelleryGroup(
                                    frequentUse,
                                    firstCatId.toInt(),
                                    item.id.toInt()
                                )
                                secondCatId = chip.id.toString()
                            }
                        }

                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        relatedCatDialogBinding.root,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }
        /** choose Jewellery Group*/

        viewModel.getGroupLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    adapter = ImageRecyclerAdapter({
                        //deleteClick

                    },
                        {
                            relatedCatDialogBinding.tvThirdCat.text = it.name
                            relatedCatDialogBinding.ivThirdCat.isVisible = true
                            relatedCatDialogBinding.tvThirdCat.isVisible = true

                            viewModel.getJewelleryCategory(
                                frequentUse,
                                firstCatId.toInt(),
                                it.id.toInt(),
                                it.id.toInt()
                            )

                        }, {
                            //addNewClick

                        }, {
                            //navigateToEditClick

                        }, {
                            //eye click

                        },
                        false
                    )
                    relatedCatDialogBinding.rvImages.adapter = adapter
                    adapter.submitList(it.data)
                    relatedCatDialogBinding.chipGroup.removeAllViews()
                    relatedCatDialogBinding.cbImageView.setOnCheckedChangeListener { compoundButton, isChecked ->
                        if (isChecked) {
                            relatedCatDialogBinding.rvImages.visibility = View.VISIBLE
                            relatedCatDialogBinding.chipGroup.visibility = View.INVISIBLE
                        } else {
                            relatedCatDialogBinding.rvImages.visibility = View.INVISIBLE
                            relatedCatDialogBinding.chipGroup.visibility = View.VISIBLE
                        }
                    }
                    relatedCatDialogBinding.ivBack.isVisible = true
                    relatedCatDialogBinding.cbFrequentlyUsed.isVisible = true
                    relatedCatDialogBinding.cbImageView.isVisible = true
                    relatedCatDialogBinding.ivBack.setOnClickListener {
                        relatedCatDialogBinding.ivThirdCat.isVisible = false
                        relatedCatDialogBinding.tvThirdCat.isVisible = false
                        relatedCatDialogBinding.cbFrequentlyUsed.isVisible = false
                        relatedCatDialogBinding.cbImageView.isVisible = false
                        viewModel.getJewelleryQuality()
                    }
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        relatedCatDialogBinding.chipGroup.addView(chip)
                        chip.setOnClickListener {
                            relatedCatDialogBinding.tvThirdCat.text = item.name
                            relatedCatDialogBinding.ivThirdCat.isVisible = true
                            relatedCatDialogBinding.tvThirdCat.isVisible = true

                            viewModel.getJewelleryCategory(
                                frequentUse,
                                firstCatId.toInt(),
                                secondCatId.toInt(),
                                item.id.toInt()
                            )

                        }


                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        relatedCatDialogBinding.root,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }

        /** choose Jewellery Category*/

        viewModel.getJewelleryCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    categoryAdapter = JewelleryCategoryRecyclerAdapter({

                    }, {

                    }, {item->
                        relatedCatDialogBinding.tvFourthCat.text = item.name
                        relatedCatDialogBinding.ivFourthCat.isVisible = true
                        relatedCatDialogBinding.tvFourthCat.isVisible = true
                        if (viewModel.getRelatedCats.value!!.data!!.contains(item)){
                            Toast.makeText(requireContext(),"This cat is already added",Toast.LENGTH_LONG).show()
                        }else{
                            viewModel.addRelatedCat(item)
                            alertDialog.dismiss()
                        }
                        //To Do
                    }, {
                        //deleteclick

                    }, {
                        //eye click

                    }, false)
                    relatedCatDialogBinding.rvImages.adapter = categoryAdapter
                    categoryAdapter.submitList(it.data)
                    relatedCatDialogBinding.chipGroup.removeAllViews()
                    relatedCatDialogBinding.cbImageView.setOnCheckedChangeListener { compoundButton, isChecked ->
                        if (isChecked) {
                            relatedCatDialogBinding.rvImages.visibility = View.VISIBLE
                            relatedCatDialogBinding.chipGroup.visibility = View.INVISIBLE
                        } else {
                            relatedCatDialogBinding.rvImages.visibility = View.INVISIBLE
                            relatedCatDialogBinding.chipGroup.visibility = View.VISIBLE
                        }
                    }
                    relatedCatDialogBinding.ivBack.isVisible = true
                    relatedCatDialogBinding.cbFrequentlyUsed.isVisible = true
                    relatedCatDialogBinding.cbImageView.isVisible = true
                    relatedCatDialogBinding.ivBack.setOnClickListener {
                        relatedCatDialogBinding.ivFourthCat.isVisible = false
                        relatedCatDialogBinding.tvFourthCat.isVisible = false
                        viewModel.getJewelleryGroup(
                            isFrequentlyUsed,
                            firstCatId.toInt(),
                            secondCatId.toInt()
                        )

                    }
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        relatedCatDialogBinding.chipGroup.addView(chip)
                        chip.setOnClickListener {
                            relatedCatDialogBinding.tvFourthCat.text = item.name
                            relatedCatDialogBinding.ivFourthCat.isVisible = true
                            relatedCatDialogBinding.tvFourthCat.isVisible = true
                            if (viewModel.getRelatedCats.value!!.data!!.contains(item)){
                                Toast.makeText(requireContext(),"This cat is already added",Toast.LENGTH_LONG).show()
                            }else{
                                viewModel.addRelatedCat(item)
                                alertDialog.dismiss()
                            }
                            //To Do
                        }


                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        relatedCatDialogBinding.root,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }


        relatedCatDialogBinding.ivExit.setOnClickListener {
            relatedCatDialogBinding.cbFrequentlyUsed.isVisible = false
            relatedCatDialogBinding.cbImageView.isVisible = false
            viewModel.resetLiveData()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun showChooseDesignListDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater: LayoutInflater = LayoutInflater.from(builder.context)
        val designBinding = DialogChooseDesignBinding.inflate(
            inflater, ConstraintLayout(builder.context), false
        )
        builder.setView(designBinding.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        viewModel.getDesign(args.type.id)
        designBinding.ivExit.setOnClickListener {
            alertDialog.dismiss()
        }
        viewModel.getDesignLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        designBinding.chipGroup.addView(chip)
                        chip.setOnClickListener {
                            if (viewModel.designInCatLiveDataLiveData.value!!.contains(item)) {
                                Toast.makeText(
                                    requireContext(),
                                    "This Design is Already Chosen",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                viewModel.addDesignByItem(item)
                                alertDialog.dismiss()
                            }

                        }
                    }
                    alertDialog.show()
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

}

//fun getRealVideoPathFromUri(context: Context, contentUri: Uri): String? {
//    var cursor: Cursor? = null
//    return try {
//        val proj = arrayOf(MediaStore.Video.Media.DATA)
//        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
//        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor?.moveToFirst()
//        cursor?.getString(column_index)
//    } finally {
//        cursor?.close()
//    }
//
//}


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

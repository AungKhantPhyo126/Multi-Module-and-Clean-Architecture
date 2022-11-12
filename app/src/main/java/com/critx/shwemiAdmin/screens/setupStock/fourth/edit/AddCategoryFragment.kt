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
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.*
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentAddCategoryBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.screens.setupStock.third.edit.persistImage
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
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
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val args by navArgs<AddCategoryFragmentArgs>()

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

    var photo1: MultipartBody.Part? = null
    var photo2: MultipartBody.Part? = null
    var photo3: MultipartBody.Part? = null
    var video: MultipartBody.Part? = null
    var selectedGif: MultipartBody.Part? = null
//    var selectedRecommendCat: MutableList<Int> = mutableListOf()

    var image1: Bitmap? = null
    var image2: Bitmap? = null
    var image3: Bitmap? = null
    var gif: Bitmap? = null
    var file1: File? = null
    var file2: File? = null
    var file3: File? = null
    var fileGif: File? = null
    var fileVideo: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.setSelectedImgUri1(null)
            viewModel.setSelectedImgUri2(null)
            viewModel.setSelectedImgUri3(null)
            viewModel.setSelectedGif(null)
            viewModel.setSelectedVideo(null)
            viewModel.selectedDesignIds = null
            sharedViewModel.resetRecommendCat()
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
//                    if (result.data != null && result?.data?.data != null) {
//                        val selectedImageUri: Uri? = result.data!!.data
////                        binding.ivVideo.setVideoURI(selectedImageUri)
////                        binding.ivVideo.pause()
//                        getRealVideoPathFromUri(
//                            requireContext(),
//                            result.data!!.data!!
//                        ).let {
//                            val thumbnail = ThumbnailUtils.createVideoThumbnail(
//                                File(it!!),
//                                Size(500, 500), CancellationSignal()
//                            )
////                            binding.ivVideo.setImageBitmap(thumbnail)
//                        }
//                    }
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
            if (viewModel.selectedDesignIds == null) {
                viewModel.selectedDesignIds = args.category!!.designsList as MutableList<Int>
            }
        } else {
            binding.btnConfirm.text = "Create & Select"
        }

        if (viewModel.selectedDesignIds != null) {
            binding.tvChooseDesign.setTextColor(requireContext().getColorStateList(com.critx.shwemiAdmin.R.color.primary_color))
        } else {
            binding.tvChooseDesign.setTextColor(requireContext().getColorStateList(com.critx.shwemiAdmin.R.color.edit_text_color))
        }



        viewModel.selectedImgUri1?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivImage1.setImageBitmap(selectedItem.bitMap)
                val requestBody = it.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo1 = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }
            if (selectedItem == null) {
                photo1 = null
                binding.ivImage1.setImageResource(com.critx.shwemiAdmin.R.drawable.empty_picture)
            }
            binding.ivRemove1.isVisible = selectedItem != null


        }
        viewModel.selectedImgUri2?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivImage2.setImageBitmap(selectedItem.bitMap)
                              val requestBody = it.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                photo2 = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }
            if (selectedItem == null) {
                photo2 = null
                binding.ivImage2.setImageResource(com.critx.shwemiAdmin.R.drawable.empty_picture)
            }

            binding.ivRemove2.isVisible = selectedItem != null

        }
        viewModel.selectedImgUri3?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivImage3.setImageBitmap(selectedItem.bitMap)
                              val requestBody = it.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                photo3 = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }
            if (selectedItem == null) {
                photo3 = null
                binding.ivImage3.setImageResource(com.critx.shwemiAdmin.R.drawable.empty_picture)
            }

            binding.ivRemove3.isVisible = selectedItem != null

        }

        viewModel.selectedGifUri?.observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                binding.ivGif.setImageBitmap(selectedItem.bitMap)
                              val requestBody = it.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                selectedGif = MultipartBody.Part.createFormData(
                    "images[]",
                    selectedItem.file.name,
                    requestBody
                )
            }
            if (selectedItem == null) {
                selectedGif = null
                binding.ivGif.setImageResource(com.critx.shwemiAdmin.R.drawable.empty_gif)
            }
            binding.ivRemoveGif.isVisible = selectedItem != null

        }
        viewModel.selectedVideoUri?.observe(viewLifecycleOwner) { selectedItem ->
            binding.ivRemoveVideo.isVisible = selectedItem != null
//            binding.ivRemoveVideo.isVisible = binding.ivVideo.drawable != requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_picture)
            if (selectedItem != null) {
//                val thumbnail = ThumbnailUtils.createVideoThumbnail(
//                    selectedItem,
//                    Size(500, 500), CancellationSignal()
//                )
                binding.ivVideo.getThumbnail(selectedItem.path)
                val requestBody =
                    selectedItem.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                video = MultipartBody.Part.createFormData("video", selectedItem.name, requestBody)
            } else {
                video = null
                binding.ivVideo.setImageDrawable(requireContext().getDrawable(com.critx.shwemiAdmin.R.drawable.empty_video))
            }

        }
        sharedViewModel.recommendCatList.observe(viewLifecycleOwner) { result ->
            if (result.filterNotNull().isNotEmpty()) {
//                selectedRecommendCat.clear()
//                selectedRecommendCat.addAll(result)
                binding.tvRecommendCat.setTextColor(requireContext().getColorStateList(com.critx.shwemiAdmin.R.color.primary_color))
            } else {
                binding.tvRecommendCat.setTextColor(requireContext().getColorStateList(com.critx.shwemiAdmin.R.color.edit_text_color))
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
            viewModel.setSelectedImgUri1(null)
        }
        binding.ivRemove2.setOnClickListener {
            viewModel.setSelectedImgUri2(null)
        }
        binding.ivRemove3.setOnClickListener {
            viewModel.setSelectedImgUri3(null)
        }
        binding.ivRemoveGif.setOnClickListener {
            viewModel.setSelectedGif(null)
        }
        binding.ivRemoveVideo.setOnClickListener {
            viewModel.setSelectedVideo(null)
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
                AddCategoryFragmentDirections.actionAddCategoryFragmentToRecommendStockFragment(args.category)
            )
        }
        binding.mcvChooseDesign.setOnClickListener {
            findNavController().navigate(
                AddCategoryFragmentDirections.actionAddCategoryFragmentToDesignListFragment(
                    args.type.id
                )
            )
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
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[0], requireContext())
                                        val fileName: String =
                                            imageList[0].substring(imageList[0].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file1 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri1(SelectedImage(file1!!, bm!!))
                                }
                            }
                            2 -> {
                                if (viewModel.selectedImgUri1.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[0], requireContext())
                                        val fileName: String =
                                            imageList[0].substring(imageList[0].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file1 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri1(SelectedImage(file1!!, bm!!))
                                }
                                if (viewModel.selectedImgUri2.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[1], requireContext())
                                        val fileName: String =
                                            imageList[1].substring(imageList[1].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file2 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri2(SelectedImage(file2!!, bm!!))
                                }

                            }
                            3 -> {
                                if (viewModel.selectedImgUri1.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[0], requireContext())
                                        val fileName: String =
                                            imageList[0].substring(imageList[0].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file1 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri1(SelectedImage(file1!!, bm!!))
                                }
                                if (viewModel.selectedImgUri2.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[1], requireContext())
                                        val fileName: String =
                                            imageList[1].substring(imageList[1].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file2 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri2(SelectedImage(file2!!, bm!!))
                                }
                                if (viewModel.selectedImgUri3.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[2], requireContext())
                                        val fileName: String =
                                            imageList[2].substring(imageList[2].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file3 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri3(SelectedImage(file3!!, bm!!))
                                }
                            }
                            4 -> {
                                if (viewModel.selectedImgUri1.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[0], requireContext())
                                        val fileName: String =
                                            imageList[0].substring(imageList[0].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file1 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri1(SelectedImage(file1!!, bm!!))
                                }
                                if (viewModel.selectedImgUri2.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[1], requireContext())
                                        val fileName: String =
                                            imageList[1].substring(imageList[1].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file2 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri2(SelectedImage(file2!!, bm!!))
                                }
                                if (viewModel.selectedImgUri3.value == null) {
                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[2], requireContext())
                                        val fileName: String =
                                            imageList[2].substring(imageList[2].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        file3 = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedImgUri3(SelectedImage(file3!!, bm!!))
                                }
                                if (viewModel.selectedGifUri.value == null) {

                                    var bm: Bitmap?
                                    withContext(Dispatchers.IO) {
                                        bm = getBitMapWithGlide(imageList[3], requireContext())
                                        val fileName: String =
                                            imageList[3].substring(imageList[3].lastIndexOf('/') + 1)
                                        val resizedBitmap = getResizedBitmap(bm!!, 600)
                                        fileGif = persistImage(
                                            resizedBitmap!!,
                                            fileName,
                                            requireContext()
                                        )
//                                        setPhoto(imageList, 0, binding.ivImage1, bm)
                                    }
                                    viewModel.setSelectedGif(SelectedImage(fileGif!!, bm!!))
                                }
//                    }
                            }

                        }
                        if (args.category?.video != null && viewModel.selectedVideoUri.value == null) {
                            binding.ivVideo.getThumbnail(args.category!!.video!!)
                        }
                    }
                }

                //getRelatedCats
                launch {
                    viewModel.getRelatedCats.collectLatest {
                        if (it.getRelatedCatsLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()

                        if (it.getRelatedCatsSuccessLoading != null) {
//                            viewModel.setSelectedRecommendCat(it.getRelatedCatsSuccessLoading!!.map { it.id.toInt() })
                            if(sharedViewModel.hasRemoveRecord.not()){
                                sharedViewModel.addRecommendCatBatch(it.getRelatedCatsSuccessLoading!!)
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
                                    it.createSuccessLoading
                                )
                                sharedViewModel.resetForRecommendCat()
                                it.createSuccessLoading = null
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
                                sharedViewModel.resetForRecommendCat()
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
            viewModel.selectedDesignIds.isNullOrEmpty()
        ) {
            Toast.makeText(requireContext(), "Fill The Required Fields", Toast.LENGTH_LONG).show()
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
            sharedViewModel.recommendCatList.value?.filterNotNull()?.map { it.id }?.let { list ->
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

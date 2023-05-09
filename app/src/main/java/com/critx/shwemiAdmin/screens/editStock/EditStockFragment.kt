package com.critx.shwemiAdmin.screens.editStock

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.*
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SetupStock.DiamondInfoDomain
import com.critx.shwemiAdmin.*
import com.critx.shwemiAdmin.databinding.AddGemValueDialogBinding
import com.critx.shwemiAdmin.databinding.FragmentEditStockBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.screens.setupStock.third.edit.persistImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class EditStockFragment : Fragment() {
    private lateinit var binding: FragmentEditStockBinding
    private val viewModel by viewModels<EditStockViewModel>()
    private lateinit var barlauncer: Any
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var launchChooseImage1: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage2: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage3: ActivityResultLauncher<Intent>
    private lateinit var launchChooseVideo: ActivityResultLauncher<Intent>
    private lateinit var launchChooseGif: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>


    private var firstCatId = ""
    private var secondCatId = ""
    private var thirdCatId: String? = null
    private var fourthCatId: String? = null
    private var productId = ""

    var photo1Id: MultipartBody.Part? = null
    var photo2Id: MultipartBody.Part? = null
    var photo3Id: MultipartBody.Part? = null
    var gifId: MultipartBody.Part? = null


    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.edit_stock)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage1 = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        viewModel.selectedImgUri1 = File(path)
                        if (photo1Id == null) photo1Id = null

                    }
                    binding.ivImage1.loadImageWithGlideWithUri(data.data)
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
                        if (photo2Id == null) photo2Id = null

                    }
                    binding.ivImage2.loadImageWithGlideWithUri(data.data)
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
                        if (photo3Id == null) photo3Id = null
                    }
                    binding.ivImage3.loadImageWithGlideWithUri(data.data)

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
                        if (path.endsWith(".gif")) {
                            viewModel.selectedGifUri = File(path)
                            if (gifId == null) gifId = null
                            binding.ivGif.loadImageWithGlideAsGifFromStorage(File(path))
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please choose a gif file",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
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
        return FragmentEditStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        toolbarsetup()
        barlauncer = this.getBarLauncherTest(requireContext()) {
            binding.edtScanHere.setText(it)
            viewModel.getProduct(it)
        }
        binding.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }

        binding.edtScanHere.setOnKeyListener { view, keyCode, keyevent ->
            //If the keyevent is a key-down event on the "enter" button
            if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Perform your action on key press here
                viewModel.getProduct(binding.edtScanHere.text.toString())

                hideKeyboard(activity, binding.edtScanHere)
                true
            } else false
        }
        binding.btnSave.setOnClickListener {
            editProduct()
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


        viewModel.getProductLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    viewModel.diamondInfo =
                        it.data?.diamond_info?.diamond_info
                    viewModel.diamondPriceFromGS =
                        it.data?.diamond_info?.price_from_goldsmith

                    viewModel.diamondValueFromGS =
                        it.data?.diamond_info?.value_from_goldsmith

                    viewModel.diamondPriceForSale =
                        it.data?.diamond_info?.price_for_sale

                    viewModel.gemValue =
                        it.data?.product_costs?.gem_value.toString()
                    viewModel.diamondValueForSale = it.data?.product_costs?.gem_value

                    viewModel.scanStock(binding.edtScanHere.text.toString())
                    firstCatId = it.data!!.jewellery_type
                    secondCatId = it.data!!.jewellery_quality
                    thirdCatId = it.data?.group
                    fourthCatId = it.data?.category
                    binding.edtEnterStockName.setText(it.data!!.name)
                    binding.edtGoldGemGm.setText(it.data!!.gold_and_gem_weight_gm.toString())
                    val kpy = getKPYFromYwae(it.data!!.gem_weight_ywae.toDouble())
                    binding.edtK.setText(kpy[0])
                    binding.edtP.setText(kpy[1])
                    binding.edtY.setText(kpy[2])
                    binding.edtGemValue.setText(it.data!!.product_costs.gem_value.toString())
                    binding.edtPtClipPrice.setText(it.data!!.product_costs.pt_and_clip_cost.toString())
                    binding.edtServiceFee.setText(it.data!!.product_costs.maintenance_cost.toString())
                    binding.btnGemValue.setOnClickListener {
                        val builder = MaterialAlertDialogBuilder(requireContext())
                        val inflater: LayoutInflater = LayoutInflater.from(builder.context)
                        val alertDialogBinding = AddGemValueDialogBinding.inflate(
                            inflater, ConstraintLayout(builder.context), false
                        )
                        builder.setView(alertDialogBinding.root)
                        val alertDialog = builder.create()

                        val displayMetrics = DisplayMetrics()
                        var mDisplayWidth = 0
                        var mDisplayHeight = 0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            val mDisplayMetrics =
                                requireActivity().windowManager.currentWindowMetrics
                            mDisplayWidth = mDisplayMetrics.bounds.width()
                            mDisplayHeight = mDisplayMetrics.bounds.height()
                        } else {
                            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                            mDisplayWidth = displayMetrics.widthPixels
                            mDisplayHeight = displayMetrics.heightPixels
                        }


                        val mLayoutParams = WindowManager.LayoutParams()
                        mLayoutParams.width = (mDisplayWidth).toInt()
                        mLayoutParams.height = (mDisplayHeight).toInt()
                        mLayoutParams.verticalMargin = 16f
                        alertDialog.window?.attributes = mLayoutParams

                        alertDialog.setCancelable(false)
                        viewModel.diamondInfo?.let { alertDialogBinding.edtDiamondWeightAndShape.setText(it) }
                        viewModel.diamondPriceFromGS?.let { alertDialogBinding.edtDiamondPrice.setText(it.toString()) }
                        viewModel.diamondValueFromGS?.let { alertDialogBinding.edtDiamondValue.setText(it.toString()) }
                        viewModel.diamondPriceForSale?.let { alertDialogBinding.edtDiamondActualPrice.setText(it.toString()) }
                        viewModel.gemValue?.let { alertDialogBinding.actDiamondActualSellValue.setText(it.toString()) }
                        alertDialogBinding.btnCalculate.setOnClickListener {
                            if (alertDialogBinding.btnCalculate.text == "Calculate") {
                                calculate(alertDialogBinding)
                            } else {
                                viewModel.diamondInfo =
                                    alertDialogBinding.edtDiamondWeightAndShape.text.toString()
                                viewModel.diamondPriceFromGS =
                                    alertDialogBinding.edtDiamondPrice.text.toString().toInt()
                                viewModel.diamondValueFromGS =
                                    alertDialogBinding.edtDiamondValue.text.toString().toInt()
                                viewModel.diamondPriceForSale =
                                    alertDialogBinding.edtDiamondActualPrice.text.toString().toInt()

                                viewModel.diamondValueForSale =1
                                    alertDialogBinding.actDiamondActualSellValue.text.toString().toInt()

                                viewModel.gemValue =
                                    alertDialogBinding.actDiamondActualSellValue.text.toString()
                                binding.edtGemValue.setText(viewModel.gemValue.orEmpty())
                                alertDialog.dismiss()
                            }

                        }
                        alertDialogBinding.tvReset.setOnClickListener {
                            resetValue(alertDialogBinding)
                        }
                        alertDialogBinding.ivExit.setOnClickListener {
                            alertDialog.dismiss()
                        }
                        alertDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        alertDialog.window?.setDimAmount(0.8f)
                        alertDialog.show()

                    }

                    if (it.data!!.files.find { it.type == "video" } != null) {
                        it.data!!.files.find { it.type == "video" }
                            ?.let { binding.ivVideo.getThumbnail(it.url) }
                    }
                    val imageList = it.data!!.files.filter { it.type == "image" }
                    repeat(imageList.size) {
                        if (imageList[it].url.endsWith(".gif")) {
                            binding.ivGif.loadImageWithGlideAsGif(imageList[it].url)
                            setOriginalImage(imageList[it].url, imageList[it].id, 3)
                            gifId = MultipartBody.Part.createFormData(
                                "images[3][id]",
                                imageList[it].id
                            )

                        } else {
                            setOriginalImage(imageList[it].url, imageList[it].id, it)
                        }
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

        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    productId = it.data!!.id
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

        viewModel.editProductLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {
                        viewModel.resetEditProductLiveData()
                        viewModel.resetDimaondData()
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


    fun setOriginalImage(imageUrl: String, imageId: String, placeHolder: Int) {
        when (placeHolder) {
            0 -> {
                binding.ivImage1.loadImageWithGlide(imageUrl)
                photo1Id = MultipartBody.Part.createFormData(
                    "images[0][id]",
                    imageId
                )
            }
            1 -> {
                binding.ivImage2.loadImageWithGlide(imageUrl)
                photo2Id = MultipartBody.Part.createFormData(
                    "images[1][id]",
                    imageId
                )
            }
            2 -> {
                binding.ivImage3.loadImageWithGlide(imageUrl)
                photo3Id = MultipartBody.Part.createFormData(
                    "images[2][id]",
                    imageId
                )
            }

        }
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                bm = getBitMapWithGlide(imageUrl, requireContext())
//                val fileName: String =
//                    imageUrl.substring(imageUrl.lastIndexOf('/') + 1)
//                when (placeHolder) {
//                    0 -> {
//                        viewModel.selectedImgUri1 = persistImage(
//                            bm!!,
//                            fileName,
//                            requireContext()
//                        )
//                    }
//                    1 -> {
//                        viewModel.selectedImgUri2 = persistImage(
//                            bm!!,
//                            fileName,
//                            requireContext()
//                        )
//                    }
//                    2 -> {
//                        viewModel.selectedImgUri3 = persistImage(
//                            bm!!,
//                            fileName,
//                            requireContext()
//                        )
//                    }
//                    3 -> {
//                        val gifDrawable = getGifWithGlide(imageUrl,requireContext())
//                        viewModel.selectedGifUri = getGifFromUrl(gifDrawable,requireContext())
//                    }
//
//                }
//
//            }
//        }
    }

    fun chooseVideo() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "video/*"
        launchChooseVideo.launch(pickIntent)
    }

    fun chooseGif() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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

    fun resetValue(dialogbinding: AddGemValueDialogBinding) {
        dialogbinding.edtDiamondWeightAndShape.text?.clear()
        dialogbinding.edtDiamondValue.text?.clear()
        dialogbinding.edtDiamondPrice.text?.clear()
        dialogbinding.edtDiamondActualPrice.text?.clear()
        dialogbinding.actDiamondActualSellValue.text?.clear()
        dialogbinding.tvReset.setTextColor(requireContext().getColorStateList(R.color.edit_text_color))
        dialogbinding.btnCalculate.text = "Calculate"
    }

    fun calculate(dialogbinding: AddGemValueDialogBinding) {
        if (dialogbinding.edtDiamondPrice.text.isNullOrEmpty() ||
            dialogbinding.edtDiamondActualPrice.text.isNullOrEmpty() ||
            dialogbinding.edtDiamondValue.text.isNullOrEmpty()
        ) {
            Toast.makeText(requireContext(), "Fill The Required Fields", Toast.LENGTH_LONG).show()
        } else {
            val diamondPrice = dialogbinding.edtDiamondPrice.text.toString().toDouble()
            val diamondActualPrice = dialogbinding.edtDiamondActualPrice.text.toString().toDouble()
            val diamondValue = dialogbinding.edtDiamondValue.text.toString().toDouble()
            val actualSellValue = diamondActualPrice * (diamondValue / diamondPrice)
            dialogbinding.actDiamondActualSellValue.setText(actualSellValue.toInt().toString())
            dialogbinding.tvReset.setTextColor(requireContext().getColorStateList(R.color.primary_color))
            dialogbinding.btnCalculate.text = "Add"
        }

    }

    fun editProduct() {
        if (binding.edtGoldGemGm.text.isNullOrEmpty() ||
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
                    "images[0][file]",
                    it.name,
                    requestBody1!!
                )
            }

            var photo2: MultipartBody.Part? = null
            var requestBody2: RequestBody? = null
            viewModel.selectedImgUri2?.let {
                requestBody2 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo2 = MultipartBody.Part.createFormData(
                    "images[1][file]",
                    it.name,
                    requestBody2!!
                )
            }

            var photo3: MultipartBody.Part? = null
            var requestBody3: RequestBody? = null
            viewModel.selectedImgUri3?.let {
                requestBody3 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                photo3 = MultipartBody.Part.createFormData(
                    "images[2][file]",
                    it.name,
                    requestBody3!!
                )
            }

            var selectedGif: MultipartBody.Part? = null
            var requestBody4: RequestBody? = null
            viewModel.selectedGifUri?.let {
                requestBody4 = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                selectedGif = MultipartBody.Part.createFormData(
                    "images[3][file]",
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


            val name = binding.edtEnterStockName.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val goldGemWeight = binding.edtGoldGemGm.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val gemValue = binding.edtGemValue.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val ptClipPrice = binding.edtPtClipPrice.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val serviceFee = binding.edtServiceFee.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val kyat = binding.edtK.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val pae = binding.edtP.text?.toString()
                ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val resultYwae = getYwaeFromKPY(
                binding.edtK.text.toString().toInt(), binding.edtP.text.toString().toInt(),
                binding.edtY.text.toString().toDouble()
            )
            val ywae = resultYwae.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val diamondInfo = if (viewModel.diamondInfo.isNullOrEmpty()) {
                null
            } else {
                viewModel.diamondInfo?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            val diamondPriceFromGS = if (viewModel.diamondPriceFromGS == null) {
                null
            } else {
                viewModel.diamondPriceFromGS!!.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            val diamondValueFromGS =
                if (viewModel.diamondValueFromGS == null) {
                    null
                } else {
                    viewModel.diamondValueFromGS!!.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
            val diamondValueForSale = if (viewModel.diamondValueForSale == null) {
                null
            } else {
                viewModel.diamondValueForSale!!.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            val diamondPriceForSale =
                if (viewModel.diamondPriceForSale == null) {
                    null
                } else {
                    viewModel.diamondPriceForSale!!.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }

            val type =
                firstCatId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val quality =
                secondCatId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val group =
                thirdCatId?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val category =
                fourthCatId?.toRequestBody("multipart/form-data".toMediaTypeOrNull())



            viewModel.editProduct(
                productId,
                name,
                type,
                quality,
                group,
                category,
                goldGemWeight,
                ywae,
                gemValue,
                ptClipPrice,
                serviceFee,
                diamondInfo,
                diamondPriceFromGS,
                diamondValueFromGS,
                diamondPriceForSale,
                diamondValueForSale,
                photo1,
                photo1Id,
                photo2,
                photo2Id,
                photo3,
                photo3Id,
                selectedGif,
                gifId,
                video
            )
        }
    }
}
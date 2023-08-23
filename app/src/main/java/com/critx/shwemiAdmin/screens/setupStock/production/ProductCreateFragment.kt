package com.critx.shwemiAdmin.screens.setupStock.production

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.critx.shwemiAdmin.databinding.AddGemValueDialogBinding
import com.critx.shwemiAdmin.databinding.FragmentProductCreateBinding
import com.critx.shwemiAdmin.databinding.ProductAddedDialogBinding
import com.critx.shwemiAdmin.generateNumberFromEditText
import com.critx.shwemiAdmin.getKPYFromYwae
import com.critx.shwemiAdmin.getYwaeFromGram
import com.critx.shwemiAdmin.getYwaeFromKPY
import com.critx.shwemiAdmin.printHelper.PrintTask
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
import java.io.InputStream


@AndroidEntryPoint
class ProductCreateFragment : Fragment() {
    private lateinit var binding: FragmentProductCreateBinding
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private val viewModel by viewModels<FinalStockSetupViewModel>()
    private val args by navArgs<ProductCreateFragmentArgs>()
    private lateinit var launchChooseImage1: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage2: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage3: ActivityResultLauncher<Intent>
    private lateinit var launchChooseVideo: ActivityResultLauncher<Intent>
    private lateinit var launchChooseGif: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>

    var photo1Id: MultipartBody.Part? = null
    var photo2Id: MultipartBody.Part? = null
    var photo3Id: MultipartBody.Part? = null
    var gifId: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage1 = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                        if (path.endsWith(".gif")) {
                            Toast.makeText(
                                requireContext(),
                                "Please choose image file",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            viewModel.selectedImgUri1 = File(path)
                            if (photo1Id != null) photo1Id = null
                            binding.ivImage1.setImageURI(data.data)
                        }
                    }
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
                        if (path.endsWith(".gif")) {
                            Toast.makeText(
                                requireContext(),
                                "Please choose image file",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            viewModel.selectedImgUri2 = File(path)
                            if (photo2Id == null) photo2Id = null
                            binding.ivImage2.setImageURI(data.data)
                        }
                    }
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
                        if (path.endsWith(".gif")) {
                            Toast.makeText(
                                requireContext(),
                                "Please choose image file",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            viewModel.selectedImgUri3 = File(path)
                            if (photo3Id == null) photo3Id = null
                            binding.ivImage3.setImageURI(data.data)
                        }
                    }
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
                        }
                        Toast.makeText(
                            requireContext(),
                            "Please choose a gif file",
                            Toast.LENGTH_LONG
                        ).show()
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
        return FragmentProductCreateBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProductCode(args.secondCatId)
        binding.btnRefreshProductCode.setOnClickListener {
            viewModel.getProductCode(args.secondCatId)
        }
        if (args.category != null) {
            setFirstCatImage(
                args.category!!.imageUrlList[0].url,
                args.category!!.imageUrlList[0].id
            )
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
                val mDisplayMetrics = requireActivity().windowManager.currentWindowMetrics
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
            viewModel.diamondPriceFromGS?.let { alertDialogBinding.edtDiamondPrice.setText(it) }
            viewModel.diamondValueFromGS?.let { alertDialogBinding.edtDiamondValue.setText(it) }
            viewModel.diamondPriceForSale?.let { alertDialogBinding.edtDiamondActualPrice.setText(it) }
            viewModel.gemValue?.let { alertDialogBinding.actDiamondActualSellValue.setText(it) }


            alertDialogBinding.btnCalculate.setOnClickListener {
                if (alertDialogBinding.btnCalculate.text == "Calculate") {
                    calculate(alertDialogBinding)
                } else {
                    viewModel.diamondInfo =
                        alertDialogBinding.edtDiamondWeightAndShape.text.toString()
                    viewModel.diamondPriceFromGS =
                        alertDialogBinding.edtDiamondPrice.text.toString()
                    viewModel.diamondValueFromGS =
                        alertDialogBinding.edtDiamondValue.text.toString()
                    viewModel.diamondPriceForSale =
                        alertDialogBinding.edtDiamondActualPrice.text.toString()
                    viewModel.gemValue =
                        alertDialogBinding.actDiamondActualSellValue.text.toString()
                    viewModel.diamondValueForSale =
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


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.createProductLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()

                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    val gemWeightYwae = getYwaeFromKPY(
                        generateNumberFromEditText(binding.edtK).toInt(),
                        generateNumberFromEditText(binding.edtP).toInt(),
                        generateNumberFromEditText(binding.edtY).toDouble(),
                    ).toDouble()

                    val goldGemWeightYwae = getYwaeFromGram(binding.edtGoldGemGm.text.toString().toDouble()).toDouble()
                    val goldWeightYwae = goldGemWeightYwae - gemWeightYwae
                    val weightKPY = getKPYFromYwae(goldWeightYwae)

                    val weightKyat = weightKPY[0]
                    val weightPae = weightKPY[1]
                    val weightYwae= weightKPY[2]
                    printItem(
                        productCode = binding.tvStockCodeNumber.text.toString(),
                        weightGm = binding.edtGoldGemGm.text.toString(),
                        weightKyat = weightKyat.toString(),
                        weightPae = weightPae.toString(),
                        weightYwae = weightYwae.toString()
                    )
                    viewModel.selectedImgUri1 = null
                    viewModel.selectedImgUri2 = null
                    viewModel.selectedImgUri3 = null
                    viewModel.selectedGifUri = null
                    viewModel.selectedVideoUri = null
                    viewModel.resetGetProductLiveData()
                    showSuccesDialog()


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


        viewModel.getProductLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()

                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.tvStockCodeNumber.text = it.data.orEmpty()
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



        binding.btnNext.setOnClickListener {
            createProduct()
        }

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

    fun showSuccesDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater: LayoutInflater = LayoutInflater.from(builder.context)
        val successBinding = ProductAddedDialogBinding.inflate(
            inflater, ConstraintLayout(builder.context), false
        )
        builder.setView(successBinding.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        successBinding.btnCreateAnotherStock.setOnClickListener {
            viewModel.getProductCode(args.secondCatId)
            formReset()
            alertDialog.dismiss()
        }
        successBinding.btnCreateNewItem.setOnClickListener {
            alertDialog.dismiss()
            findNavController().navigate(ProductCreateFragmentDirections.actionProductCreateFragmentToSetupStockFragment())
//            findNavController().navigate(ProductSetupFragmentDirections.actionProductSetupFragmentToSetupStockFragment())

        }
        successBinding.btnPrintLabel.setOnClickListener {
            alertDialog.dismiss()
            findNavController().navigate(ProductCreateFragmentDirections.actionProductCreateFragmentToSetupStockFragment())
//            findNavController().navigate(ProductSetupFragmentDirections.actionProductSetupFragmentToSetupStockFragment())
        }
        successBinding.btnDone.setOnClickListener {
//            findNavController().navigate(ProductSetupFragment.)
            alertDialog.dismiss()
            findNavController().navigate(ProductCreateFragmentDirections.actionProductCreateFragmentToSetupStockFragment())

        }
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

    fun createProduct() {

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


            val photoList = mutableListOf<MultipartBody.Part?>(photo1, photo2, photo3, selectedGif)
            val photoToUpload = photoList.filterNotNull() as MutableList


            val productCode = binding.tvStockCodeNumber.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
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
            val diamondPriceFromGS = if (viewModel.diamondPriceFromGS.isNullOrEmpty()) {
                null
            } else {
                viewModel.diamondPriceFromGS?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            val diamondValueFromGS =
                if (viewModel.diamondValueFromGS.isNullOrEmpty()) {
                    null
                } else {
                    viewModel.diamondValueFromGS?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
            val diamondValueForSale = if (viewModel.diamondValueForSale.isNullOrEmpty()) {
                null
            } else {
                viewModel.diamondValueForSale?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
            val diamondPriceForSale =
                if (viewModel.diamondPriceForSale.isNullOrEmpty()) {
                    null
                } else {
                    viewModel.diamondPriceForSale?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }

            val type =
                args.firstCatId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val quality =
                args.secondCatId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val group =
                args.thirdCatId?.let {
                    it.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
            val category =
                args.fourthCatId?.let {
                    it.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }



            viewModel.createProduct(
                name,
                productCode,
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

    fun setFirstCatImage(imageUrl: String, imageId: String) {
        binding.ivImage1.loadImageWithGlide(imageUrl)
        photo1Id = MultipartBody.Part.createFormData(
            "images[0][id]",
            imageId
        )
    }

    fun formReset() {
        binding.edtEnterStockName.setText("")
        binding.edtGemValue.setText("")
        binding.edtK.setText("0")
        binding.edtP.setText("0")
        binding.edtY.setText("0")
        binding.edtGoldGemGm.setText("0")
        binding.edtPtClipPrice.setText("0")
        binding.edtServiceFee.setText("0")
        if (args.category != null) {
            setFirstCatImage(
                args.category!!.imageUrlList[0].url,
                args.category!!.imageUrlList[0].id
            )
        } else {
            binding.ivImage1.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
        }
        binding.ivImage2.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
        binding.ivImage3.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
        binding.ivGif.setImageDrawable(requireContext().getDrawable(R.drawable.empty_gif))
        binding.ivVideo.setImageDrawable(requireContext().getDrawable(R.drawable.empty_video))
        viewModel.resetDimaondData()
        viewModel.selectedImgUri1 = null
        viewModel.selectedImgUri2 = null
        viewModel.selectedImgUri3 = null
        viewModel.selectedGifUri = null
        viewModel.selectedVideoUri = null
    }
    private fun printItem(productCode: String,weightKyat:String,weightPae:String,weightYwae:String,weightGm:String) {

//NS-010623-001
        val sample = " AA1V00240H0320" +
                "%2H0309V002282D30,L,04,1,0DN0013,$productCode" +
                "%2H0314V00122P02RH0,SATOSERIF.ttf,0,020,020,${productCode.substring(0, 2)}" +
                "%2H0314V00102P02RH0,SATOSERIF.ttf,0,020,020,${productCode.substring(3, 9)}" +
                "%2H0314V00082P02RH0,SATOSERIF.ttf,0,020,020,${productCode.substring(10, productCode.length)}" +
                "%2H0315V00060P02RH0,SATOSERIF.ttf,1,020,023,$weightGm gm" +
                "%2H0312V00028P02RH0,SATOSERIF.ttf,1,020,020,${weightKyat}  $weightPae  $weightYwae" +
                "Q1" +
                "Z"




        PrintTask.isPortSet = false
        PrintTask.variableMap = HashMap<String, String>()
        PrintTask.variableMap.put("__command", sample)
//        PrintTask.variableMap.put("__encoding", "base64")
        PrintTask(requireActivity()).execute("dummy")
    }
}
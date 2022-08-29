package com.critx.shwemiAdmin.screens.setupStock.production

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.CancellationSignal
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.getAlertDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.AddGemValueDialogBinding
import com.critx.shwemiAdmin.databinding.FragmentProductCreateBinding
import com.critx.shwemiAdmin.databinding.ProductAddedDialogBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.convertBitmapToFile
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.getRealVideoPathFromUri
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.getResizedBitmap
import com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock.RecommendStockAdapter
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class ProductCreateFragment : Fragment() {
    private lateinit var binding:FragmentProductCreateBinding
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter: RecommendStockAdapter
    private val viewModel by viewModels<FinalStockSetupViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

//    private val args by navArgs<FinalStockSetupFragmentArgs>()


    private lateinit var launchChooseImage1: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage2: ActivityResultLauncher<Intent>
    private lateinit var launchChooseImage3: ActivityResultLauncher<Intent>
    private lateinit var launchChooseVideo: ActivityResultLauncher<Intent>
    private lateinit var launchChooseGif: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                            500
                        );// 400 is for example, replace with desired size
                        binding.ivImage1.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.selectedImgUri1 = SelectedImage(file!!, selectedImage!!)
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
                        binding.ivImage2.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.selectedImgUri2 = SelectedImage(file!!, selectedImage!!)
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
                        binding.ivImage3.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.selectedImgUri3 = SelectedImage(file!!, selectedImage!!)
                    }
                }

            }

        launchChooseVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let {

                        viewModel.selectedVideoUri =
                            getRealPathFromUri(requireContext(), it)?.let { it1 ->

                                File(
                                    it1
                                )
                            }
                    }
                    if (result.data != null && result?.data?.data != null) {
                        val selectedImageUri: Uri? = result.data!!.data
//                        binding.ivVideo.setVideoURI(selectedImageUri)
//                        binding.ivVideo.pause()
                        val bmThumbnail = getRealVideoPathFromUri(
                            requireContext(),
                            result.data!!.data!!
                        ).let {
                            val thumbnail = ThumbnailUtils.createVideoThumbnail(
                                File(it!!),
                                Size(500, 500), android.os.CancellationSignal()
                            )
                            binding.ivVideo.setImageBitmap(thumbnail)
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
                        binding.ivGif.setImageBitmap(selectedImage)
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.selectedGifUri = SelectedImage(file!!, selectedImage!!)
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
        binding.cbGemValue.setOnCheckedChangeListener { compoundButton, ischecked ->
            if (ischecked) {
                val builder = MaterialAlertDialogBuilder(requireContext())
                val inflater: LayoutInflater = LayoutInflater.from(builder.context)
                val alertDialogBinding = AddGemValueDialogBinding.inflate(
                    inflater, ConstraintLayout(builder.context), false
                )
                builder.setView(alertDialogBinding.root)
                val alertDialog = builder.create()
                alertDialog.setCancelable(false)
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
                alertDialog.show()
            } else {
                viewModel.resetDimaondData()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //CreateProduct
                launch {
                    viewModel.createProductState.collectLatest {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.success !=null){
                            showSuccesDialog()
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

    fun showSuccesDialog(){
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
        val diamondPrice = dialogbinding.edtDiamondPrice.text.toString().toInt()
        val diamondActualPrice = dialogbinding.edtDiamondActualPrice.text.toString().toInt()
        val diamondValue = dialogbinding.edtDiamondValue.text.toString().toInt()
        val actualSellValue = diamondActualPrice * (diamondValue / diamondPrice)
        dialogbinding.actDiamondActualSellValue.setText(actualSellValue.toString())
        dialogbinding.tvReset.setTextColor(requireContext().getColorStateList(R.color.primary_color))
        dialogbinding.btnCalculate.text = "Add"

    }

    fun createProduct() {

        var photo1: MultipartBody.Part? = null
        var photo2: MultipartBody.Part? = null
        var photo3: MultipartBody.Part? = null
        var video: MultipartBody.Part? = null
        var gif: MultipartBody.Part? = null


        viewModel.selectedImgUri1?.let {
            val requestBody = convertBitmapToFile(
                it.file.name,
                it.bitMap,
                requireContext()
            ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo1 = MultipartBody.Part.createFormData("images[]", it.file.name, requestBody)
        }
        viewModel.selectedImgUri2?.let {
            val requestBody = convertBitmapToFile(
                it.file.name,
                it.bitMap,
                requireContext()
            ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo2 = MultipartBody.Part.createFormData("images[]", it.file.name, requestBody)
        }
        viewModel.selectedImgUri3?.let {
            val requestBody = convertBitmapToFile(
                it.file.name,
                it.bitMap,
                requireContext()
            ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
            photo3 = MultipartBody.Part.createFormData("images[]", it.file.name, requestBody)
        }
        viewModel.selectedVideoUri?.let {
            val requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            video = MultipartBody.Part.createFormData("video", it.name, requestBody)
        }
        viewModel.selectedGifUri?.let {
            val requestBody = convertBitmapToFile(
                it.file.name,
                it.bitMap,
                requireContext()
            ).asRequestBody("multipart/form-data".toMediaTypeOrNull())
            gif = MultipartBody.Part.createFormData("images[]", it.file.name, requestBody)
        }
        val photoList = mutableListOf(photo1!!, photo2!!, photo3!!, gif!!)
//        val videoList = mutableListOf(video!!)

        val name = binding.edtEnterStockName.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val goldGemWeight = binding.edtGoldGemGm.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val gemValue = binding.edtGemValue.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ptClipPrice = binding.edtPtClipPrice.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val serviceFee = binding.edtServiceFee.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val kyat = binding.edtK.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val pae = binding.edtP.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ywae = binding.edtY.text.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val diamondInfo =
            viewModel.diamondInfo?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val diamondPriceFromGS =
            viewModel.diamondPriceFromGS?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val diamondValueFromGS =
            viewModel.diamondValueFromGS?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val diamondValueForSale =
            viewModel.diamondValueForSale?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val diamondPriceForSale =
            viewModel.diamondPriceForSale?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val type = sharedViewModel.firstCat!!.id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val quality =
            sharedViewModel.secondCat!!.id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val group = sharedViewModel.thirdCat!!.id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val category =
            sharedViewModel.fourthCat!!.id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModel.createProduct(
            name,
            type,
            quality,
            group,
            category,
            goldGemWeight,
            kyat,
            pae,
            ywae,
            gemValue,
            ptClipPrice,
            serviceFee,
            diamondInfo,
            diamondPriceFromGS,
            diamondValueFromGS,
            diamondPriceForSale,
            diamondValueForSale,
            photoList,
            video!!
        )

//        viewModel.createProduct(
//            name,
//            args.firstCat.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//            args.secondCat.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//            args.thirdCat.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//            args.fourthCat.id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
//            goldGemWeight,
//            kyat,
//            pae,
//            ywae,
//            gemValue,
//            ptClipPrice,
//
//        )
    }

}
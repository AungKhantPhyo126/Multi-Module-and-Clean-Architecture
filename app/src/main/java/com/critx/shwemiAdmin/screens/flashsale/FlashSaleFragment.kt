package com.critx.shwemiAdmin.screens.flashsale

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.loadImageWithGlideWithUri
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentFlashSaleBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.collectstock.CollectStockViewModel
import com.critx.shwemiAdmin.screens.giveGold.convertToSqlDate
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@AndroidEntryPoint
class FlashSaleFragment : Fragment() {
    private lateinit var binding: FragmentFlashSaleBinding
    private lateinit var barlauncer: Any
    private val viewModel by viewModels<FlashSaleViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>
    private var snackBar: Snackbar? = null

    var startDate = ""
    var endDate = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentFlashSaleBinding.inflate(inflater).also {
            binding = it
        }.root
    }

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
                    binding.ivImage1.loadImageWithGlideWithUri(data.data)
                }
            }
        }
        readStoragePermissionlauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) chooseImage()
        }
    }

    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.flash_sale)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        barlauncer = this.getBarLauncherTest(requireContext()) {
            binding.edtScanHere.setText(it)
            viewModel.scanStock(it)
        }
        binding.edtScanHere.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    viewModel.scanStock(binding.edtScanHere.text.toString())
                    hideKeyboard(activity, binding.edtScanHere)
                    return true
                }
                return false
            }
        })
        val adapter = FlashSaleRecyclerAdapter {
            viewModel.removeStockCode(it)
        }
        binding.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }
        binding.mcvDateRange.setOnClickListener {
            showDateRangePicker()
        }
        binding.layoutDiscount.rvStockCodeList.adapter = adapter

        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    //id list
                    val resultItem = CollectStockBatchUIModel(
                        it.data!!.id,
                        binding.edtScanHere.text.toString(),
                        it.data!!.type.toString()
                    )
                    if (viewModel.stockCodeList.contains(resultItem).not()) {
                        viewModel.addStockCode(resultItem)
                    } else {
                        Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                            .show()
                    }
//                    findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment())
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.addFlashSaleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()) {
                        viewModel.resetStockCodes()
                        viewModel.selectedImgUri = null
                        binding.ivImage1.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
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

        viewModel.scannedStockcodes.observe(viewLifecycleOwner) {
            binding.layoutBtnGroup.btnApprove.isEnabled = it.size > 0
            binding.layoutDiscount.tvTotalScannedStockCount.text = it.size.toString()
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.edtScanHere.text?.clear()
        }
        binding.ivImage1.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }
        binding.ivRemove1.setOnClickListener {
            viewModel.selectedImgUri = null
            binding.ivImage1.setImageDrawable(requireContext().getDrawable(R.drawable.empty_picture))
        }

        binding.layoutBtnGroup.btnApprove.setOnClickListener {
            var imageToUpload: MultipartBody.Part? = null
            var requestBody: RequestBody? = null
            viewModel.selectedImgUri?.let {
                requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                imageToUpload = MultipartBody.Part.createFormData(
                    "image",
                    it.name,
                    requestBody!!
                )
            }
            if (imageToUpload == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_LONG).show()
            } else {
                viewModel.addFlashSale(
                    binding.layoutBtnGroup.tieTitle.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.layoutBtnGroup.tieDiscountAmount.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    startDate.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    endDate.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    viewModel.stockCodeList.map { it.productId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) },
                    imageToUpload!!
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val first_date_calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            first_date_calendar.time = Date(selection.first)

            val second_date_calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            second_date_calendar.time = Date(selection.second)

            startDate = convertToSqlDate(first_date_calendar)
            endDate = convertToSqlDate(second_date_calendar)

            binding.tvDateRange.text = "$startDate-$endDate"
            // Do something with the selected dates
        }

        picker.show(childFragmentManager, picker.toString())
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
package com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.loadImageWithGlide
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.commonkotlin.util.getOrderValue
import com.critx.shwemiAdmin.*
import com.critx.shwemiAdmin.databinding.FragmentFillOrderInfoBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class FillOrderInfoFragment : Fragment() {
    private lateinit var binding: FragmentFillOrderInfoBinding
    private val args by navArgs<FillOrderInfoFragmentArgs>()
    private val viewModel by viewModels<FillOrderInfoViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var barlauncer: Any
    private lateinit var barLauncherVoucher: Any

    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>


    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchChooseImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    if (data != null && data.data != null) {
                        getRealPathFromUri(requireContext(), data.data!!)?.let { path ->
                            viewModel.selectedImgOutsideUri = File(path)

                        }
                        binding.includeSampleTakeSection.ivOutside.setImageURI(data.data)
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
        return FragmentFillOrderInfoBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getGoldSmithList()
        barlauncer = this.getBarLauncherTest(requireContext()) {
            binding.includeSampleTakeSection.edtScanHere.setText(it)
            viewModel.scanStock(it)
        }

        binding.includeSampleTakeSection.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }
        binding.includeSampleTakeSection.edtScanHere.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    viewModel.scanStock(binding.includeSampleTakeSection.edtScanHere.text.toString())
                    hideKeyboard(activity, binding.includeSampleTakeSection.edtScanHere)
                    return true
                }
                return false
            }
        })

        barLauncherVoucher = this.getBarLauncherTest(requireContext()) {
            binding.includeSampleTakeSection.edtScanVoucherHere.setText(it)
            viewModel.scanVoucher(it)
        }
        binding.includeSampleTakeSection.tilScanVoucherHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barLauncherVoucher)
        }
        binding.includeSampleTakeSection.edtScanVoucherHere.setOnKeyListener(object :
            View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    viewModel.scanVoucher(binding.includeSampleTakeSection.edtScanHere.text.toString())
                    hideKeyboard(activity, binding.includeSampleTakeSection.edtScanHere)
                    return true
                }
                return false
            }
        })

        viewModel.voucherScanLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.checkSampleWithVoucher(it.data!!.id)
                    viewModel.resetVoucherScanLive()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.sampleLiveDataFromVoucher.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    it.data!!.forEach { sampleItem ->
                        if (viewModel.sampleList.contains(sampleItem)) {
                            Toast.makeText(
                                requireContext(),
                                "Stock Already Scanned",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }else if (sampleItem.specification.isNullOrEmpty().not()){
                            viewModel.addSample(sampleItem)
                        }
                    }
                    viewModel.resetSampleLiveDataFromVoucher()

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.tvCatName.text = args.bookMark.custom_category_name
        binding.ivStockImage.loadImageWithGlide(args.bookMark.image)
        val kpyList = getKPYFromYwae(args.bookMark.avg_unit_weight_ywae.toDouble())
        binding.edtK.setText(kpyList[0].toInt().toString())
        binding.edtP.setText( kpyList[1].toInt().toString())
        binding.edtY.setText(kpyList[2].toString())

        val adapter = StockInfoRecyclerAdapter(viewModel)
        binding.rvStockInfo.adapter = adapter

        val sampleImageRecyclerAdapter = SampleImageRecyclerAdapter {
            viewModel.remove(it)
        }
        binding.rvSampleList.adapter = sampleImageRecyclerAdapter
        binding.includeSampleTakeSection.btnSaveAndTake.setOnClickListener {
            var outSideimageToUpload: MultipartBody.Part? = null
            var requestBody: RequestBody? = null
            viewModel.selectedImgOutsideUri?.let {
                requestBody = it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                outSideimageToUpload = MultipartBody.Part.createFormData(
                    "image",
                    it.name,
                    requestBody!!
                )
                viewModel.saveOutsideSample(
                    binding.includeSampleTakeSection.edtStockName.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.includeSampleTakeSection.edtWeight.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.includeSampleTakeSection.edtSpecification.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    outSideimageToUpload!!
                )
            } ?: Toast.makeText(requireContext(), "Please upload a photo", Toast.LENGTH_LONG).show()

        }
        binding.includeSampleTakeSection.ivOutside.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }

        binding.includeSampleTakeSection.switch1.setOnCheckedChangeListener { compoundButton, b ->
//            binding.includeSampleTakeSection.outsideGroup.isVisible = b
            if (!b) binding.includeSampleTakeSection.outsideGroup.isVisible = false
            if (!b) binding.includeSampleTakeSection.tilScanVoucherHere.isVisible = false
            binding.includeSampleTakeSection.tilScanHere.isVisible = !b
            binding.includeSampleTakeSection.chooseOutsideOrVoucher.isVisible = b
        }
        binding.includeSampleTakeSection.btnOutsideSample.setOnClickListener {
            binding.includeSampleTakeSection.outsideGroup.isVisible = true
            binding.includeSampleTakeSection.chooseOutsideOrVoucher.isVisible = false
        }
        binding.includeSampleTakeSection.btnScanFromVoucher.setOnClickListener {
            binding.includeSampleTakeSection.tilScanVoucherHere.isVisible = true
            binding.includeSampleTakeSection.chooseOutsideOrVoucher.isVisible = false
        }
        if (args.bookMark.sizes.isNullOrEmpty()) {
            viewModel.getBookMarkStockInfo(args.bookMark.id)
        } else {
            adapter.submitList(args.bookMark.sizes)
            repeat(args.bookMark.sizes.size) {
                viewModel.orderQtyList.add("")
            }
            viewModel.jewellerySizeIdList.addAll(args.bookMark.sizes.map { it.id })
        }

        var toggle = 0
        binding.ivStar.setOnClickListener {
            if (toggle == 0) {
                toggle = 1
                binding.ivStar.setImageDrawable(requireContext().getDrawable(R.drawable.filled_star))

            } else {
                toggle = 0
                binding.ivStar.setImageDrawable(requireContext().getDrawable(R.drawable.star_icon))
            }
        }

        viewModel.saveOutsideSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    viewModel.addSample(it.data!!)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    loadingDialog.dismiss()
                }
            }
        }

        viewModel.takenSampleListLiveData.observe(viewLifecycleOwner) {
            binding.tvEmptySample.isVisible = it.isNullOrEmpty()
            sampleImageRecyclerAdapter.submitList(it)
            sampleImageRecyclerAdapter.notifyDataSetChanged()
        }

        viewModel.voucherScanLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.checkSampleWithVoucher(it.data!!.id)
                    viewModel.resetVoucherScanLive()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.sampleLiveDataFromVoucher.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    it.data!!.forEach { sampleItem ->
                        if (viewModel.sampleList.contains(sampleItem)) {
                            Toast.makeText(
                                requireContext(),
                                "Stock Already Scanned",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else if (sampleItem.specification.isNullOrEmpty().not()) {
                            viewModel.addSample(sampleItem)
                        }
                    }
                    viewModel.resetSampleLiveDataFromVoucher()

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.checkSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    if (it.data!!.id.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "Sample Not Found", Toast.LENGTH_LONG)
                            .show()
                    } else if (viewModel.sampleList.contains(it.data!!)) {
                        Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        viewModel.addSample(it.data!!)

                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.checkSampleUseCase(it.data!!.id)
                    viewModel.resetScanProductCodeLive()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.orderStockLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {
                        findNavController().popBackStack()
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }

        viewModel.bookMarkStockInfoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    repeat(it.data!!.size) {
                        viewModel.orderQtyList.add("")
                    }
                    viewModel.jewellerySizeIdList.addAll(it.data!!.map { it.id })
                    adapter.submitList(it.data)
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }

        //Gold Quality for goldSmith
        val list = listOf<String>("A", "B", "C", "100%")
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.item_drop_down_text, list)
        binding.actGoldQuality.setAdapter(arrayAdapter)
        if (args.bookMark.goldQuality != null){
            binding.actGoldQuality.setText(list.find { it == args.bookMark.goldQuality }, false)
        }else{
            binding.actGoldQuality.setText(list[0], false)
        }
        binding.actGoldQuality.setOnClickListener {
            binding.actGoldQuality.showDropdown(arrayAdapter)
        }

        viewModel.goldSmithListLiveData.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
//                    it.data!!.map { it.name }
                    val list = it.data!!.map { it.name }
                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.item_drop_down_text, list)
                    binding.actGoldSmith.setAdapter(arrayAdapter)
                    if (args.bookMark.goldSmith != null){
                        binding.actGoldSmith.setText(list.find { it == args.bookMark.goldSmith!!.name }, false)
                        viewModel.selectedGSId = args.bookMark.goldSmith!!.id
                    }else{
                        binding.actGoldSmith.setText(list[0], false)
                    }
                    binding.actGoldSmith.setOnClickListener {
                        binding.actGoldSmith.showDropdown(arrayAdapter)
                    }
                    viewModel.selectedGSId = it.data!![0].id
                    binding.actGoldSmith.addTextChangedListener { editable ->
                        viewModel.selectedGSId = it.data!!.find {
                            it.name == binding.actGoldSmith.text.toString()
                        }?.id
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.includeButton.btnApprove.setOnClickListener {
            binding.includeButton.btnOrder.isEnabled = true
            var totalOrderQty = 0
            repeat(viewModel.orderQtyList.size) {
                val orderQty =
                    if (viewModel.orderQtyList[it].isNotEmpty()) viewModel.orderQtyList[it] else "0"
                totalOrderQty += orderQty.toInt()
            }
            var gqValue: Double = when (binding.actGoldQuality.text.toString()) {
                "A" -> {
                    17.0
                }
                "B" -> {
                    17.5
                }
                "C" -> {
                    18.0
                }
                "100%" -> {
                    16.0
                }
                else -> {
                    0.0
                }
            }
            binding.includeButton.tieOrderWeight.setText(
                getHunderdPercentWt(
                    totalOrderQty,
                    getKyatsFromKPY(
                        binding.edtK.text.toString().toInt(),
                        binding.edtP.text.toString().toInt(),
                        binding.edtY.text.toString().toDouble(),
                    ), gqValue
                ).toString()
            )
        }
        binding.includeButton.btnOrder.setOnClickListener {
            val isImportant = MultipartBody.Part.createFormData(
                "order[is_important]",
                toggle.toString()
            )

            val bookMarkId = MultipartBody.Part.createFormData(
                "order[bookmark_id]",
                args.bookMark.id
            )

            val orderGoldQuality = MultipartBody.Part.createFormData(
                "order[gold_quality]",
                binding.actGoldQuality.text.toString()
            )

            val orderGoldSmith = MultipartBody.Part.createFormData(
                "order[goldsmith_id]",
                viewModel.selectedGSId.orEmpty()
            )

            var totalQty = 0
            viewModel.orderQtyList.forEach {
                var qty = if (it.isNullOrEmpty()) 0 else it.toInt()
                totalQty += qty
            }
            val equivalent_pure_gold_weight_kpy = MultipartBody.Part.createFormData(
                "order[equivalent_pure_gold_weight_ywae]",
                ((binding.includeButton.tieOrderWeight.text?.toString()?:"0.0").toDouble() * 128).toString()
//                getOrderValue(
//                    binding.edtK.text.toString().toDouble(),
//                    binding.edtP.text.toString().toDouble(),
//                    binding.edtY.text.toString().toDouble(),
//                    binding.actGoldQuality.text.toString(),
//                    totalQty
//                )
            )
            val order_qty = mutableListOf<MultipartBody.Part>()
            repeat(viewModel.orderQtyList.size) {
                val orderQty =
                    if (viewModel.orderQtyList[it].isNotEmpty()) viewModel.orderQtyList[it] else "0"
                order_qty.add(
                    MultipartBody.Part.createFormData(
                        "order[items][${it}][order_qty]",
                        orderQty
                    )
                )

//                sharedViewModel.orderGoldQuality["order[items][${it}][order_qty]"] =
//                    viewModel.orderQtyList[it]
//                sharedViewModel.jewellery_type_size_id["order[items][${it}][jewellery_type_size_id]"] =
//                    viewModel.jewellerySizeIdList[it]
            }
            val jewellery_type_size_id = mutableListOf<MultipartBody.Part>()
            repeat(viewModel.jewellerySizeIdList.size) {
                jewellery_type_size_id.add(
                    MultipartBody.Part.createFormData(
                        "order[items][${it}][jewellery_type_size_id]",
                        viewModel.jewellerySizeIdList[it]
                    )
                )
            }
            val sample_id = mutableListOf<MultipartBody.Part>()
            repeat(viewModel.sampleList.size) {
                sample_id.add(
                    MultipartBody.Part.createFormData(
                        "samples[${it}][sample_id]",
                        viewModel.sampleList[it].id!!
                    )
                )
            }
            val kyat = MultipartBody.Part.createFormData(
                "bookmark[avg_weight_per_unit_kyat]",
                binding.edtK.text.toString()
            )

            val pae = MultipartBody.Part.createFormData(
                "bookmark[avg_weight_per_unit_pae]",
                binding.edtP.text.toString()
            )

            val ywaeFromKpy = getYwaeFromKPY(binding.edtK.text.toString().toInt(),binding.edtP.text.toString().toInt(),binding.edtY.text.toString().toDouble())
            val avgYwae = MultipartBody.Part.createFormData(
                "bookmark[avg_unit_weight_ywae]",
               ywaeFromKpy.toString()
            )

            val kyatOrder = MultipartBody.Part.createFormData(
                "order[avg_weight_per_unit_kyat]",
                binding.edtK.text.toString()
            )

            val paeOrder = MultipartBody.Part.createFormData(
                "order[avg_weight_per_unit_pae]",
                binding.edtP.text.toString()
            )
            val orderYwaeFromKpy = getYwaeFromKPY(binding.edtK.text.toString().toInt(),binding.edtP.text.toString().toInt(),binding.edtY.text.toString().toDouble())
            val orderYwae = MultipartBody.Part.createFormData(
                "order[avg_unit_weight_ywae]",
                orderYwaeFromKpy.toString()
            )

            val gsNewItemId = args.bookMark.goldSmith?.let {
                MultipartBody.Part.createFormData(
                    "order[gs_new_item_id]",
                    args.bookMark.id
                )
            }

            if (totalQty == 0) {
                Toast.makeText(
                    requireContext(),
                    "Total Order Qty must not be zero",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if(gsNewItemId!=null){
                    viewModel.orderStock(
                        null,
                        orderYwae,
                        null,
                        null,
                        orderGoldQuality,
                        orderGoldSmith,
                        null,
                        gsNewItemId,
                        equivalent_pure_gold_weight_kpy,
                        jewellery_type_size_id,
                        order_qty,
                        sample_id,
                        isImportant,
                        null
                    )
                }else{
                    viewModel.orderStock(
                        null,
                        orderYwae,
                        null,
                        null,
                        orderGoldQuality,
                        orderGoldSmith,
                        bookMarkId,
                        null,
                        equivalent_pure_gold_weight_kpy,
                        jewellery_type_size_id,
                        order_qty,
                        sample_id,
                        isImportant,
                        null
                    )
                }

            }

        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
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

fun getHunderdPercentWt(totalOrderQty: Int, avgKyat: Double, gq: Double): Double {
    val result = totalOrderQty * (avgKyat) * 16 / gq
    return String.format("%.2f", result).toDouble()
}



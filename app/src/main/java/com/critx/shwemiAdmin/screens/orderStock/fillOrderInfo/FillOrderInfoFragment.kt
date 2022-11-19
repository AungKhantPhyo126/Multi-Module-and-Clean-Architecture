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
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.loadImageWithGlide
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.commonkotlin.util.getOrderValue
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentFillOrderInfoBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.showDropdown
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
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
    var photo: MultipartBody.Part? = null

    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>


    private lateinit var loadingDialog: AlertDialog
    private var selectedGoldSmith: String? = null
    private var selectedGoldQuality: String? = null
    private var snackBar: Snackbar? = null

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
//                        selectedImage = getResizedBitmap(
//                            selectedImage!!,
//                            500
//                        );// 400 is for example, replace with desired size
                        Log.i("imageResize", selectedImage?.width.toString())
                        val file = getRealPathFromUri(requireContext(), it)?.let { it1 ->
                            File(
                                it1
                            )
                        }
                        viewModel.setSelectedImgUri(SelectedImage(file!!, selectedImage!!))
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
        binding.ivStockImage.loadImageWithGlide(args.bookMark.image)
        binding.tvKyatValue.text = args.bookMark.avg_weight_per_unit_kyat
        binding.tvPaeValue.text = args.bookMark.avg_weight_per_unit_pae
        binding.tvYwaeValue.text =
            args.bookMark.avg_weight_per_unit_ywae.toDouble().toInt()
                .toString()
        val adapter = StockInfoRecyclerAdapter(viewModel)
        binding.rvStockInfo.adapter = adapter

        val sampleImageRecyclerAdapter = SampleImageRecyclerAdapter{
            viewModel.remove(it)
        }
        binding.rvSampleList.adapter = sampleImageRecyclerAdapter
        viewModel.selectedImgUri.observe(viewLifecycleOwner) {
            if (it != null) {
                val requestBody = it.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                binding.includeSampleTakeSection.ivOutside.setImageBitmap(it.bitMap)
                photo = MultipartBody.Part.createFormData("image", it.file.name, requestBody)
            }
        }
        binding.includeSampleTakeSection.btnSaveAndTake.setOnClickListener {
            if (photo != null) {
                viewModel.saveOutsideSample(
                    binding.includeSampleTakeSection.edtStockName.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.includeSampleTakeSection.edtWeight.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    binding.includeSampleTakeSection.edtSpecification.text.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    photo!!
                )
            } else {
                Toast.makeText(requireContext(), "Please upload a photo", Toast.LENGTH_LONG).show()
            }

        }
        binding.includeSampleTakeSection.ivOutside.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }

        binding.includeSampleTakeSection.switch1.setOnCheckedChangeListener { compoundButton, b ->
            binding.includeSampleTakeSection.outsideGroup.isVisible = b
            binding.includeSampleTakeSection.tilScanHere.isVisible = !b
        }
        viewModel.getBookMarkStockInfo(args.bookMark.id)

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
                    loadingDialog.dismiss()
                }
            }
        }

        viewModel.takenSampleListLiveData.observe(viewLifecycleOwner) {
            binding.tvEmptySample.isVisible = it.isNullOrEmpty()
            sampleImageRecyclerAdapter.submitList(it)
            sampleImageRecyclerAdapter.notifyDataSetChanged()
        }

        viewModel.checkSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    if (it.data!!.sampleId == null){
                        Toast.makeText(requireContext(),"Sample Not Found", Toast.LENGTH_LONG).show()
                    }else if (viewModel.sampleList.contains(it.data!!)) {
                        Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                            .show()
                    }else{
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
        val list = listOf<String>("A", "B", "C", "D")
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.item_drop_down_text, list)
        binding.actGoldQuality.setAdapter(arrayAdapter)
        binding.actGoldQuality.setText(list[0], false)
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
                    binding.actGoldSmith.setText(list[0], false)
                    binding.actGoldSmith.setOnClickListener {
                        binding.actGoldSmith.showDropdown(arrayAdapter)
                    }
                    if (sharedViewModel.selectedGoldSmith.value.isNullOrEmpty().not()) {
                        binding.actGoldSmith.setText(
                            it.data!!.find { it.id == sharedViewModel.selectedGoldSmith.value }!!.name,
                            false
                        )
                    }
                    sharedViewModel.selectedGoldSmith.value = it.data!![0].id
                    binding.actGoldSmith.addTextChangedListener { editable ->
                        sharedViewModel.selectedGoldSmith.value = it.data!!.find {
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



//        binding.btnSampleTake.setOnClickListener {
////            sharedViewModel.bookMarkId = MultipartBody.Part.createFormData(
////                "order[bookmark_id]",
////                sharedViewModel.selectedBookMark!!.id
////            )
////
////            sharedViewModel.orderGoldQuality = MultipartBody.Part.createFormData(
////                "order[gold_quality]",
////                binding.actGoldQuality.text.toString()
////            )
////
////            sharedViewModel.orderGoldSmith = MultipartBody.Part.createFormData(
////                "order[goldsmith_id]",
////                sharedViewModel.selectedGoldSmith.value!!
////            )
////
////
////
////            repeat(viewModel.orderQtyList.size) {
////                sharedViewModel.order_qty.add(
////                    MultipartBody.Part.createFormData(
////                        "order[items][${it}][order_qty]",
////                        viewModel.orderQtyList[it]
////                    )
////                )
////                sharedViewModel.jewellery_type_size_id.add(
////                    MultipartBody.Part.createFormData(
////                        "order[items][${it}][jewellery_type_size_id]",
////                        viewModel.orderQtyList[it]
////                    )
////                )
//////                sharedViewModel.orderGoldQuality["order[items][${it}][order_qty]"] =
//////                    viewModel.orderQtyList[it]
//////                sharedViewModel.jewellery_type_size_id["order[items][${it}][jewellery_type_size_id]"] =
//////                    viewModel.jewellerySizeIdList[it]
////            }
//            findNavController().navigate(FillOrderInfoFragmentDirections.actionFillOrderInfoFragmentToSampleTakeAndReturnFragment())
//        }
        binding.includeButton.btnApprove.setOnClickListener {

                val bookMarkId= MultipartBody.Part.createFormData(
                    "order[bookmark_id]",
                    args.bookMark.id
                )

                val orderGoldQuality = MultipartBody.Part.createFormData(
                    "order[gold_quality]",
                    binding.actGoldQuality.text.toString()
                )

                val orderGoldSmith = MultipartBody.Part.createFormData(
                    "order[goldsmith_id]",
                    sharedViewModel.selectedGoldSmith.value!!
                )

                var totalQty = 0
                viewModel.orderQtyList.forEach {
                    var qty = if (it.isNullOrEmpty()) 0 else it.toInt()
                    totalQty += qty
                }
                val equivalent_pure_gold_weight_kpy = MultipartBody.Part.createFormData(
                    "order[equivalent_pure_gold_weight_kpy]",
                    getOrderValue(
                        binding.tvKyatValue.text.toString().toDouble(),
                        binding.tvPaeValue.text.toString().toDouble(),
                        binding.tvYwaeValue.text.toString().toDouble(),
                        binding.actGoldQuality.text.toString(),
                        totalQty
                    )
                )
            val order_qty = mutableListOf<MultipartBody.Part>()
                repeat(viewModel.orderQtyList.size) {
                    val orderQty = if(viewModel.orderQtyList[it].isNotEmpty()) viewModel.orderQtyList[it] else "0"
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
                        viewModel.sampleList[it].sampleId!!
                    )
                )
            }

                viewModel.orderStock(
                    null,
                    null,
                    null,
                    orderGoldQuality,
                    orderGoldSmith,
                    bookMarkId,
                    equivalent_pure_gold_weight_kpy,
                    jewellery_type_size_id,
                    order_qty,
                    sample_id
                )


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
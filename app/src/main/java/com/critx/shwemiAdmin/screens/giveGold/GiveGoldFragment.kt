package com.critx.shwemiAdmin.screens.giveGold

import android.Manifest
import android.app.Activity
import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentGiveGoldBinding
import com.critx.shwemiAdmin.databinding.ServiceChargeDialogBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo.SampleImageRecyclerAdapter
import com.critx.shwemiAdmin.screens.repairStock.showServiceChargeOnlyDialog
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.screens.setupStock.third.edit.getRealPathFromUri
import com.critx.shwemiAdmin.showDropdown
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class GiveGoldFragment : Fragment() {
    private lateinit var binding: FragmentGiveGoldBinding
    private lateinit var alertDialogBinding:ServiceChargeDialogBinding
    private val viewModel by viewModels<GiveGoldViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var barLauncher: Any
    private lateinit var barLauncherSampleTake: Any
    var photo: MultipartBody.Part? = null
    private lateinit var launchChooseImage: ActivityResultLauncher<Intent>
    private lateinit var readStoragePermissionlauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.choose_due_date)
            .setSelection(Calendar.getInstance().timeInMillis)
            .build()

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
        return FragmentGiveGoldBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.give_gold)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getGoldSmithList()
        viewModel.getGoldBoxId()
        barLauncher = this.getBarLauncherTest(requireContext()) {
            alertDialogBinding.edtInvoiceNumber.setText(it)
        }
        val sampleImageRecyclerAdapter = SampleImageRecyclerAdapter{
            viewModel.remove(it)
        }
        binding.rvSampleList.adapter = sampleImageRecyclerAdapter

//        if (args.sampleList.isNullOrEmpty()) {
//            binding.btnSampleTake.setTextColor(requireContext().getColorStateList(R.color.edit_text_color))
//        } else {
//            binding.btnSampleTake.setTextColor(requireContext().getColorStateList(R.color.primary_color))
//        }



        /**retrieve product**/
        binding.btnRetrieveProduct.setOnClickListener {
            showServiceChargeDialogForGoldGive()
        }
        viewModel.serviceChargeLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!){
                        viewModel.resetserviceChargeLiveData()
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }


//        binding.btnSampleTake.setOnClickListener {
//            sharedViewModel.orderItem.value = binding.edtOrderItemName.text.toString()
//            sharedViewModel.orderQty.value = binding.edtOrderQty.text.toString()
//            sharedViewModel.weightK.value = binding.edtK.text.toString()
//            sharedViewModel.weightP.value = binding.edtP.text.toString()
//            sharedViewModel.weightY.value = binding.edtY.text.toString()
//            sharedViewModel.goldWeight.value = binding.edtGoldGm.text.toString()
//            sharedViewModel.gemWeight.value = binding.edtGemGm.text.toString()
//            sharedViewModel.goldAndGemWeight.value = binding.tvGoldAndGemGm.text.toString()
//            sharedViewModel.wastageK.value = binding.edtK2.text.toString()
//            sharedViewModel.wastageP.value = binding.edtP2.text.toString()
//            sharedViewModel.wastageY.value = binding.edtY2.text.toString()
//            sharedViewModel.dueDate.value = binding.tvDueDate.text.toString()
////            findNavController().navigate(GiveGoldFragmentDirections.actionGiveGoldFragmentToSampleTakeAndReturnFragment())
//        }
        barLauncherSampleTake = this.getBarLauncherTest(requireContext()) {
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
                    if (it.data!!.sampleId.isNullOrEmpty()){
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
                    viewModel.checkSample(it.data!!.id)
                    viewModel.resetScanProductCodeLive()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.giveGoldLiveData.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {
                        viewModel.resetGiveGoldLiveData()
                    }

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
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
                    binding.actGoldSmith.setOnClickListener {
                        binding.actGoldSmith.showDropdown(arrayAdapter)
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.selectedImgUri.observe(viewLifecycleOwner) {
            if (it != null) {
                val requestBody = it.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                binding.includeSampleTakeSection.ivOutside.setImageBitmap(it.bitMap)
                photo = MultipartBody.Part.createFormData("image", it.file.name, requestBody)
            }
        }
        viewModel.getGoldBoxLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    if (sharedViewModel.selectedGoldBoxId.value.isNullOrEmpty().not()) {
                        binding.mcvHundredPercent.isChecked =
                            it.data!!.find { it.id == sharedViewModel.selectedGoldBoxId.value }!!.name == "100%"
                        binding.mcvOther.isChecked =
                            it.data!!.find { it.id == sharedViewModel.selectedGoldBoxId.value }!!.name == "Other"
                    }
                    binding.mcvHundredPercent.setOnCheckedChangeListener { card, isChecked ->
                        if (isChecked) {
                            sharedViewModel.selectedGoldBoxId.value =
                                it.data!!.find { it.name == "100%" }!!.id
                        }
                    }
                    binding.mcvOther.setOnCheckedChangeListener { card, isChecked ->
                        if (isChecked) {
                            sharedViewModel.selectedGoldBoxId.value =
                                it.data!!.find { it.name == "Other" }!!.id
                        }
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        binding.mcvHundredPercent.setOnClickListener {
            binding.mcvHundredPercent.isChecked = binding.mcvHundredPercent.isChecked.not()
            binding.mcvOther.isChecked = false
        }

        binding.mcvOther.setOnClickListener {
            binding.mcvOther.isChecked = binding.mcvOther.isChecked.not()
            binding.mcvHundredPercent.isChecked = false
        }
        binding.mcvDueDate.setOnClickListener {
            datePicker.show(childFragmentManager, "choose resign date")
        }
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.time = Date(it)
            val date = convertToSqlDate(calendar)
            binding.tvDueDate.text = date
        }

        binding.mcvGoldGemGm.setOnClickListener {
            if (binding.edtGoldGm.text!!.isNotEmpty() && binding.edtGemGm.text!!.isNotEmpty()) {
                binding.tvGoldAndGemGm.text =
                    (binding.edtGoldGm.text.toString().toDouble() + binding.edtGemGm.text.toString()
                        .toDouble()).toString()
            }
        }

        binding.includeSampleTakeSection.switch1.setOnCheckedChangeListener { compoundButton, b ->
            binding.includeSampleTakeSection.outsideGroup.isVisible = b
            binding.includeSampleTakeSection.tilScanHere.isVisible = !b
        }
        binding.includeSampleTakeSection.ivOutside.setOnClickListener {
            if (isReadExternalStoragePermissionGranted()) {
                chooseImage()
            } else {
                requestPermission()
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
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    loadingDialog.dismiss()
                }
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
                Toast.makeText(requireContext(), "Please Fill Required Data", Toast.LENGTH_LONG).show()
            }

        }

        binding.btnConfirm.setOnClickListener {
            if (
                sharedViewModel.selectedGoldSmith.value.isNullOrEmpty() ||
                sharedViewModel.selectedGoldBoxId.value.isNullOrEmpty()
            ) {
                Toast.makeText(requireContext(), "select goldsmith and goldbox", Toast.LENGTH_LONG)
                    .show()
            } else {
                val goldSmith = sharedViewModel.selectedGoldSmith.value!!
                val orderItem = binding.edtOrderItemName.text.toString()
                val orderQty = binding.edtOrderQty.text.toString()
                val weightK = binding.edtK.text.toString()
                val weightP = binding.edtP.text.toString()
                val weightY = binding.edtY.text.toString()
                val goldGm = binding.edtGoldGm.text.toString()
                val gemGm = binding.edtGemGm.text.toString()
                val goldAndGemGm = binding.tvGoldAndGemGm.text.toString()
                val goldBox = sharedViewModel.selectedGoldBoxId.value!!
                val wastageK = binding.edtK2.text.toString()
                val wastageP = binding.edtP2.text.toString()
                val wastageY = binding.edtY2.text.toString()
                val dueDate = binding.tvDueDate.text.toString()
                val sampleList = if (viewModel.sampleList.isNullOrEmpty().not()) viewModel.sampleList.map { it.sampleId!! } else null
                viewModel.giveGold(
                    goldSmith,
                    orderItem,
                    orderQty,
                    weightK,
                    weightP,
                    weightY,
                    goldBox,
                    goldGm,
                    gemGm,
                    goldAndGemGm,
                    wastageK,
                    wastageP,
                    wastageY,
                    dueDate,
                    sampleList
                )
            }
        }
    }

//    fun bindCachedData() {
//        binding.edtOrderItemName.setText(sharedViewModel.orderItem.value ?: "")
//        binding.edtOrderQty.setText(sharedViewModel.orderQty.value ?: "")
//        binding.edtK.setText(sharedViewModel.weightK.value ?: "")
//        binding.edtP.setText(sharedViewModel.weightP.value ?: "")
//        binding.edtY.setText(sharedViewModel.weightY.value ?: "")
//        binding.edtGoldGm.setText(sharedViewModel.goldWeight.value ?: "")
//        binding.edtGemGm.setText(sharedViewModel.gemWeight.value ?: "")
//        binding.tvGoldAndGemGm.text = sharedViewModel.goldAndGemWeight.value ?: ""
//        binding.edtK2.setText(sharedViewModel.wastageK.value ?: "")
//        binding.edtP2.setText(sharedViewModel.wastageP.value ?: "")
//        binding.edtY2.setText(sharedViewModel.wastageY.value ?: "")
//        binding.tvDueDate.text = sharedViewModel.dueDate.value ?: ""
//    }

    fun showServiceChargeDialogForGoldGive() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater: LayoutInflater = LayoutInflater.from(builder.context)
        alertDialogBinding = ServiceChargeDialogBinding.inflate(
            inflater, ConstraintLayout(builder.context), false
        )
        alertDialogBinding.tilInvoiceScan.setEndIconOnClickListener {
            scanQrCode(requireContext(),barLauncher)
        }
        builder.setView(alertDialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialogBinding.btnGive.setOnClickListener {
            viewModel.serviceCharge(
                alertDialogBinding.edtInvoiceNumber.text.toString(),
                alertDialogBinding.edtWastageWeightInGm.text.toString(),
                alertDialogBinding.edtChargeAmount.text.toString()
            )
            alertDialog.dismiss()
        }
        alertDialogBinding.ivCross.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
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


fun convertToSqlDate(calendar: Calendar): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)
}
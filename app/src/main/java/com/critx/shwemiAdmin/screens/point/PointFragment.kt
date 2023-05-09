package com.critx.shwemiAdmin.screens.point

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentPointBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.flashsale.FlashSaleViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class PointFragment:Fragment() {
    private lateinit var binding:FragmentPointBinding
    private val viewModel by viewModels<PointsViewModel>()
    private lateinit var barlauncer:Any
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private var customerId:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())
        return FragmentPointBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.point)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        barlauncer = this.getBarLauncherTest(requireContext()) {
            binding.edtScanHere.setText(it)
            viewModel.userScan(it)
        }
        binding.edtScanHere.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    viewModel.userScan(binding.edtScanHere.text.toString())
                    hideKeyboard(activity, binding.edtScanHere)
                    return true
                }
                return false
            }
        })
        binding.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(),barlauncer)
        }

        binding.btnPlus.setOnClickListener {
            viewModel.manualPointsAddorReduce(
                customerId.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                binding.edtPointAmount.text.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                binding.edtReason.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "add".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            )
        }
        binding.btnMinus.setOnClickListener {
            viewModel.manualPointsAddorReduce(
                customerId.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                binding.edtPointAmount.text.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                binding.edtReason.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "reduce".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            )
        }

        viewModel.userScanLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    customerId = it.data!!.id
                    viewModel.getUserPoints(it.data!!.id)
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

        viewModel.getUserPointsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.edtCustomerName.setText(it.data?.user_name.orEmpty())
                    binding.edtTotalPoint.setText(it.data?.total_point.orEmpty())
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
        viewModel.manualPointsAddorReduceLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()) {
                       clearData()
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
    fun clearData(){
        binding.edtScanHere.text?.clear()
        binding.edtCustomerName.text?.clear()
        binding.edtTotalPoint.text?.clear()
        binding.edtPointAmount.text?.clear()
        binding.edtReason.text?.clear()
        customerId = ""
    }
}
package com.critx.shwemiAdmin.screens.confirmVoucher

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
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentConfirmVoucherBinding
import com.critx.shwemiAdmin.getGramFromYwae
import com.critx.shwemiAdmin.getYwaeFromGram
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.discount.DiscountViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.edit.CREATEED_GROUP_ID
import com.critx.shwemiAdmin.uiModel.asUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmVoucherFragment:Fragment() {
    private lateinit var binding: FragmentConfirmVoucherBinding
    private val viewModel by viewModels<ConfirmVoucherViewModel>()
    private lateinit var barlauncer:Any
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private var voucherCode = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentConfirmVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){
        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.confirm_voucher)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible=true
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_edit))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        toolbarsetup()

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
        binding.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarEndIcon.setOnClickListener {
            findNavController().navigate(ConfirmVoucherFragmentDirections.actionConfirmVoucherFragmentToUnConfirmVoucherFragment())
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.confirmVoucher(voucherCode)
        }

        //come from unconfirmed voucher
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            "selected-voucher-code"
        )
            ?.observe(viewLifecycleOwner) {voucherCode->
                binding.edtScanHere.setText(voucherCode)
                viewModel.scanStock(voucherCode)
            }

        viewModel.scanVoucherLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.edtScannedInvoice.setText(it.data?.total_cost)
                    binding.edtOldInvoice.setText(it.data?.old_voucher_paid_amount)
                    binding.edtDeposit.setText(it.data?.paid_amount)
                    binding.edtBalance.setText(it.data?.remaining_amount)
                    binding.edtRebuyGold.setText(getGramFromYwae(it.data?.gold_gem_weight_ywae.let {
                        if (it.isNullOrEmpty()) 0.0 else it.toDouble()
                    }).toString())
                    voucherCode = it.data?.code.orEmpty()
                    viewModel.getStocksInVoucher(voucherCode)
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

        viewModel.confirmVoucherLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()){
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

        viewModel.getStocksInVoucherLiveData.observe(viewLifecycleOwner) {result->
            when (result) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.btnStockInVoucher.setOnClickListener {
                        findNavController().navigate(ConfirmVoucherFragmentDirections.actionConfirmVoucherFragmentToStockInVoucherFragment(
                            result.data?.map { it.asUiModel() }.orEmpty().toTypedArray()
                        ))
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        result.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }

    }
    fun clearData(){
        voucherCode = ""
        binding.edtScannedInvoice.text?.clear()
        binding.edtOldInvoice.text?.clear()
        binding.edtDeposit.text?.clear()
        binding.edtBalance.text?.clear()
        binding.edtRebuyGold.text?.clear()
    }
}
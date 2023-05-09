package com.critx.shwemiAdmin.screens.discount

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentDiscountBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.flashsale.FlashSaleViewModel
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscountFragment : Fragment() {
    private lateinit var binding: FragmentDiscountBinding
    private lateinit var barlauncer: Any
    private val viewModel by viewModels<DiscountViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())

        return FragmentDiscountBinding.inflate(inflater).also {
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
        toolbarCenterText.text = getString(R.string.discount)
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
        binding.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }
        binding.layoutBtnGroup.tilTitle.isVisible = false
        val adapter = DiscountRecyclerAdapter {
            viewModel.removeStockCode(it)
        }
        binding.mcvScanHere.setOnClickListener {
            scanQrCode(requireContext(), barlauncer)

        }
        binding.layoutDiscount.rvStockCodeList.adapter = adapter


        viewModel.scanDiscountVoucherLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    //id list
                    val resultItem = DiscountVoucherScanDomain(
                        it.data!!.id,
                        binding.edtScanHere.text.toString(),
                        )
                    if (viewModel.voucherCodesList.contains(resultItem).not()) {
                        viewModel.addStockCode(resultItem)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Voucher Already Scanned",
                            Toast.LENGTH_LONG
                        )
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
        viewModel.addDiscountLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()) {
                        viewModel.resetStockCodes()
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

        viewModel.voucherCodesLiveData.observe(viewLifecycleOwner) {
            binding.layoutBtnGroup.btnApprove.isEnabled = it.size > 0
            binding.layoutDiscount.tvTotalScannedStockCount.text = it.size.toString()
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.edtScanHere.text?.clear()
        }

        binding.layoutBtnGroup.btnApprove.setOnClickListener {
            viewModel.addDiscount(
                viewModel.voucherCodesList.map { it.id },
                binding.layoutBtnGroup.tieDiscountAmount.text.toString()
            )
        }

    }
}

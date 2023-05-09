package com.critx.shwemiAdmin.screens.confirmVoucher.unconfirmVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.databinding.FragmentUnconfirmedVoucherBinding
import com.critx.shwemiAdmin.screens.arrangeBox.ArrangeBoxViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.edit.CREATEED_GROUP_ID
import com.critx.shwemiAdmin.uiModel.UnConfirmVoucherUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnConfirmVoucherFragment : Fragment() {
    private lateinit var binding: FragmentUnconfirmedVoucherBinding
    private lateinit var loadingDialog: AlertDialog
    private val viewModel by viewModels<UnConfirmVoucherViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentUnconfirmedVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getVouchers("sale")
        binding.radioGpVoucher.setOnCheckedChangeListener { radioGroup, checkedId ->
            if (checkedId == binding.radioBtnSaleVoucher.id){
                viewModel.getVouchers("sale")
            }else{
                viewModel.getVouchers("pawn")
            }
        }
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val adapter = UnConfirmVoucherRecyclerAdapter{
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "selected-voucher-code",
                    it
            )
            findNavController().popBackStack()
        }
        binding.rvUnconfirmVoucher.adapter = adapter
        viewModel.getUnConfirmVoucherLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }

                is Resource.Success -> {
                    loadingDialog.dismiss()
                    adapter.submitList(it.data)

                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}
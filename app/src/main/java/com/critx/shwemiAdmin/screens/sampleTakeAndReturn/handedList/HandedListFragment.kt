package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.handedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.databinding.FragmentHandedListBinding
import com.critx.shwemiAdmin.databinding.FragmentInventoryBinding
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.GIVE_GOLD_STATE
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.ORDER_STOCK_STATE
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.SampleTakeAndReturnViewModel
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory.InventoryViewModel
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory.NewSampleRecyclerAdapter
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HandedListFragment : Fragment() {
    private lateinit var binding: FragmentHandedListBinding
    private lateinit var barlauncer: Any
    private val viewModel by viewModels<SampleTakeAndReturnViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private val sharedViewModel by activityViewModels<SharedViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentHandedListBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getHandedList()
        val adapter = HandedListRecyclerAdapter{
            viewModel.removeHandedList(it.id)
        }
        binding.rvHandedList.adapter = adapter
        viewModel.getHandedListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    adapter.submitList(it.data)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.removeHandedListItemLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    viewModel.getHandedList()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnConfirm.isEnabled =
            sharedViewModel.sampleTakeScreenUIState == GIVE_GOLD_STATE || sharedViewModel.sampleTakeScreenUIState == ORDER_STOCK_STATE

        binding.btnConfirm.setOnClickListener {
            if(sharedViewModel.sampleTakeScreenUIState == GIVE_GOLD_STATE){
                findNavController().navigate(
                    HandedListFragmentDirections.actionHandedListFragmentToGiveGoldFragment().setSampleList( adapter.currentList.map { it.id }.toTypedArray())
                )
            }else{
                findNavController().navigate(
                    HandedListFragmentDirections.actionHandedListFragmentToFillOrderInfoFragment().setSampleList( adapter.currentList.map { it.id }.toTypedArray())
                )
            }

        }
    }
}
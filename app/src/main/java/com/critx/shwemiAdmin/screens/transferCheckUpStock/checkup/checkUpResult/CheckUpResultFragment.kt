package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.checkUpResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.shwemiAdmin.databinding.FragmentCheckUpBinding
import com.critx.shwemiAdmin.databinding.FragmentCheckUpResultBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter

class CheckUpResultFragment: Fragment() {
    private lateinit var binding: FragmentCheckUpResultBinding
    private val args by navArgs<CheckUpResultFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCheckUpResultBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CheckUpResultRecyclerAdapter{

        }
        binding.layoutScannedStockList.rvStockCodeList.adapter = adapter
        adapter.submitList(args.checkUpStocks.notFromBox)
        binding.chiptGp.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.contains(binding.chipRequired.id)){
                adapter.submitList(args.checkUpStocks.required)
            }else{
                adapter.submitList(args.checkUpStocks.notFromBox)
            }
        }
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
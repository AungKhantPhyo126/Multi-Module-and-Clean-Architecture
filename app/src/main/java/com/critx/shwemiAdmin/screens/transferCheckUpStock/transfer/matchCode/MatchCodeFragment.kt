package com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer.matchCode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.getBarLauncherTestRfid
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.databinding.FragmentMatchCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchCodeFragment :Fragment() {
    private lateinit var binding:FragmentMatchCodeBinding
    private val args by navArgs<MatchCodeFragmentArgs>()
    private val viewModel by viewModels<MatchCodeViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private lateinit var barlauncher:Any


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMatchCodeBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()

        barlauncher = this.getBarLauncherTest(requireContext()){
           viewModel.rfidScanCacheLiveData.value = it
        }
        val adapter = MatchCodeRecyclerAdapter(viewModel) {
            scanQrCode(requireContext(),barlauncher)
            viewModel.rfidScanPosition = it
        }
        viewModel.rfidScanCacheLiveData.observe(viewLifecycleOwner){
            if (viewModel.rfidScanPosition != null){
                viewModel.rfidCodeList[viewModel.rfidScanPosition!!]=it
                adapter.notifyDataSetChanged()
            }
        }
        binding.rvMatchCode.adapter = adapter
//        val dummyList =listOf(
//            "smd 1","smd 2","smd 3",
//            "smd 4","smd 5","smd 6",
//            "smd 7","smd 8","smd 9",
//            "smd 10","smd 11","smd 12",
//            "smd 13","smd 14","smd 15",
//            "smd 16","smd 17","smd 18",
//        )
        repeat(args.stockCodeList.toList().size) {
            viewModel.rfidCodeList.add("")
        }
        adapter.submitList(args.stockCodeList.toList())

        viewModel.transferLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!){
                        findNavController().popBackStack()
                    }
                    viewModel.resetTransferLiveData()
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        binding.btnTransfer.setOnClickListener {
            Log.i("checkRfid",viewModel.rfidCodeList.toString())

            val rfidHashMap: HashMap<String, String> = HashMap()

            repeat(args.stockCodeList.toList().size) {
                rfidHashMap["rfid_code[${args.stockCodeIdList.toList()[it]}]"]=viewModel.rfidCodeList[it]
            }
            viewModel.transferStock(args.boxCode,args.stockCodeIdList.toList(),rfidHashMap)
        }
    }
}
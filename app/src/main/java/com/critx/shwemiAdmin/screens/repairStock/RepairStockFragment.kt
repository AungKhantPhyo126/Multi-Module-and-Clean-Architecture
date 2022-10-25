package com.critx.shwemiAdmin.screens.repairStock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentRepairStockBinding
import com.critx.shwemiAdmin.databinding.ServiceChargeDialogBinding
import com.critx.shwemiAdmin.screens.giveGold.showServiceChargeDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class RepairStockFragment:Fragment() {
    private lateinit var binding: FragmentRepairStockBinding
    private val viewModel by viewModels<RepairStockViewModel>()
    private lateinit var loadingDialog: AlertDialog
    var checkedGoldSmith = ""


    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.repair_stock)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentRepairStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getGoldSmithList()
        binding.btnAssignGs.setOnClickListener {
            findNavController().navigate(RepairStockFragmentDirections.actionRepairStockFragmentToAssignGsFragment(checkedGoldSmith))
        }
        val adapter = RepairStockJobRecyclerAdapter()
        binding.rvJob.adapter=adapter

        binding.chipGroupGs.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedGoldSmith = checkedIds[0].toString()
            viewModel.getJobDone(checkedIds[0].toString())
        }

        binding.mcvServiceGs.setOnClickListener {
            if (checkedGoldSmith.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Select A goldSmith First",Toast.LENGTH_LONG).show()
            }else{
                requireContext().showServiceChargeOnlyDialog {
                    viewModel.chargeRepairStock(it.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
                }
            }
        }

        viewModel.chargeRepairStockLivedata.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                   requireContext().showSuccessDialog(it.data!!){
                       viewModel.getJobDone(checkedGoldSmith)
                       viewModel.resetChargeRepairStockLiveData()
                   }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.jobDoneLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    binding.tvJobDoneValue.text = it.data!!.number_of_job_done
                    adapter.submitList(it.data!!.data)
                    viewModel.resetJobDoneLiveData()
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.goldSmithListLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        binding.chipGroupGs.addView(chip)
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}

fun Context.showServiceChargeOnlyDialog(onClick: (amount:String) -> Unit) {
    val builder = MaterialAlertDialogBuilder(this)
    val inflater: LayoutInflater = LayoutInflater.from(builder.context)
    val alertDialogBinding = ServiceChargeDialogBinding.inflate(
        inflater, ConstraintLayout(builder.context), false
    )
    alertDialogBinding.tilWastageWeightInGm.isVisible = false
    alertDialogBinding.btnGive.text = "OK"
    builder.setView(alertDialogBinding.root)
    val alertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialogBinding.btnGive.setOnClickListener {
        alertDialog.dismiss()
        onClick(alertDialogBinding.edtChargeAmount.text.toString())
    }
    alertDialogBinding.ivCross.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}
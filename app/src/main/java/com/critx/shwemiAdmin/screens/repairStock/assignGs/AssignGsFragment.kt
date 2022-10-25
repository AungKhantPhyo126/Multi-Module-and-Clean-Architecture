package com.critx.shwemiAdmin.screens.repairStock.assignGs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentAssignGoldsmithBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class AssignGsFragment :Fragment(){
    private lateinit var binding: FragmentAssignGoldsmithBinding
    private val viewModel by viewModels<AssignGsViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private val args by navArgs<AssignGsFragmentArgs>()
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text="Assign Goldsmith"
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAssignGoldsmithBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog=requireContext().getAlertDialog()
        viewModel.getJewelleryType()
        viewModel.jewelleryTypeLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    binding.chipGpJewelleryItem.removeAllViews()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        binding.chipGpJewelleryItem.addView(chip)
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.assignGoldSmithLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!){
                        viewModel.resetAssignGoldSmithLiveData()
                        findNavController().popBackStack()
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.repairJobsLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    binding.chipGpJewelleryItem.removeAllViews()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        binding.chipGpJewelleryItem.addView(chip)
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.selectedRepairJob.observe(viewLifecycleOwner){
            binding.chipScrollView.isVisible = it.isNullOrEmpty()
            binding.assignView.isVisible = it.isNotEmpty()
            if (it.isNullOrEmpty() && viewModel.selectedJewelleryType.value!!.isNotEmpty()){

            }else{
                binding.btnAssignGs.text = "Assign GoldSmith"
                binding.viewSecondLine.setBackgroundColor(requireContext().getColor(R.color.primary_color))
                binding.tvLabelThird.background =requireContext().getDrawable(R.drawable.circle_bg_text)
            }
        }

        viewModel.selectedJewelleryType.observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()){
                binding.tvLabelSecond.background = requireContext().getDrawable(R.drawable.circle_bg_grey)
            }else{
                binding.btnAssignGs.text = "Step 2"
                viewModel.getRepairJobs("4")
                binding.tvLabelSecond.background =requireContext().getDrawable(R.drawable.circle_bg_text)
            }
        }

        var checkedChip = ""

        binding.chipGpJewelleryItem.setOnCheckedStateChangeListener { group, checkedIds ->
            for (index in 0 until group.childCount){
                val chip = group[index] as Chip
                if (chip.isChecked){
                    checkedChip = chip.id.toString()
//                    viewModel.setJewelleryType(chip.id.toString())
                }
            }
        }
        binding.btnAssignGs.setOnClickListener {
            if (binding.btnAssignGs.text.toString() == "Step 1"){
                viewModel.setJewelleryType(checkedChip)
            }else if (binding.btnAssignGs.text.toString() == "Step 2"){
                viewModel.setRepairJob(checkedChip)
            }else{
                val itemType = viewModel.selectedJewelleryType.value!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val repairJob = viewModel.selectedRepairJob.value!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val weight = binding.edtWeight.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val quantity = binding.edtQty.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val goldSmith = args.goldSmith.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                viewModel.assignGoldSmith(goldSmith,itemType, repairJob, weight, quantity)
            }
        }

    }
}
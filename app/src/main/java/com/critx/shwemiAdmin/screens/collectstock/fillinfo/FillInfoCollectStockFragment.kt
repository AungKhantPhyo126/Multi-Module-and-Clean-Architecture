package com.critx.shwemiAdmin.screens.collectstock.fillinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCollectStockBinding
import com.critx.shwemiAdmin.databinding.FragmentFillInfoCollectStockBinding
import com.critx.shwemiAdmin.getYwaeFromKPY
import com.critx.shwemiAdmin.showDropdown
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class FillInfoCollectStockFragment : Fragment() {
    private lateinit var binding: FragmentFillInfoCollectStockBinding
    private val args by navArgs<FillInfoCollectStockFragmentArgs>()
    private val viewModel by viewModels<FillInfoCollectStockViewModel>()
    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFillInfoCollectStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    fun makeEnableTil(view: TextInputLayout, isChecked: Boolean) {
        view.isEnabled = isChecked
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        binding.cbWastage.setOnCheckedChangeListener { compoundButton, ischecked ->
            makeEnableTil(binding.tilK, ischecked)
            makeEnableTil(binding.tilP, ischecked)
            makeEnableTil(binding.tilY, ischecked)
        }
        binding.cbGoldsmith.setOnCheckedChangeListener { compoundButton, ischecked ->
            if (ischecked){
                viewModel.getGoldSmithList()
            }
            makeEnableTil(binding.tilChooseOneGoldSmith, ischecked)
        }
        binding.cbBonus.setOnCheckedChangeListener { compoundButton, ischecked ->
            makeEnableTil(binding.tilEnterBonusAmount, ischecked)
        }
        binding.cbSize.setOnCheckedChangeListener { compoundButton, ischecked ->
            binding.rvJewellerySize.isVisible = ischecked
        }
        val adapter = SizeRecyclerAdapter {
            viewModel.selectSize(it.id)
        }


        binding.rvJewellerySize.adapter = adapter

        viewModel.goldSmithListLiveData.observe(viewLifecycleOwner){

            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
//                    it.data!!.map { it.name }
                    val list = it.data!!.map { it.name }
                    val arrayAdapter = ArrayAdapter(requireContext(),R.layout.item_drop_down_text,list)
                    binding.edtChooseOneGoldSmith.setAdapter(arrayAdapter)
                    binding.edtChooseOneGoldSmith.setText(list[0],false)
                    binding.edtChooseOneGoldSmith.addTextChangedListener {editable->
                        viewModel.selectedGoldSmith = it.data!!.find {
                            it.name==binding.edtChooseOneGoldSmith.text.toString()
                        }?.id
                    }
                    binding.edtChooseOneGoldSmith.setOnClickListener {
                        binding.edtChooseOneGoldSmith.showDropdown(arrayAdapter)
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }
        }



        viewModel.jewellerySizeLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    viewModel.selectedSize = it.data?.find { it.isChecked }?.id
                    adapter.submitList(it.data)
                    adapter.notifyDataSetChanged()
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.collectBatchLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                   requireContext().showSuccessDialog("Stock Data Given"){
                       findNavController().popBackStack()
                   }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                }
            }
        }

        binding.cbSize.isVisible = args.jewelleryType != null

        if (args.jewelleryType != null) {
            viewModel.getJewellerySize(args.jewelleryType!!)
        }

        binding.btnConfirm.setOnClickListener {
            collectBatch()
        }
    }

    fun collectBatch() {
        val kyat = if (binding.edtK.text.isNullOrEmpty()) {
            0
        } else binding.edtK.text.toString().toInt()

        val pae = if (binding.edtP.text.isNullOrEmpty()) {
            0
        } else binding.edtP.text.toString().toInt()

        val ywaeNumber = if (binding.edtY.text.isNullOrEmpty()) {
            0.0
        } else binding.edtY.text.toString().toDouble()

        val ywaeFromKpy = getYwaeFromKPY(kyat,
            pae,ywaeNumber)

        val ywae: RequestBody? = if (binding.edtY.text.isNullOrEmpty()) {
            null
        } else ywaeFromKpy.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val goldSmith:  RequestBody? = if (viewModel.selectedGoldSmith.isNullOrEmpty()) {
            null
        } else viewModel.selectedGoldSmith.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val bonus : RequestBody? = if (binding.edtEnterBonusAmount.text.isNullOrEmpty()) {
            null
        } else binding.edtEnterBonusAmount.text.toString()
        .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val size : RequestBody? = if (viewModel.selectedSize.isNullOrEmpty()) {
            null
        } else viewModel.selectedSize.toString()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val productIds = mutableListOf<RequestBody>()
        args.productIdList.forEach {
            productIds.add(
                it.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }

        viewModel.collectBatch(
            ywae,
            goldSmith,
            bonus,
            size,
            productIds
        )
    }
}
package com.critx.shwemiAdmin.screens.orderStock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentOrderStockBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.showDropdown
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderStockFragment:Fragment() {
    private lateinit var binding:FragmentOrderStockBinding
    private val viewModel by viewModels<OrderStockViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var adapter :StockToOrderRecyclerAdapter
    private lateinit var loadingDialog: AlertDialog
    private var selectedJewelleryType:String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      return FragmentOrderStockBinding.inflate(inflater).also {
          binding = it
      }.root
    }

    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.order_stock)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.paper_icon))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getJewelleryType()
        binding.tvEmptyList.isVisible = true
        adapter = StockToOrderRecyclerAdapter {
            findNavController().navigate(
                OrderStockFragmentDirections.actionOrderStockFragmentToFillOrderInfoFragment(
                    it
                )
            )
        }
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView

        toolbarEndIcon.setOnClickListener {
            findNavController().navigate(OrderStockFragmentDirections.actionOrderStockFragmentToNewOrderInfoFragment(
                selectedJewelleryType!!
            ))
        }
        binding.rvStockToOrder.adapter=adapter
        viewModel.jewelleryTypeLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    val list = it.data!!.map { it.name }
                    val arrayAdapter = ArrayAdapter(requireContext(),R.layout.item_drop_down_text,list)
                    binding.actJewelleryType.addTextChangedListener {editable->
                         selectedJewelleryType = it.data!!.find {
                            it.name==binding.actJewelleryType.text.toString()
                        }?.id
                        loadBookMarks(selectedJewelleryType.orEmpty(),"0")
                    }
                    binding.actJewelleryType.setAdapter(arrayAdapter)
                    binding.actJewelleryType.setText(list[0],false)
                    binding.actJewelleryType.setOnClickListener {
                        binding.actJewelleryType.showDropdown(arrayAdapter)
                    }
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        adapter.addLoadStateListener {loadState->
            if ( loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached )
            {
                binding.tvEmptyList.isVisible = adapter.itemCount < 1
            }

        }

        binding.btnRetrieveFromGs.setOnClickListener {
            binding.tvEmptyList.isVisible = false
            loadBookMarks(selectedJewelleryType.orEmpty(),"1")
        }
    }
    private fun loadBookMarks(jewelleryType:String,isItemFromGs:String) {
        viewModel.getBookMarks(jewelleryType,isItemFromGs).observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }

}
package com.critx.shwemiAdmin.screens.orderStock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentOrderStockBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class OrderStockFragment:Fragment() {
    private lateinit var binding:FragmentOrderStockBinding

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
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        binding.tvEmptyList.isVisible = true
        val adapter = StockToOrderRecyclerAdapter({
           findNavController().navigate(OrderStockFragmentDirections.actionOrderStockFragmentToFillOrderInfoFragment())
        },{})
        binding.rvStockToOrder.adapter=adapter
        binding.btnRetrieveFromGs.setOnClickListener {
            binding.tvEmptyList.isVisible = false
            adapter.submitList(listOf(
                StockCodeForListUiModel(
                    "1",
                    "123456788"
                ),
                StockCodeForListUiModel(
                    "2",
                    "123456788"
                ),
                StockCodeForListUiModel(
                    "3",
                    "123456788"
                ),
                StockCodeForListUiModel(
                    "4",
                    "123456788"
                )
            ))
        }
    }

}
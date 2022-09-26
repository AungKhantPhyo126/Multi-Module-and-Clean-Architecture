package com.critx.shwemiAdmin.screens.checkUpBoxWeight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCheckUpBoxWeightBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class CheckUpBoxWeightFragment: Fragment() {
    private lateinit var binding: FragmentCheckUpBoxWeightBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCheckUpBoxWeightBinding.inflate(inflater).also {
            binding=it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.check_box_weight)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        val adapter = CheckUpBoxRecyclerAdapter()
        binding.includeArrangeBoxList.rvArrangeBox.adapter = adapter
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
package com.critx.shwemiAdmin.screens.arrangeBox

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentArrangeBoxBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArrangeBoxFragment:Fragment() {
    private lateinit var binding:FragmentArrangeBoxBinding
    private lateinit var barlauncherBox:Any
    private lateinit var loadingDialog: AlertDialog
    private val viewModel by viewModels<ArrangeBoxViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentArrangeBoxBinding.inflate(inflater).also {
            binding=it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.arrange_box)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        barlauncherBox = this.getBarLauncherTest(requireContext()) { viewModel.getBoxData(it) }
        val adapter = ArrangeBoxRecyclerAdapter{
            viewModel.removeBox(it)
        }
        binding.includeArrangeBoxList.rvArrangeBox.adapter = adapter
        binding.edtBoxCode.setOnKeyListener { view, keyCode, keyevent ->
            //If the keyevent is a key-down event on the "enter" button
            if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Perform your action on key press here
                viewModel.getBoxData(binding.edtBoxCode.text.toString())
                true
            } else false
        }
        viewModel.getBoxDataLive.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    viewModel.addbox(it.data!!)
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.scannedBoxWeightLive.observe(viewLifecycleOwner){
            binding.btnArrange.isEnabled = it.size>0
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.edtBoxCode.text?.clear()
        }
        binding.btnArrange.setOnClickListener {
            viewModel.resetBoxList()
        }
    }
}
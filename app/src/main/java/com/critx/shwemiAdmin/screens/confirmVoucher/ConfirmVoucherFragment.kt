package com.critx.shwemiAdmin.screens.confirmVoucher

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
import com.critx.shwemiAdmin.databinding.FragmentConfirmVoucherBinding

class ConfirmVoucherFragment:Fragment() {
    private lateinit var binding: FragmentConfirmVoucherBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentConfirmVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){
        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.confirm_voucher)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible=true
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_edit))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarEndIcon.setOnClickListener {
            findNavController().navigate(ConfirmVoucherFragmentDirections.actionConfirmVoucherFragmentToUnConfirmVoucherFragment())
        }
    }
}
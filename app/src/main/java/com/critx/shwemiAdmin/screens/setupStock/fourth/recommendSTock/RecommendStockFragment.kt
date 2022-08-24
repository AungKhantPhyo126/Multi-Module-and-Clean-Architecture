package com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentRecommendStockBinding
import com.critx.shwemiAdmin.screens.setupStock.fourth.JewelleryCategoryRecyclerAdapter
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseCategoryViewModel
import com.google.android.material.snackbar.Snackbar

class RecommendStockFragment:Fragment() {
    private lateinit var binding: FragmentRecommendStockBinding
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter: RecommendStockAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentRecommendStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    private fun toolbarsetup() {
        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.reconmend_ahtal)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        adapter = RecommendStockAdapter({

        },{

        })
    }
}
package com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentRecommendStockBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.JewelleryCategoryRecyclerAdapter
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.AddCategoryViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseCategoryViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.edit.CREATEED_GROUP_ID
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecommendStockFragment : Fragment() {
    private lateinit var binding: FragmentRecommendStockBinding
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter: RecommendStockAdapter
    private val viewModel by viewModels<RecommendStockViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val args by navArgs<RecommendStockFragmentArgs>()


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
        loadingDialog = requireContext().getAlertDialog()
        if (findNavController().previousBackStackEntry?.destination?.id == R.id.chooseCategoryFragment) {
            args.categoryUIModel!!.isChecked = false
            sharedViewModel.addRecommendCat(args.categoryUIModel!!)
        }
//        else if (args.categoryUIModel != null){
//            viewModel.getRelatedCategories(args.categoryUIModel!!.id)
//        }
        adapter = RecommendStockAdapter({
            //delete click
            sharedViewModel.removeRecommendCat(it)
        }, {
            //addnew click
            findNavController().navigate(RecommendStockFragmentDirections.actionRecommendStockFragmentToSetupStockFragment())
        })
        binding.rvRecommendStock.adapter = adapter
        sharedViewModel.recommendCatList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()

        }
//        viewModel.recommendCatListLiveData.observe(viewLifecycleOwner){
//            when(it){
//                is Resource.Loading->{
//                    loadingDialog.show()
//                }
//                is Resource.Success->{
//                    loadingDialog.dismiss()
////                    sharedViewModel.addRecommendCatBatch(it.data.orEmpty())
////                    adapter.submitList(it.data)
////                    adapter.notifyDataSetChanged()
//                }
//                is Resource.Error->{
//                    loadingDialog.dismiss()
//                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
//
//                }
//            }
//        }


        binding.btnOk.setOnClickListener {
            findNavController().popBackStack(R.id.addCategoryFragment, false)
        }
    }

}
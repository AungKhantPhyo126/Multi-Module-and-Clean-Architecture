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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.getAlertDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentRecommendStockBinding
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
class RecommendStockFragment:Fragment() {
    private lateinit var binding: FragmentRecommendStockBinding
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter: RecommendStockAdapter
    private val  viewModel by viewModels<RecommendStockViewModel> ()
    private val  sharedViewModel by activityViewModels<AddCategoryViewModel> ()
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
        viewModel.getJewelleryCategory(null,null,null,null)

        adapter = RecommendStockAdapter({
            viewModel.selectImage(it)
            collectSelectedImage()
        },{

        })
        binding.rvRecommendStock.adapter =adapter
        collectData()



        binding.btnOk.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun collectSelectedImage(){
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //getJewelleryGroup
                launch {
                    viewModel.getRecommendCategory.collect {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.successLoading != null) {
                            adapter.submitList(it.successLoading)
                            sharedViewModel.setSelectedRecommendCat(it.successLoading!!.filter {
                                it.isChecked
                            }.map { it.id.toInt() })
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "selected recommend categories",
                                it.successLoading!!.filter {
                                    it.isChecked
                                }.map { it.id.toInt() }
                            )
                            adapter.notifyDataSetChanged()
                        }

                    }
                }
            }
        }
    }

    fun collectData(){
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //getJewelleryGroup
                launch {
                    viewModel.getRecommendCategory.collect {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.successLoading != null) {
                            adapter.submitList(it.successLoading)
                            args.catId?.let {
                                if (sharedViewModel.selectedRecommendCat.value.isNullOrEmpty()){
                                    viewModel.getRelatedCat(it)
                                }else{
                                    sharedViewModel.selectedRecommendCat.value!!.forEach {
                                        viewModel.selectImage(it.toString())
                                    }
                                }
                            }
//                            sharedViewModel.selectedRecommendCat?.addAll(it.successLoading!!.filter {
//                                it.isChecked
//                            }.map { it.id.toInt() })
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "selected recommend categories",
                                it.successLoading!!.filter {
                                    it.isChecked
                                }.map { it.id.toInt() }
                            )
                            adapter.notifyDataSetChanged()
                        }

                    }
                }

                launch {
                    viewModel.getRelatedCats.collectLatest {
                        if(it.getRelatedCatsLoading){
                            loadingDialog.show()
                        }else loadingDialog.dismiss()

                        if (it.getRelatedCatsSuccessLoading != null){
                            it.getRelatedCatsSuccessLoading?.forEach {
                                viewModel.selectImage(it.id)
                                collectSelectedImage()
                            }
                        }
                    }
                }



                launch {
                    viewModel.event.collectLatest { event ->
                        when (event) {
                            is UiEvent.ShowErrorSnackBar -> {
                                snackBar?.dismiss()
                                snackBar = Snackbar.make(
                                    binding.root,
                                    event.message,
                                    Snackbar.LENGTH_LONG
                                )
                                snackBar?.show()
                            }
                        }
                    }
                }
            }
        }
    }

}
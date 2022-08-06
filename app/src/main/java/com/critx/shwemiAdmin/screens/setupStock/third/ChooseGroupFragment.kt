package com.critx.shwemiAdmin.screens.setupStock.third

import android.Manifest
import android.content.pm.PackageManager
import com.critx.shwemiAdmin.screens.setupStock.first.SetupStockFragmentDirections
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.databinding.ChoiceChipBinding
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.*
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseGroupFragment : Fragment() {
    private lateinit var binding: FragmentChooseGroupBinding
    private val args by navArgs<ChooseGroupFragmentArgs>()
    private val viewModel by viewModels<ChooseGroupViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter:ImageRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseGroupBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.set_up_stock)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }
//     object CustomSelection: SelectionTracker.SelectionPredicate<K>() {
//        override fun canSetStateForKey(key: K, nextState: Boolean):
//                Boolean {
//            return true
//        }
//
//        override fun canSetStateAtPosition(position: Int, nextState:
//        Boolean): Boolean {
//            return true
//        }
//
//        override fun canSelectMultiple(): Boolean {
//            return false
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        var frequentUse = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        viewModel.getJewelleryGroup(frequentUse,args.firstCat.id.toInt(),args.secondCat.id.toInt())
        binding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, ischecked ->
             frequentUse = if (ischecked) 1 else 0
            viewModel.getJewelleryGroup(frequentUse,args.firstCat.id.toInt(),args.secondCat.id.toInt())
        }
        loadingDialog = requireContext().getAlertDialog()
        binding.tvFirstCat.text = args.firstCat.name
        binding.tvSecondCat.text = args.secondCat.name
        setupRecyclerImage()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //getJewelleryGroup
                launch {
                    viewModel.getGroupState.collectLatest {
                        if (it.loading){
                            loadingDialog.show()
                        }else loadingDialog.dismiss()
                        if (it.successLoading !=null) {
                           adapter.submitList(it.successLoading)
                            setupChipView(it.successLoading.orEmpty())
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

//        viewModel.jewelleryGroupLive.observe(viewLifecycleOwner){
//           viewLifecycleOwner.lifecycleScope.launch{
//               adapter.submitData(it)
//
//               Log.i("gg","ggkahptioethpahwe")
//               adapter.notifyDataSetChanged()
//               adapter.snapshot().size
//
////               binding.mcvAddItem.isVisible = adapter.snapshot().items.isEmpty()
////               setupChipView(adapter.snapshot().items)
//           }
//        }
//       val adapterObserver = object: RecyclerView.AdapterDataObserver(){
//           override fun onChanged() {
//               super.onChanged()
//               setupChipView(adapter.snapshot().items)
//               binding.mcvAddItem.isVisible = adapter.snapshot().items.isEmpty()
//           }
//        }


//        adapter.registerAdapterDataObserver(adapterObserver)


        binding.mcvAddItem.setOnClickListener {
            findNavController().navigate(
                ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                    args.firstCat,
                    args.secondCat
                )
            )
        }

        binding.cbImageView.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.rvImages.visibility = View.VISIBLE
                binding.chipGroupChooseGp.visibility = View.INVISIBLE
            } else {
                binding.rvImages.visibility = View.INVISIBLE
                binding.chipGroupChooseGp.visibility = View.VISIBLE
            }
        }

//
//        val tracker = SelectionTracker.Builder(
//            "mySelection",
//            binding.rvImages,
//            MyItemKeysProvider(binding.rvImages),
//            ItemsDetailsLookup(binding.rvImages),
//            StorageStrategy.createLongStorage()
//        ).withSelectionPredicate(
//            SelectionPredicates.createSelectSingleAnything()
//        ).build()
//        adapter.tracker=tracker
//


    }

    fun setupRecyclerImage() {
        adapter = ImageRecyclerAdapter({
            viewModel.selectImage(it)
        }, {
            findNavController().navigate(
                ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                    args.firstCat,
                    args.secondCat
                )
            )
        }, {
            findNavController().navigate(
                ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                    args.firstCat,
                    args.secondCat
                )
            )
        })
        binding.rvImages.adapter = adapter
    }

    fun setupChipView(list: List<ChooseGroupUIModel>) {
        binding.chipGroupChooseGp.removeAllViews()
        for (item in list.toSet()) {
            val chip = requireContext().createChip(item.name)
            val bubble = BubbleCardBinding.inflate(layoutInflater).root
            val editView = bubble.findViewById<ImageView>(R.id.iv_edit)

            val popupWindow: PopupWindow = BubblePopupHelper.create(requireContext(), bubble)
            popupWindow.width = 300
            popupWindow.height = 200
            chip.id = item.id.toInt()
            chip.setOnLongClickListener {
                val location = IntArray(2)
                chip.getLocationInWindow(location)
                bubble.arrowDirection = ArrowDirection.BOTTOM
                popupWindow.showAtLocation(
                    chip,
                    Gravity.NO_GRAVITY,
                    location[0],
                    location[1] - chip.height
                )
                return@setOnLongClickListener true
            }
            editView.setOnClickListener {
                popupWindow.dismiss()
                findNavController().navigate(
                    ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                        args.firstCat,
                        args.secondCat
                    )
                )
            }
//            val chip = ItemImageSelectionBinding.inflate(layoutInflater).root
                binding.chipGroupChooseGp.addView(chip)
        }
        val addChipView = requireContext().createChip("Add New")
        addChipView.chipIcon = requireContext().getDrawable(R.drawable.ic_plus)
        addChipView.isCheckable = false
        addChipView.isChipIconVisible = true
        addChipView.setTextColor( requireContext().getColorStateList(R.color.primary_color))
        addChipView.chipIconTint = requireContext().getColorStateList(R.color.primary_color)
        binding.chipGroupChooseGp.addView(addChipView)


        addChipView.setOnClickListener{
            findNavController().navigate(
                ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                    args.firstCat,
                    args.secondCat
                )
            )
        }


        binding.chipGroupChooseGp.setOnCheckedStateChangeListener { group, checkedIds ->
            for (index in 0 until group.childCount) {
                val chip = group[index] as Chip
                if (chip.isChecked) {
                    binding.tvThirdCat.isVisible = true
                    binding.tvThirdCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvThirdCat.text = chip.text
                    binding.ivThirdCat.isVisible = true
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}

data class CustomChip(
    val id: String,
    val name: String
)
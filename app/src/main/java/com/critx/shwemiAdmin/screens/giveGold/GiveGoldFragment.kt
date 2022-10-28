package com.critx.shwemiAdmin.screens.giveGold

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentGiveGoldBinding
import com.critx.shwemiAdmin.databinding.ServiceChargeDialogBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.showDropdown
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class GiveGoldFragment : Fragment() {
    private lateinit var binding: FragmentGiveGoldBinding
    private val viewModel by viewModels<GiveGoldViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private lateinit var datePicker: MaterialDatePicker<Long>
    private val args by navArgs<GiveGoldFragmentArgs>()

    var selectedGoldBoxId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.choose_due_date)
            .setSelection(Calendar.getInstance().timeInMillis)
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentGiveGoldBinding.inflate(inflater).also {
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
        toolbarCenterText.text = getString(R.string.give_gold)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getGoldSmithList()
        viewModel.getGoldBoxId()
        if (args.sampleList.isNullOrEmpty()){
            binding.btnSampleTake.setTextColor(requireContext().getColorStateList(R.color.edit_text_color))
        }else {
            binding.btnSampleTake.setTextColor(requireContext().getColorStateList(R.color.primary_color))
        }

        binding.btnSampleTake.setOnClickListener {
            findNavController().navigate(GiveGoldFragmentDirections.actionGiveGoldFragmentToSampleTakeAndReturnFragment())
        }
        viewModel.giveGoldLiveData.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {
                        viewModel.resetGiveGoldLiveData()
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }


        viewModel.goldSmithListLiveData.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
//                    it.data!!.map { it.name }
                    val list = it.data!!.map { it.name }
                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.item_drop_down_text, list)
                    binding.actGoldSmith.setAdapter(arrayAdapter)
                    binding.actGoldSmith.setText(list[0], false)
                    viewModel.selectedGoldSmith = it.data!![0].id
                    binding.actGoldSmith.addTextChangedListener { editable ->
                        viewModel.selectedGoldSmith = it.data!!.find {
                            it.name == binding.actGoldSmith.text.toString()
                        }?.id
                    }
                    binding.actGoldSmith.setOnClickListener {
                        binding.actGoldSmith.showDropdown(arrayAdapter)
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.getGoldBoxLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.mcvHundredPercent.setOnCheckedChangeListener { card, isChecked ->
                        if (isChecked) {
                            selectedGoldBoxId = it.data!!.find { it.name == "100%" }!!.id
                        }
                    }
                    binding.mcvOther.setOnCheckedChangeListener { card, isChecked ->
                        if (isChecked) {
                            selectedGoldBoxId = it.data!!.find { it.name == "Other" }!!.id
                        }
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        binding.mcvHundredPercent.setOnClickListener {
            binding.mcvHundredPercent.isChecked = binding.mcvHundredPercent.isChecked.not()
            binding.mcvOther.isChecked = false
        }

        binding.mcvOther.setOnClickListener {
            binding.mcvOther.isChecked = binding.mcvHundredPercent.isChecked.not()
            binding.mcvHundredPercent.isChecked = false
        }
        binding.mcvDueDate.setOnClickListener {
            datePicker.show(childFragmentManager, "choose resign date")
        }
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.time = Date(it)
            val date = convertToSqlDate(calendar)
            binding.tvDueDate.text = date
        }

        binding.mcvGoldGemGm.setOnClickListener {
            if (binding.edtGoldGm.text!!.isNotEmpty() && binding.edtGemGm.text!!.isNotEmpty()) {
                binding.tvGoldAndGemGm.text =
                    (binding.edtGoldGm.text.toString().toInt() + binding.edtGemGm.text.toString()
                        .toInt()).toString()
            }
        }


        binding.btnConfirm.setOnClickListener {
            if (
                viewModel.selectedGoldSmith.isNullOrEmpty() ||
                selectedGoldBoxId.isEmpty()
            ) {
                Toast.makeText(requireContext(), "select goldsmith and goldbox", Toast.LENGTH_LONG)
                    .show()
            } else {


                val goldSmith = viewModel.selectedGoldSmith!!
                val orderItem = binding.edtOrderItemName.text.toString()
                val orderQty = binding.edtOrderQty.text.toString()
                val weightK = binding.edtK.text.toString()
                val weightP = binding.edtP.text.toString()
                val weightY = binding.edtY.text.toString()
                val goldGm = binding.edtGoldGm.text.toString()
                val gemGm = binding.edtGemGm.text.toString()
                val goldAndGemGm = binding.tvGoldAndGemGm.text.toString()
                val goldBox = selectedGoldBoxId
                val wastageK = binding.edtK2.text.toString()
                val wastageP = binding.edtP2.text.toString()
                val wastageY = binding.edtY2.text.toString()
                val dueDate = binding.tvDueDate.text.toString()
                val sampleList = args.sampleList?.toList()
                viewModel.giveGold(
                    goldSmith,
                    orderItem,
                    orderQty,
                    weightK,
                    weightP,
                    weightY,
                    goldBox,
                    goldGm,
                    gemGm,
                    goldAndGemGm,
                    wastageK,
                    wastageP,
                    wastageY,
                    dueDate,
                    sampleList
                )
            }
        }
    }
}

fun Context.showServiceChargeDialog(onClick: () -> Unit) {
    val builder = MaterialAlertDialogBuilder(this)
    val inflater: LayoutInflater = LayoutInflater.from(builder.context)
    val alertDialogBinding = ServiceChargeDialogBinding.inflate(
        inflater, ConstraintLayout(builder.context), false
    )
    builder.setView(alertDialogBinding.root)
    val alertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialogBinding.btnGive.setOnClickListener {
        alertDialog.dismiss()
        onClick()
    }
    alertDialogBinding.ivCross.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}

fun convertToSqlDate(calendar: Calendar): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)
}
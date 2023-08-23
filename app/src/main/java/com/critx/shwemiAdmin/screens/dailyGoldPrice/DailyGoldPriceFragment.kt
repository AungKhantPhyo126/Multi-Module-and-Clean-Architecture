package com.critx.shwemiAdmin.screens.dailyGoldPrice

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.common.ui.validatePrice
import com.critx.common.ui.validatePriceForWeight
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentDailyGoldPriceBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class DailyGoldPriceFragment : Fragment() {
    private lateinit var binding: FragmentDailyGoldPriceBinding
    private val viewModel by viewModels<DailyGoldPriceViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDailyGoldPriceBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = false
//        toolbarCenterText.text=getString(R.string.daily_gold_price)
        toolbarCenterImage.isVisible = true
        toolbarEndIcon.isVisible = true
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_logout))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        refreshData()
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        viewModel.profileLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.tvLogginedBy.isVisible = true
                    binding.tvUserName.text = it.data!!.name
                    binding.tvTodayDate.text = it.data!!.todayDate
                    binding.tvTodayName.text = it.data!!.todayName
                    viewModel.getGoldPrice()
                    viewModel.resetProfileLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (it.message == "TOKEN_EXPIRED" || it.message == "TOKEN_NOT_PROVIDED" || it.message == "TOKEN_INVALID") {
                        findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    viewModel.resetProfileLiveData()

//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }


        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarEndIcon.setOnClickListener {
            viewModel.logout()
        }
        binding.btnUpdate.setOnClickListener {
            if (
                validatePrice(binding.layoutDailyGoldPriceInput.edtGoldBlock, requireContext()) &&
                validatePrice(binding.layoutDailyGoldPriceInput.edt15pGq, requireContext()) &&
                validatePrice(binding.layoutDailyGoldPriceInput.edt22kGq, requireContext()) &&
                validatePrice(binding.layoutDailyGoldPriceInput.edtDiamond, requireContext()) &&
                validatePriceForWeight(binding.layoutDailyGoldPriceInput.edtWg, requireContext()) &&
                validatePrice(binding.layoutDailyGoldPriceInput.edtRebuy, requireContext()) &&
                validatePrice(binding.layoutDailyGoldPriceInput.edtGoldBlock, requireContext())
            ) {
                val map: HashMap<String, String> = HashMap()
                map["price[1]"] = binding.layoutDailyGoldPriceInput.edtGoldBlock.text.toString()
                map["price[2]"] = binding.layoutDailyGoldPriceInput.edt15pGq.text.toString()
                map["price[3]"] = binding.layoutDailyGoldPriceInput.edt22kGq.text.toString()
                map["price[4]"] = binding.layoutDailyGoldPriceInput.edtDiamond.text.toString()
                map["price[5]"] = binding.layoutDailyGoldPriceInput.edtWg.text.toString()
                map["price[6]"] = binding.layoutDailyGoldPriceInput.edtRebuy.text.toString()
                viewModel.updateGoldPrice(map)
            }

        }

        viewModel.updateGoldLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    requireContext().showSuccessDialog(it.data!!) {
                        viewModel.resetUpdateGoldLive()
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    viewModel.resetUpdateGoldLive()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.logOutLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    viewModel.resetLogOutLiveData()
                    findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    viewModel.resetLogOutLiveData()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        viewModel.getGoldPriceLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.layoutDailyGoldPriceInput.edtGoldBlock.setText(it.data?.get(0)!!.price)
                    binding.layoutDailyGoldPriceInput.edt15pGq.setText(it.data?.get(1)!!.price)
                    binding.layoutDailyGoldPriceInput.edt22kGq.setText(it.data?.get(2)!!.price)
                    binding.layoutDailyGoldPriceInput.edtDiamond.setText(it.data?.get(3)!!.price)
                    binding.layoutDailyGoldPriceInput.edtWg.setText(it.data?.get(4)!!.price)
                    binding.layoutDailyGoldPriceInput.edtRebuy.setText(it.data?.get(5)!!.price)
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.layoutDailyGoldPriceInput.edtGoldBlock.setText("")
                    binding.layoutDailyGoldPriceInput.edt15pGq.setText("")
                    binding.layoutDailyGoldPriceInput.edt22kGq.setText("")
                    binding.layoutDailyGoldPriceInput.edtDiamond.setText("")
                    binding.layoutDailyGoldPriceInput.edtWg.setText("")
                    binding.layoutDailyGoldPriceInput.edtRebuy.setText("")
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //logout
                launch {
                    viewModel.logoutState.collectLatest {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (!it.successMessage.isNullOrEmpty()) {
                            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
                        }
                    }
                }

                //Error Event
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

                //getProfile
//                launch {
//                    viewModel.profileState.collectLatest {
//                        if (it.successLoading != null) {
//                            binding.tvLogginedBy.isVisible = true
//                            binding.tvUserName.text = it.successLoading!!.name
//                            binding.tvTodayDate.text = it.successLoading!!.todayDate
//                            binding.tvTodayName.text = it.successLoading!!.todayName
//                        }else{
//                            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
//                        }
//                    }
//                }
            }
        }


//        if (!viewModel.isLogin()){
//            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
//        }else{
//            if (viewModel.isRefreshTokenExpire()){
//                findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
//            }else{
//                viewModel.getProfile()
//                enqueueRefreshTokenWork()
//            }
//        }
        binding.btnByTable.setOnClickListener {
            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToPriceByTableFragment())
        }
    }

    fun refreshData() {
        viewModel.getProfile()
    }

//    fun addConstantTextInEditText(edt: EditText, text: String?) {
//        edt.setText(text)
//        Selection.setSelection(edt.text, edt.text.length)
//        edt.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                if (!s.toString().contains(text!!)) {
//                    edt.setText(text)
//                    Selection.setSelection(edt.text, edt.text.length)
//                }
//            }
//        })
//    }
}
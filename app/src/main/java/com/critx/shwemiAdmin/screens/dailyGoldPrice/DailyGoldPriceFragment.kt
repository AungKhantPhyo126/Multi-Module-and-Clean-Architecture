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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentDailyGoldPriceBinding
import com.critx.shwemiAdmin.uiModel.dailygoldandprice.asUiModel
import com.critx.shwemiAdmin.workerManager.RefreshTokenWorker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DailyGoldPriceFragment:Fragment() {
    private lateinit var binding:FragmentDailyGoldPriceBinding
    private val viewModel by viewModels<DailyGoldPriceViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    lateinit var workManager:WorkManager
    lateinit var repeatingRequest:PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDailyGoldPriceBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=false
//        toolbarCenterText.text=getString(R.string.daily_gold_price)
        toolbarCenterImage.isVisible =true
        toolbarEndIcon.isVisible = true
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_logout))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        workManager = WorkManager.getInstance(requireContext())
        loadingDialog = requireContext().getAlertDialog()
        viewModel.isloggedIn()

        val workConstraints = Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshTokenWorker>(15, TimeUnit.MINUTES)
                .setConstraints(workConstraints)
                .build()

        workManager.getWorkInfoByIdLiveData(repeatingRequest.id).observe(viewLifecycleOwner) {
            Log.i("refresh success",it.state.toString())
            if (it.state == WorkInfo.State.ENQUEUED){
                viewModel.getProfile()
                Log.i("refresh success","reached")
            }
        }

        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarEndIcon.setOnClickListener {
            viewModel.logout()
        }

        viewModel.getGoldPriceLive.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    binding.layoutDailyGoldPriceInput.edtGoldBlock.setText(it.data?.get(0)!!.price +it.data?.get(0)!!.unit )
                    binding.layoutDailyGoldPriceInput.edt15pGq.setText(it.data?.get(1)!!.price +it.data?.get(1)!!.unit )
                    binding.layoutDailyGoldPriceInput.edt22kGq.setText(it.data?.get(2)!!.price +it.data?.get(2)!!.unit )
                    binding.layoutDailyGoldPriceInput.edtDiamond.setText(it.data?.get(3)!!.price +it.data?.get(3)!!.unit )
                    binding.layoutDailyGoldPriceInput.edtWg.setText(it.data?.get(4)!!.price +it.data?.get(4)!!.unit )
                    binding.layoutDailyGoldPriceInput.edtRebuy.setText(it.data?.get(4)!!.price +it.data?.get(4)!!.unit )
                }
                is Resource.Error->{
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                //logout
                launch {
                    viewModel.logoutState.collectLatest {
                        if (it.loading){
                            loadingDialog.show()
                        }else loadingDialog.dismiss()
                        if (!it.successMessage.isNullOrEmpty()) {
                            workManager.cancelUniqueWork(RefreshTokenWorker.REFRESH_TOKEN_WORK)
//                            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
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
                                if (event.message == "refresh token fail"){
                                    workManager.cancelUniqueWork(RefreshTokenWorker.REFRESH_TOKEN_WORK)
                                }
                            }
                        }
                    }
                }

                //getProfile
                launch {
                    viewModel.profileState.collectLatest {
                        if (it.successLoading != null){
                            binding.tvLogginedBy.isVisible=true
                            binding.tvUserName.text= it.successLoading!!.name
                            binding.tvTodayDate.text = it.successLoading!!.todayDate
                            binding.tvTodayName.text = it.successLoading!!.todayName
                        }
                    }
                }
            }
        }

        viewModel.isLogin.observe(viewLifecycleOwner){
            if (it){
                    enqueueRefreshTokenWork()
                    viewModel.getProfile()
                viewModel.getGoldPrice()
            }else{
                findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
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
        binding.btnUpdate.setOnClickListener {
            requireContext().showSuccessDialog("Price Uploaded") {
//                findNavController().popBackStack()
//                findNavController().clearBackStack(R.id.dailyGoldPriceFragment)
            }
        }
    }
    private fun enqueueRefreshTokenWork() {
        workManager.enqueueUniquePeriodicWork(
            RefreshTokenWorker.REFRESH_TOKEN_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}
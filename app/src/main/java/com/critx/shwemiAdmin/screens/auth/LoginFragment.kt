package com.critx.shwemiAdmin.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.critx.common.databinding.LoadingDialogBinding
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.hideKeyboard
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private var snackBar: Snackbar? = null
    private lateinit var loadingDialog:AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoginBinding.inflate(inflater).also {
            binding = it
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        binding.loginButton.setOnClickListener {
            hideKeyboard(requireActivity(),it)
            binding.tilUsername.error=null
            binding.tilPassword.error=null
            if (binding.edtUserName.text.isNullOrEmpty()){
                binding.tilUsername.error = "Username Required"
            }
            if (binding.edtPassword.text.isNullOrEmpty()){
                binding.tilPassword.error="Password Required"
            }
            if (!binding.edtUserName.text.isNullOrEmpty() && !binding.edtPassword.text.isNullOrEmpty()){
                viewModel.login(
                    binding.edtUserName.text.toString(),
                    binding.edtPassword.text.toString()
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //Login Action
                launch {
                    viewModel.state.collectLatest {
                        if (it.loading){
                            loadingDialog.show()
                        }else loadingDialog.dismiss()
                        if (!it.successMessage.isNullOrEmpty()) {
                            findNavController().popBackStack()
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

            }
        }

    }



}
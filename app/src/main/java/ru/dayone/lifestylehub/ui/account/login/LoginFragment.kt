package ru.dayone.lifestylehub.ui.account.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.account.login.LoginViewModel
import ru.dayone.lifestylehub.account.login.LoginViewModelFactory
import ru.dayone.lifestylehub.databinding.FragmentLoginBinding
import ru.dayone.lifestylehub.account.utils.AccountStatus

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider.Factory.from()

        val viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                requireContext()
            )
        )[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.tvToRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_account_to_registrationFragment)
        }

        binding.btnLogin.setOnClickListener {
            clearErrors()

            val email = binding.etEmailValue.text.toString()
            val password = binding.etLoginPassword.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                if(email.isEmpty()){
                    binding.inputLayoutEmailValue.error = getString(R.string.error_empty_field)
                }
                if(password.isEmpty()){
                    binding.inputLayoutLoginPassword.error = getString(R.string.error_empty_field)
                }
            }else{
                binding.llLoginProgress.visibility = View.VISIBLE
                binding.llLoginProgress.setOnClickListener(null)
                viewModel.loginUser(email, password)
            }
        }

        viewModel.accountStatus.observe(viewLifecycleOwner){ status ->
            binding.llLoginProgress.visibility = View.GONE

            when(status){
                is AccountStatus.Failed -> { onLoginError(status.message) }
                is AccountStatus.Succeed -> { onLoginSucceed() }
            }
        }

        return binding.root
    }

    private fun onLoginSucceed(){
        findNavController().navigate(R.id.action_navigation_login_to_navigation_account_info)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onLoginError(error: String){
        Toast.makeText(
            requireContext(),
            error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun clearErrors(){
        binding.inputLayoutEmailValue.error = ""
        binding.inputLayoutEmailValue.isErrorEnabled = false

        binding.inputLayoutLoginPassword.error = ""
        binding.inputLayoutLoginPassword.isErrorEnabled = false
    }
}
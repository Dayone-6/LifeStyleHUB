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
import ru.dayone.lifestylehub.account.utils.AccountFailureCode
import ru.dayone.lifestylehub.account.utils.AccountStatus
import ru.dayone.lifestylehub.data.local.AppPrefs

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

            val login = binding.etLoginValue.text.toString()
            val password = binding.etLoginPassword.text.toString()
            if(login.isEmpty() || password.isEmpty()){
                if(login.isEmpty()){
                    binding.inputLayoutLoginValue.error = getString(R.string.error_empty_field)
                }
                if(password.isEmpty()){
                    binding.inputLayoutLoginPassword.error = getString(R.string.error_empty_field)
                }
            }else{
                viewModel.loginUser(login, password)
            }
        }

        viewModel.loginUser.observe(viewLifecycleOwner){user ->
            AppPrefs.setAuthorizedUserLogin(user.login)
            AppPrefs.setIsAuthorized(true)
        }

        viewModel.accountStatus.observe(viewLifecycleOwner){ status ->
            when(status){
                is AccountStatus.Failed -> {
                    when(status.code){
                        AccountFailureCode.DEFAULT -> {
                            onDefaultError()
                        }
                        AccountFailureCode.LOGIN_NOT_EXISTS -> {
                            onLoginNotExistsError()
                        }
                        AccountFailureCode.INVALID_PASSWORD -> {
                            onPasswordError()
                        }
                        else -> {}
                    }
                }
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

    private fun onDefaultError(){
        Toast.makeText(
            requireContext(),
            getString(R.string.message_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onLoginNotExistsError(){
        binding.inputLayoutLoginValue.error = getString(R.string.error_login_not_exists)
    }

    private fun onPasswordError(){
        binding.inputLayoutLoginPassword.error = getString(R.string.error_bad_password)
    }

    private fun clearErrors(){
        binding.inputLayoutLoginValue.error = ""
        binding.inputLayoutLoginValue.isErrorEnabled = false

        binding.inputLayoutLoginPassword.error = ""
        binding.inputLayoutLoginPassword.isErrorEnabled = false
    }
}
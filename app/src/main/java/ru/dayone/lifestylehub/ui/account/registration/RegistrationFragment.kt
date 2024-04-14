package ru.dayone.lifestylehub.ui.account.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.account.model.User
import ru.dayone.lifestylehub.account.registiration.RegistrationViewModel
import ru.dayone.lifestylehub.account.registiration.RegistrationViewModelFactory
import ru.dayone.lifestylehub.databinding.FragmentRegistrationBinding
import ru.dayone.lifestylehub.account.utils.AccountStatus

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider.Factory.from()
        viewModel = ViewModelProvider(
            this,
            RegistrationViewModelFactory(
                requireContext()
            )
        )[RegistrationViewModel::class.java]

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        viewModel.accountStatus.observe(viewLifecycleOwner) { status ->
            binding.llRegistrationLoading.visibility = View.GONE
            binding.llRegistration.isEnabled = true
            when(status){
                is AccountStatus.Succeed -> {
                    findNavController().navigate(R.id.action_navigation_registration_to_navigation_login)
                }
                is AccountStatus.Failed -> {
                    Toast.makeText(
                        requireContext(),
                        status.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnRegistrate.setOnClickListener {
            clearAllErrors()

            val password = binding.etRegistrationPassword.text.toString()
            val name = binding.etRegistrationName.text.toString()
            val email = binding.etRegistrationEmail.text.toString()

            if(password.isEmpty() || name.isEmpty()){
                val error = getString(R.string.error_empty_field)
                if(password.isEmpty()){
                    binding.inputLayoutRegistrationPassword.error = error
                }
                if(name.isEmpty()){
                    binding.inputLayoutRegistrationName.error = error
                }
            }else{
                binding.llRegistrationLoading.visibility = View.VISIBLE
                binding.llRegistration.isEnabled = false
                binding.llRegistrationLoading.setOnClickListener(null)

                viewModel.signUpNewUser(
                    User(
                        email,
                        password,
                        name
                    )
                )
            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearAllErrors(){
        binding.inputLayoutRegistrationPassword.error = ""
        binding.inputLayoutRegistrationPassword.isErrorEnabled = false

        binding.inputLayoutRegistrationName.error = ""
        binding.inputLayoutRegistrationName.isErrorEnabled = false
    }
}
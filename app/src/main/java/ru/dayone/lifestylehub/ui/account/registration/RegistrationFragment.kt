package ru.dayone.lifestylehub.ui.account.registration

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.account.local_data.User
import ru.dayone.lifestylehub.account.registiration.RegistrationViewModel
import ru.dayone.lifestylehub.account.registiration.RegistrationViewModelFactory
import ru.dayone.lifestylehub.databinding.FragmentRegistrationBinding
import ru.dayone.lifestylehub.account.utils.AccountFailureCode
import ru.dayone.lifestylehub.account.utils.AccountStatus
import ru.dayone.lifestylehub.account.utils.AccountSuccessCode

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
            when(status){
                is AccountStatus.Succeed -> {
                    when(status.code){
                        AccountSuccessCode.ADD_USER_SUCCEED -> {
                            binding.llRegistrationLoading.visibility = View.GONE
                            binding.llRegistration.isEnabled = true
                            findNavController().navigate(R.id.action_navigation_registration_to_navigation_login)
                        }
                        AccountSuccessCode.GET_RANDOM_USER_SUCCEED -> {
                            binding.llRegistrationLoading.visibility = View.GONE
                            binding.llRegistration.isEnabled = true
                        }
                        else -> {}
                    }
                }
                is AccountStatus.Failed -> {
                    binding.llRegistrationLoading.visibility = View.GONE
                    binding.llRegistration.isEnabled = true
                    when(status.code){
                        AccountFailureCode.DEFAULT -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.message_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        AccountFailureCode.LOGIN_ALREADY_EXISTS -> {
                            binding.inputLayoutRegistrationLogin.error = getString(R.string.error_login_already_exists)
                        }
                        AccountFailureCode.GET_RANDOM_USER_FAILED -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.message_get_random_user_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                }
            }
        }

        viewModel.userData.observe(viewLifecycleOwner){user ->
            binding.etRegistrationLogin.text = Editable.Factory().newEditable(user.login)
            binding.etRegistrationName.text = Editable.Factory().newEditable(user.name)
            binding.etRegistrationSurname.text = Editable.Factory().newEditable(user.surname)
            binding.etRegistrationEmail.text = Editable.Factory().newEditable(user.email)
        }

        binding.llRegistrationLoading.visibility = View.VISIBLE
        binding.llRegistration.isEnabled = false
        viewModel.getRandomUser()

        binding.btnRegistrate.setOnClickListener {
            clearAllErrors()

            val login = binding.etRegistrationLogin.text.toString()
            val password = binding.etRegistrationPassword.text.toString()
            val name = binding.etRegistrationName.text.toString()
            val surname = binding.etRegistrationSurname.text.toString()
            val email = binding.etRegistrationEmail.text.toString()

            if(login.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()){
                val error = getString(R.string.error_empty_field)
                if(login.isEmpty()){
                    binding.inputLayoutRegistrationLogin.error = error
                }
                if(password.isEmpty()){
                    binding.inputLayoutRegistrationPassword.error = error
                }
                if(name.isEmpty()){
                    binding.inputLayoutRegistrationName.error = error
                }
                if(surname.isEmpty()){
                    binding.inputLayoutRegistrationSurname.error = error
                }
            }else{
                binding.llRegistrationLoading.visibility = View.VISIBLE
                binding.llRegistration.isEnabled = false

                viewModel.addUser(
                    User(
                    login,
                    password.hashCode(),
                    name,
                    surname,
                    email
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
        binding.inputLayoutRegistrationLogin.isErrorEnabled = false
        binding.inputLayoutRegistrationLogin.error = ""

        binding.inputLayoutRegistrationPassword.error = ""
        binding.inputLayoutRegistrationPassword.isErrorEnabled = false

        binding.inputLayoutRegistrationName.error = ""
        binding.inputLayoutRegistrationName.isErrorEnabled = false

        binding.inputLayoutRegistrationSurname.error = ""
        binding.inputLayoutRegistrationSurname.isErrorEnabled = false
    }
}
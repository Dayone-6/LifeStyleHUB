package ru.dayone.lifestylehub.ui.account.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.databinding.FragmentAccountInfoBinding
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.utils.status.Status

class AccountInfoFragment : Fragment() {

    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AccountInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider.Factory.from()

        var authLogin = ""
        if (!AppPrefs.getIsAuthorized()){
            findNavController().navigate(R.id.action_accountInfoFragment_to_navigation_login)
        }else{
            authLogin = AppPrefs.getAuthorizedUserLogin()
        }
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this,
            AccountInfoViewModelFactory(
                requireContext()
            )
        )[AccountInfoViewModel::class.java]

        viewModel.status.observe(viewLifecycleOwner){status ->
            when(status){
                is Status.Success -> { onGetUserSuccess() }
                is Status.Failure -> { onGetUserFailed() }
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {user ->
            binding.tvAccountEmail.text = user.email
            binding.tvAccountLogin.text = user.login
            binding.tvAccountName.text = user.name
            binding.tvAccountSurname.text = user.surname
        }

        viewModel.getUserByLogin(authLogin)

        binding.btnLogOut.setOnClickListener {
            AppPrefs.setIsAuthorized(false)
            AppPrefs.setAuthorizedUserLogin("")
            findNavController().navigate(R.id.action_accountInfoFragment_to_navigation_login)
        }
        return binding.root
    }

    private fun onGetUserSuccess(){
        binding.llAccountLoading.visibility = View.GONE
    }

    private fun onGetUserFailed(){
        Toast.makeText(
            requireContext(),
            getString(R.string.message_get_user_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import ru.dayone.lifestylehub.account.model.User
import ru.dayone.lifestylehub.databinding.FragmentAccountInfoBinding
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.utils.status.UserInfoStatus

class AccountInfoFragment : Fragment() {

    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider.Factory.from()

        if (!AppPrefs.getIsAuthorized()){
            findNavController().navigate(R.id.action_accountInfoFragment_to_navigation_login)
        }
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)

        AppPrefs.getAuthInstance().currentUser ?.apply {
            binding.llAccountLoading.visibility = View.GONE
            binding.tvAccountEmail.text = email
            binding.tvAccountName.text = displayName
        }

        binding.btnLogOut.setOnClickListener {
            AppPrefs.setIsAuthorized(false)
            AppPrefs.getAuthInstance().signOut()
            findNavController().navigate(R.id.action_accountInfoFragment_to_navigation_login)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
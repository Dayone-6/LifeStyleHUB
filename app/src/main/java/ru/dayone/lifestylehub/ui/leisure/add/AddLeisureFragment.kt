package ru.dayone.lifestylehub.ui.leisure.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity
import ru.dayone.lifestylehub.databinding.FragmentAddLeisureBinding
import ru.dayone.lifestylehub.utils.status.Status
import java.util.Calendar

class AddLeisureFragment : Fragment() {


    private lateinit var viewModel: AddLeisureViewModel

    private var _binding: FragmentAddLeisureBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = System.currentTimeMillis()

    private lateinit var placeId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            this,
            AddLeisureViewModelFactory(requireContext())
        )[AddLeisureViewModel::class.java]

        _binding = FragmentAddLeisureBinding.inflate(inflater, container, false)

        if (arguments != null){
            binding.inputLeisureNotes.visibility = View.GONE
            placeId = requireArguments().getString("placeId").toString()
        }else{
            placeId = ""
        }

        viewModel.status.observe(viewLifecycleOwner){
            when(it){
                is Status.Success -> { onAddSucceed() }
                is Status.Failure -> { onAddFailed() }
            }
        }

        binding.calendarLeisure.date = selectedDate
        binding.calendarLeisure.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val c = Calendar.getInstance()
            c.set(year, month, dayOfMonth)
            selectedDate = c.timeInMillis
        }

        binding.btnAddLeisure.setOnClickListener {
            if(binding.etLeisureTitle.text.isEmpty()){
                binding.inputLeisureTitle.error = getString(R.string.error_empty_field)
            }else{
                viewModel.addLeisure(LeisureEntity(
                    0,
                    AppPrefs.getAuthorizedUserLogin(),
                    binding.etLeisureTitle.text.toString(),
                    selectedDate,
                    binding.etLeisureNotes.text.toString(),
                    placeId
                ))
            }
        }

        return binding.root
    }

    private fun onAddSucceed(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun onAddFailed(){
        Toast.makeText(
            requireContext(),
            getString(R.string.message_failed),
            Toast.LENGTH_SHORT
        ).show()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
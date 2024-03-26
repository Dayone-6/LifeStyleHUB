package ru.dayone.lifestylehub.ui.activity_settings

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.databinding.FragmentActivitySettingsBinding

class ActivitySettingsFragment : Fragment() {
    private var _binding: FragmentActivitySettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivitySettingsBinding.inflate(inflater, container, false)

        val errorViewsList = listOf(
            binding.inputLayoutActivityPriceFrom,
            binding.inputLayoutActivityPriceTo,
            binding.inputLayoutActivityAccessibilityFrom,
            binding.inputLayoutActivityAccessibilityTo,
            binding.inputLayoutActivityAccessibilityExact,
            binding.inputLayoutActivityPriceExact
        )
        val etViewsList = listOf(
            binding.etActivityPriceFrom,
            binding.etActivityPriceTo,
            binding.etActivityAccessibilityFrom,
            binding.etActivityAccessibilityTo,
            binding.etActivityAccessibilityExact,
            binding.etActivityPriceExact
        )
        requireArguments().apply {
            binding.etActivityPriceFrom.text = Editable.Factory().newEditable(getString("minPrice"))
            binding.etActivityPriceTo.text = Editable.Factory().newEditable(getString("maxPrice"))
            binding.etActivityPriceExact.text =
                Editable.Factory().newEditable(getString("exactPrice"))

            binding.etActivityAccessibilityFrom.text =
                Editable.Factory().newEditable(getString("minAccessibility"))
            binding.etActivityAccessibilityTo.text =
                Editable.Factory().newEditable(getString("maxAccessibility"))
            binding.etActivityAccessibilityExact.text =
                Editable.Factory().newEditable(getString("exactAccessibility"))

            binding.etActivityParticipants.text =
                Editable.Factory().newEditable(getString("participants"))
        }

        binding.btnActivitySaveSettings.setOnClickListener {
            val b = Bundle()
            var flag = true
            for(errorView in errorViewsList.indices){
                if(!checkIsValueValid(etViewsList[errorView].text.toString())){
                    flag = false
                    errorViewsList[errorView].error = getString(R.string.error_value_not_between_one_and_zero)
                }
            }
            if(flag) {
                b.putString("minPrice", binding.etActivityPriceFrom.text.toString())
                b.putString("maxPrice", binding.etActivityPriceTo.text.toString())
                b.putString(
                    "exactPrice",
                    binding.etActivityPriceExact.text.toString())

                b.putString(
                    "minAccessibility",
                    binding.etActivityAccessibilityFrom.text.toString())
                b.putString(
                    "maxAccessibility",
                    binding.etActivityAccessibilityTo.text.toString())
                b.putString(
                    "exactAccessibility",
                    binding.etActivityAccessibilityExact.text.toString())

                b.putString(
                    "participants",
                    binding.etActivityParticipants.text.toString())

                findNavController().navigate(
                    R.id.action_activitySettingsFragment_to_findActivityFragment,
                    b
                )
            }
        }
        return binding.root
    }

    private fun checkIsValueValid(s: String): Boolean {
        val num: Double = if (s.isEmpty()) {
            0.0
        } else {
            s.toDouble()
        }
        return num in 0.0..1.0
    }
}
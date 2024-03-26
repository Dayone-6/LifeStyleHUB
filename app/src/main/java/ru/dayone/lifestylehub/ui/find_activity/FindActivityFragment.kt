package ru.dayone.lifestylehub.ui.find_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.adapters.ActivityAdapter
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.local.activity.ActivityEntity
import ru.dayone.lifestylehub.databinding.FragmentFindAcitvityBinding
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.status.ActivityStatus

class FindActivityFragment : Fragment() {

    private var _binding: FragmentFindAcitvityBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FindActivityViewModel

    companion object {
        private var minPrice = ""
        private var maxPrice = ""
        private var exactPrice = ""
        private var minAccessibility = ""
        private var maxAccessibility = ""
        private var exactAccessibility = ""
        private var participants = ""
    }

    private val adapter =
        ActivityAdapter(mutableListOf(), object : ActivityAdapter.OnClickListener {
            override fun onItemClick(item: ActivityEntity) {

            }

            override fun onFavIconClick(item: ActivityEntity) {
                viewModel.updateActivity(item)
            }

        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindAcitvityBinding.inflate(inflater, container, false)
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types,
            R.layout.item_activity_type
        )
        binding.spActivityType.adapter = spinnerAdapter
        binding.rvActivities.adapter = adapter
        binding.rvActivities.layoutManager = LinearLayoutManager(requireContext())
        if (arguments != null) {
            requireArguments().apply {
                exactPrice = getString("exactPrice")!!
                maxPrice = getString("maxPrice")!!
                minPrice = getString("minPrice")!!
                minAccessibility = getString("minAccessibility")!!
                maxAccessibility = getString("maxAccessibility")!!
                exactAccessibility = getString("exactAccessibility")!!
                participants = getString("participants")!!
            }
        }
        viewModel = ViewModelProvider(
            this,
            FindActivityViewModelFactory(requireContext())
        )[FindActivityViewModel::class.java]
        binding.btnSettings.setOnClickListener {
            val b = Bundle()
            b.apply {
                putString("minPrice", minPrice)
                putString("maxPrice", maxPrice)
                putString("exactPrice", exactPrice)

                putString("minAccessibility", minAccessibility)
                putString("maxAccessibility", maxAccessibility)
                putString("exactAccessibility", exactAccessibility)

                putString("participants", participants)
            }
            findNavController().navigate(
                R.id.action_findActivityFragment_to_activitySettingsFragment,
                b
            )
        }

        binding.btnFindActivity.setOnClickListener {
            if(AppPrefs.isNetworkAvailable(requireContext())) {
                binding.btnFindActivity.isEnabled = false
                binding.pbActivity.visibility = View.VISIBLE
                val type: String? = when (binding.spActivityType.selectedItem.toString()) {
                    getString(R.string.text_type_busywork) -> "busywork"
                    getString(R.string.text_type_mucic) -> "music"
                    getString(R.string.text_type_cooking) -> "cooking"
                    getString(R.string.text_type_diy) -> "diy"
                    getString(R.string.text_type_charity) -> "charity"
                    getString(R.string.text_type_relaxation) -> "relaxation"
                    getString(R.string.text_type_social) -> "social"
                    getString(R.string.text_type_recre) -> "recreational"
                    getString(R.string.text_type_educ) -> "education"
                    getString(R.string.text_type_any) -> null
                    else -> {
                        null
                    }
                }
                try {
                    viewModel.getRemoteActivity(
                        null,
                        type,
                        if (participants.isEmpty()) {
                            null
                        } else {
                            participants.toInt()
                        },
                        if (minPrice.isEmpty()) {
                            null
                        } else {
                            minPrice.toDouble()
                        },
                        if (maxPrice.isEmpty()) {
                            null
                        } else {
                            maxPrice.toDouble()
                        },
                        if (exactPrice.isEmpty()) {
                            null
                        } else {
                            exactPrice.toDouble()
                        },
                        if (exactAccessibility.isEmpty()) {
                            null
                        } else {
                            exactAccessibility.toDouble()
                        },
                        if (minAccessibility.isEmpty()) {
                            null
                        } else {
                            minAccessibility.toDouble()
                        },
                        if (maxAccessibility.isEmpty()) {
                            null
                        } else {
                            maxAccessibility.toDouble()
                        }
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.message_check_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                showToastNetworkNotAvailable()
            }
        }

        viewModel.remoteStatus.observe(viewLifecycleOwner) {
            binding.btnFindActivity.isEnabled = true
            binding.pbActivity.visibility = View.GONE
            when (it) {
                is ActivityStatus.Remote.Succeed -> {
                    onRemoteSucceed(it.activity)
                }

                is ActivityStatus.Remote.Failed -> {
                    when(it.code){
                        FailureCode.DEFAULT -> {onRemoteFailed()}
                        FailureCode.NO_ACTIVITY_BY_THIS_REQUEST -> { onNoActivity() }
                        else -> {}
                    }
                }
            }
        }

        viewModel.localStatus.observe(viewLifecycleOwner) {
            when (it) {
                is ActivityStatus.Local.Succeed -> {
                    onLocalSucceed(it.activities)
                }

                is ActivityStatus.Local.Failed -> {
                    onLocalFailed()
                }
            }
        }

        binding.cbOnlyFavourite.setOnClickListener {
            if(binding.cbOnlyFavourite.isChecked){
                adapter.replaceData(adapter.getData().filter { it.isFavourite }.toMutableList())
            }else{
                viewModel.getAllActivities()
            }
        }
        viewModel.getAllActivities()
        return binding.root
    }

    private fun onRemoteSucceed(activity: ActivityEntity) {
        adapter.addActivity(activity)
        binding.rvActivities.scrollToPosition(0)
        viewModel.saveActivity(activity)
    }

    private fun showToastNetworkNotAvailable() {
        Toast.makeText(
            requireContext(),
            getString(R.string.message_network_unavailable),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun onRemoteFailed() {
        Toast.makeText(
            requireContext(),
            getString(R.string.message_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onNoActivity(){
        Toast.makeText(
            requireContext(),
            getString(R.string.message_no_activity),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onLocalSucceed(activities: List<ActivityEntity>) {
        adapter.replaceData(activities.asReversed().toMutableList())
    }

    private fun onLocalFailed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
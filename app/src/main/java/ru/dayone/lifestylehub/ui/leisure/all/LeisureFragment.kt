package ru.dayone.lifestylehub.ui.leisure.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.adapters.LeisureAdapter
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity
import ru.dayone.lifestylehub.databinding.FragmentLeisureBinding
import ru.dayone.lifestylehub.utils.status.LeisureStatus

class LeisureFragment : Fragment() {

    private var _binding: FragmentLeisureBinding? = null
    private val binding get() = _binding!!

    private lateinit var leisureViewModel: LeisureViewModel

    private lateinit var adapter: LeisureAdapter
    private lateinit var skeleton: Skeleton

    private val skeletonItemsCount = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        leisureViewModel = ViewModelProvider(
            this,
            LeisureViewModelFactory(requireContext())
        )[LeisureViewModel::class.java]

        adapter = LeisureAdapter(mutableListOf(), object : LeisureAdapter.ActionListener{
            override fun onDelete(item: LeisureEntity) {
                Snackbar.make(
                    requireContext(),
                    requireView(),
                    getString(R.string.message_confirm),
                    Snackbar.LENGTH_SHORT
                ).setText(
                    getString(R.string.message_confirm_delete_leisure) + item.title + "?"
                ).setAction(getString(R.string.text_yes)){
                    leisureViewModel.deleteLeisure(item.id)
                    adapter.deleteItem(item.id)
                }.show()
            }

            override fun onItemClick(item: LeisureEntity) {
                val b = Bundle()
                b.putString("placeId", item.placeId)
                b.putString("title", item.title)
                b.putString("notes", item.notes)
                b.putLong("date", item.date)
                findNavController().navigate(R.id.action_navigation_leisure_to_leisureDetailsFragment, b)
            }
        })

        _binding = FragmentLeisureBinding.inflate(inflater, container, false)

        leisureViewModel.status.observe(viewLifecycleOwner){
            when(it){
                is LeisureStatus.Succeed -> { onGetSucceed(it.leisure) }
                is LeisureStatus.Failed -> { onGetFailed() }
            }
        }

        binding.rvLeisure.adapter = adapter
        binding.rvLeisure.layoutManager = LinearLayoutManager(requireContext())
        skeleton = binding.rvLeisure.applySkeleton(
            R.layout.item_leisure,
            skeletonItemsCount,
            AppPrefs.getSkeletonConfig()
        )

        binding.btnAllLeisureAdd.setOnClickListener {
            findNavController().navigate(R.id.action_all_leisure_to_add_leisure)
        }

        return binding.root
    }

    private fun onGetSucceed(leisure: List<LeisureEntity>){
        adapter.replaceData(leisure)
        skeleton.showOriginal()
    }

    private fun onGetFailed(){
        Toast.makeText(
            requireContext(),
            getString(R.string.message_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()
        if(AppPrefs.getIsAuthorized()) {
            skeleton.showSkeleton()
            leisureViewModel.getAllLeisure(AppPrefs.getAuthorizedUserLogin())
            binding.tvLeisureMessage.visibility = View.GONE
            binding.rvLeisure.visibility = View.VISIBLE
            binding.btnAllLeisureAdd.visibility = View.VISIBLE
        }else{
            binding.rvLeisure.visibility = View.GONE
            binding.btnAllLeisureAdd.visibility = View.GONE
            binding.tvLeisureMessage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
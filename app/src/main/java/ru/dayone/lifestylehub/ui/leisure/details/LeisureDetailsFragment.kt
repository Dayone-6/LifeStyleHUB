package ru.dayone.lifestylehub.ui.leisure.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.databinding.FragmentLeisureDetailsBinding

class LeisureDetailsFragment : Fragment() {

    private var _binding: FragmentLeisureDetailsBinding? = null
    private val binding get() = _binding!!

    private var title: String = ""
    private var notes: String = ""
    private var date = 0L
    private var placeId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeisureDetailsBinding.inflate(inflater, container, false)

        binding.view.setOnClickListener {  }

        requireArguments().apply {
            title = getString("title")!!
            notes = getString("notes")!!
            date = getLong("date")
            placeId = getString("placeId")

        }
        if(placeId == ""){
            binding.btnToPlaceDetails.visibility = View.GONE
        }else{
            binding.tvLeisureDetailsNotes.visibility = View.GONE
        }

        binding.tvLeisureDetailsNotes.text = notes
        binding.tvLeisureDetailsTitle.text = title
        binding.calendarLeisureDetails.date = date
        binding.btnToPlaceDetails.setOnClickListener {
            val b = Bundle()
            b.putString("id", placeId)
            b.putInt("from", R.layout.fragment_leisure_details)
            findNavController().navigate(R.id.action_leisure_to_place, b)
        }

        return binding.root
    }
}
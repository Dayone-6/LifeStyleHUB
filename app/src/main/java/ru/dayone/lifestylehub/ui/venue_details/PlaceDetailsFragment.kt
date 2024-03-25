package ru.dayone.lifestylehub.ui.venue_details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.android.material.snackbar.Snackbar
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.adapters.LeisureAdapter
import ru.dayone.lifestylehub.adapters.PlaceCategoryAdapter
import ru.dayone.lifestylehub.adapters.PlacePhotosAdapter
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.local.details.PlaceDetailsEntity
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity
import ru.dayone.lifestylehub.databinding.FragmentPlaceDetailsBinding
import ru.dayone.lifestylehub.utils.DATE_KEY
import ru.dayone.lifestylehub.utils.MAIN_DELIMITER
import ru.dayone.lifestylehub.utils.PHOTOS_API_KEY
import ru.dayone.lifestylehub.utils.PLACES_OAUTH_KEY
import ru.dayone.lifestylehub.utils.status.LeisureStatus
import ru.dayone.lifestylehub.utils.status.PlaceDetailsStatus

class PlaceDetailsFragment : Fragment() {
    private var _binding: FragmentPlaceDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaceDetailsViewModel

    private var photoUrls = arrayListOf<String>()

    private lateinit var mainImageSkeleton: Skeleton
    private lateinit var nameSkeleton: Skeleton
    private lateinit var addressSkeleton: Skeleton
    private lateinit var categoriesSkeleton: Skeleton
    private lateinit var statusSkeleton: Skeleton
    private lateinit var likesSkeleton: Skeleton
    private lateinit var contactsSkeleton: Skeleton
    private lateinit var leisureSkeleton: Skeleton

    private lateinit var adapter: PlaceCategoryAdapter
    private lateinit var leisureAdapter: LeisureAdapter

    private lateinit var placeId: String
    private var from = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceDetailsBinding.inflate(inflater, container, false)

        placeId = requireArguments().getString("id")!!
        from = requireArguments().getInt("from")

        adapter = PlaceCategoryAdapter(listOf())
        if (AppPrefs.getIsAuthorized()) {
            leisureAdapter =
                LeisureAdapter(mutableListOf(), object : LeisureAdapter.ActionListener {
                    override fun onDelete(item: LeisureEntity) {
                        Snackbar.make(
                            requireContext(),
                            requireView(),
                            getString(R.string.message_confirm),
                            Snackbar.LENGTH_SHORT
                        ).setText(
                            getString(R.string.message_confirm_delete_leisure) + item.title + "?"
                        ).setAction(getString(R.string.text_yes)) {
                            viewModel.deleteLeisure(item.id)
                            leisureAdapter.deleteItem(item.id)
                        }.show()
                    }

                    override fun onItemClick(item: LeisureEntity) {

                    }
                })

            binding.rvDetailsCategories.adapter = adapter
            binding.rvDetailsCategories.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            binding.rvDetailsLeisure.adapter = leisureAdapter
            binding.rvDetailsLeisure.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.btnDetailsAddLeisure.visibility = View.GONE
        }

        mainImageSkeleton = binding.ivDetailsMainPhoto.createSkeleton(AppPrefs.getSkeletonConfig())
        nameSkeleton = binding.tvDetailsName.createSkeleton(AppPrefs.getSkeletonConfig())
        addressSkeleton = binding.tvDetailsAdress.createSkeleton(AppPrefs.getSkeletonConfig())
        categoriesSkeleton = binding.rvDetailsCategories.applySkeleton(
            R.layout.item_category,
            3,
            AppPrefs.getSkeletonConfig()
        )
        statusSkeleton = binding.tvDetailsStatus.createSkeleton(AppPrefs.getSkeletonConfig())
        likesSkeleton = binding.tvDetailsLikes.createSkeleton(AppPrefs.getSkeletonConfig())
        contactsSkeleton = binding.llContacts.createSkeleton(AppPrefs.getSkeletonConfig())
        leisureSkeleton = binding.rvDetailsLeisure.applySkeleton(
            R.layout.item_leisure,
            3,
            AppPrefs.getSkeletonConfig()
        )

        mainImageSkeleton.showSkeleton()
        nameSkeleton.showSkeleton()
        addressSkeleton.showSkeleton()
        categoriesSkeleton.showSkeleton()
        statusSkeleton.showSkeleton()
        likesSkeleton.showSkeleton()
        contactsSkeleton.showSkeleton()
//        leisureSkeleton.showSkeleton()

        viewModel = ViewModelProvider(
            this,
            PlaceDetailsViewModelFactory(requireContext())
        )[PlaceDetailsViewModel::class.java]

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                is PlaceDetailsStatus.Succeed -> {
                    onGetDetailsSucceed(it.details)
                }

                is PlaceDetailsStatus.Failed -> {
                    onGetDetailsFailed()
                }
            }
        }

        viewModel.leisureStatus.observe(viewLifecycleOwner) {
            when (it) {
                is LeisureStatus.Succeed -> {
                    onLeisureSucceed(it.leisure)
                }

                is LeisureStatus.Failed -> {
                    onGetDetailsFailed()
                }
            }
        }

        if (binding.ivDetailsMainPhoto.drawable == null) {
            viewModel.getDetails(
                placeId,
                PLACES_OAUTH_KEY,
                DATE_KEY,
                PHOTOS_API_KEY
            )
        }

        binding.btnDetailsAddLeisure.setOnClickListener {
            val b = Bundle()
            b.putString("placeId", placeId)
            findNavController().navigate(
                if (from == R.layout.fragment_home) {
                    R.id.action_place_to_add_leisure
                } else {
                    R.id.action_leisure_place_to_add_leisure
                }, b
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (AppPrefs.getIsAuthorized()) {
            viewModel.getLeisure(AppPrefs.getAuthorizedUserLogin(), placeId)
        }
    }

    private fun onLeisureSucceed(leisure: List<LeisureEntity>) {
        leisureAdapter.replaceData(leisure)
//        leisureSkeleton.showOriginal()
    }

    private fun onGetDetailsSucceed(details: PlaceDetailsEntity) {
        if (details.suffixes != null) {
            photoUrls = details.suffixes!!.split(MAIN_DELIMITER) as ArrayList<String>
        }
        Glide
            .with(requireContext())
            .load(details.photoPrefix + "1000x1000" + photoUrls[0])
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mainImageSkeleton.showOriginal()
                    return false
                }

            })
            .into(binding.ivDetailsMainPhoto)
        binding.tvDetailsName.text = details.name
        binding.tvDetailsAdress.text = details.address

        val likesText = getString(R.string.text_like_people) + ": " + details.likes
        binding.tvDetailsLikes.text = likesText

        if (details.status.isNullOrEmpty()) {
            statusSkeleton.showOriginal()
            binding.tvDetailsStatus.visibility = View.GONE
        } else {
            binding.tvDetailsStatus.text = details.status
            binding.tvDetailsStatus.setTextColor(
                if (details.isOpen == true) {
                    requireContext().getColor(R.color.text_good)
                } else {
                    requireContext().getColor(R.color.text_bad)
                }
            )
            statusSkeleton.showOriginal()

        }

        if (!details.phone.isNullOrEmpty()) {
            binding.tvDetailsPhone.text = details.phone
        } else {
            binding.tvDetailsPhone.visibility = View.GONE
            binding.tvDetailsPhoneTitle.visibility = View.GONE
        }
        if (!details.url.isNullOrEmpty()) {
            binding.tvDetailsUrl.text = details.url
        } else {
            binding.tvDetailsUrl.visibility = View.GONE
            binding.tvDetailsUrlTitle.visibility = View.GONE
        }

        if (details.categories != null) {
            Log.d("data", details.categories.split(MAIN_DELIMITER).toString())
            adapter.replaceData(details.categories.split(MAIN_DELIMITER))
        }

        if (details.suffixes != null) {
            val suffixes = details.suffixes!!.split(MAIN_DELIMITER)
            binding.rvDetailsPhotos.adapter = PlacePhotosAdapter(
                suffixes.subList(2, suffixes.size),
                details.photoPrefix + "1500x1000"
            )
            binding.rvDetailsPhotos.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        contactsSkeleton.showOriginal()
        likesSkeleton.showOriginal()
        addressSkeleton.showOriginal()
        categoriesSkeleton.showOriginal()
        nameSkeleton.showOriginal()

    }

    private fun onGetDetailsFailed() {
        Toast.makeText(
            requireContext(),
            getString(R.string.message_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
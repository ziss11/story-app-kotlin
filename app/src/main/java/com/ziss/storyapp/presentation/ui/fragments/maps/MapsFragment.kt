package com.ziss.storyapp.presentation.ui.fragments.maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.ziss.storyapp.R
import com.ziss.storyapp.databinding.FragmentMapsBinding
import com.ziss.storyapp.presentation.ViewModelFactory
import com.ziss.storyapp.presentation.ui.fragments.home.MapsViewModel
import com.ziss.storyapp.presentation.viewmodels.LoginViewModel
import com.ziss.storyapp.utils.ResultState

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var factory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { factory }
    private val mapsViewModel: MapsViewModel by viewModels { factory }

    private val boundBuilder = LatLngBounds.Builder()
    private var token = ""

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMapToolbarEnabled = false

        setMapStyle()
        fetchStories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireActivity())
        fetchToken()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.fabZoomIn.setOnClickListener {
            map.animateCamera(CameraUpdateFactory.zoomIn())
        }
        binding.fabZoomOut.setOnClickListener {
            map.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    private fun setMapStyle() {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(),
                    R.raw.maps_style
                )
            )
            if (!success) {
                Log.d(TAG, "Style paring failed")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can't find style. Error", e)
        }
    }

    private fun fetchToken() {
        loginViewModel.getToken().observe(requireActivity()) { token = it }
    }

    private fun fetchStories() {
        mapsViewModel.getStoriesWithLocation(token).observe(requireActivity()) { result ->
            when (result) {
                is ResultState.Loading -> {}
                is ResultState.Success -> {
                    val stories = result.data.stories

                    stories.forEach { story ->
                        val latLng = LatLng(story.lat, story.lon)
                        map.addMarker(
                            MarkerOptions().position(latLng).title(story.name)
                                .snippet(story.description)
                        )
                        boundBuilder.include(latLng)
                    }

                    val bounds = boundBuilder.build()
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(bounds, 300)
                    )
                }

                is ResultState.Failed -> {}
                else -> {}
            }
        }
    }

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
    }
}
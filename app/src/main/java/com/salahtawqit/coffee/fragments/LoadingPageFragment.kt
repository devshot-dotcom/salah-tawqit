package com.salahtawqit.coffee.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import com.salahtawqit.coffee.viewmodels.LocationHelperViewModel
import com.salahtawqit.coffee.R
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * The intermittent loading screen.
 *
 * While the loading is displayed, prayer times are calculated depending on the intermittent
 * calculation method.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class LoadingPageFragment : Fragment() {
    private lateinit var videoView : VideoView
    private var loadingStatusView : TextView? = null
    private lateinit var locationHelperViewModel : LocationHelperViewModel
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()

    /**
     * Display the loading video in the corresponding [VideoView].
     * @param view [View] The parent activity's layout view.
     */
    private fun startLoadingVideo() {
        // Set the gif resource to be shown inside the video view.
        videoView.setVideoURI(
            Uri.parse("android.resource://${activity?.packageName}/${R.raw.loading}"))
        videoView.start()

        // Restart the video once it's completed.
        videoView.setOnCompletionListener { videoView.start() }
    }

    /**
     * Navigate to the last fragment after a set delay.
     */
    private fun navigateBackwardsDelayed(delay: Int) {
        Timer().schedule(delay.toLong()) {
            activity?.runOnUiThread {
                lifecycleScope.launchWhenResumed {
                    findNavController().popBackStack()
                }
            }
        }
    }

    /**
     * Use [locationHelperViewModel] to calculate user location.
     * Observe the calculated location and pass it onto the [calculationHelperViewModel] for
     * unpacking and parsing purposes.
     */
    private fun handleAutomaticCalculation() {
        locationHelperViewModel.calculateUserLocation()

        // Observe location error.
        locationHelperViewModel.isLocationErred.observe(this) {
            if(it) navigateBackwardsDelayed(3000)
        }

        // Observe location changes.
        locationHelperViewModel.location.observe(this) {
            if (it == null) {
                // Navigate back to the previous fragment after a delay.
                navigateBackwardsDelayed(3000)
                return@observe
            }

            // Pass location to [CalculationHelperViewModel] and calculate.
            calculationHelperViewModel.setLocation(it)
            calculationHelperViewModel.automaticallyCalculate()
        }

        // Observe geocode error.
        calculationHelperViewModel.isGeocodeErred.observe(this) {
            if(it) {
                loadingStatusView?.text = getString(R.string.error_finding_location)
                navigateBackwardsDelayed(3000)
            }
        }

        // Observe calculation completion.
        calculationHelperViewModel.isCalculated.observe(this) {
            if(it) navigateBackwardsDelayed(2000)
        }
    }

    private fun handleManualCalculation() {
        TODO("Handle Manual Calculation")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_loading_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start the loading video
        videoView = view.findViewById(R.id.gif_video_view)
        startLoadingVideo()

        // Loading status TextView
        loadingStatusView = activity?.findViewById(R.id.loading_status_text)

        // Initialize the ViewModels
        locationHelperViewModel = ViewModelProvider(this).get(LocationHelperViewModel::class.java)

        /** Set loading status Text as the one in the [LocationHelperViewModel] */
        loadingStatusView?.text = locationHelperViewModel.loadingStatus.value

        // Observe loading status.
        locationHelperViewModel.loadingStatus.observe(viewLifecycleOwner) {
            loadingStatus -> loadingStatusView?.text = loadingStatus
        }

        // Get the navigation arguments [calculationMode in this case]
        val args: LoadingPageFragmentArgs by navArgs()

        when (args.calculationMode) {
            "automatic" -> handleAutomaticCalculation()
            "manual" -> handleManualCalculation()
        }
    }

    override fun onStart() {
        super.onStart()
        startLoadingVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationHelperViewModel.removeUpdates()
    }
}
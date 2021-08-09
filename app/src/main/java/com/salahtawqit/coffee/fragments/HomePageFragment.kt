package com.salahtawqit.coffee.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.addCallback
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The homepage that contains the landing page.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class HomePageFragment: Fragment() {
    private lateinit var pager: ViewPager2
    private lateinit var scrollButton: ImageButton
    private lateinit var scrollButtonParams: RelativeLayout.LayoutParams
    private var fragmentContainer: FragmentContainerView? = null
    private val animDuration = 500
    private var pagerItemCount = 1
    private val calculationHelperViewModel: CalculationHelperViewModel by activityViewModels()

    // ViewPager2 page change listener.
    private val pageChangeListener = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            // Rotate scrollButton based on the pager's current position.
            when(position) {
                0 -> scrollButton.animate().setDuration(animDuration.toLong()).rotation(0f)
                1 -> scrollButton.animate().setDuration(animDuration.toLong()).rotation(180f)
            }
        }
    }

    // ScrollButton click listener.
    private val scrollButtonClicked = View.OnClickListener {
        when(pager.currentItem) {
            0 -> pager.currentItem = 1
            else -> pager.currentItem = 0
        }
    }

    /**
     * Check if database has an existing row of calculation results.
     *
     * In a positive case, use [CalculationHelperViewModel] to set the dataMap using the list
     * received from the database.
     */
    private fun checkResultsFromDatabase(context: Context) {
        lifecycleScope.launch(Dispatchers.IO) {
            val calculationResultsDao = RoomDatabaseHelper.getRoom(context).calculationResultsDao()
            val selectionResults = calculationResultsDao.selectAll()

            if(selectionResults.isNotEmpty())
                calculationHelperViewModel.setDataMapFromEntities(selectionResults)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // OnBackPressed implementation.
        activity?.onBackPressedDispatcher?.addCallback {

            // Scroll back to the previous fragment if it exists.
            if(pager.currentItem > 0) {
                pager.currentItem -= pager.currentItem
                return@addCallback
            }

            // Else do the default back button work.
            this.isEnabled = false
            activity?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cache the required views
        pager = view.findViewById(R.id.home_pager)
        scrollButton = view.findViewById(R.id.home_scroll_button)
        scrollButtonParams = scrollButton.layoutParams as RelativeLayout.LayoutParams
        fragmentContainer = activity?.findViewById(R.id.fragment_container)

        // Instantiate the adapter.
        val adapter = activity?.let { HomePagerAdapter(it) }

        /**
         * Set pager page change listener to implement [scrollButton] animation.
         */
        pager.registerOnPageChangeCallback(pageChangeListener)

        // Set click listener on scrollButton
        scrollButton.setOnClickListener(scrollButtonClicked)

        checkResultsFromDatabase(view.context)

        /**
         * Check if calculation by the [CalculationHelperViewModel] has already been done.
         *
         * This happens in two cases.
         *
         * 1) When the current fragment is navigated back from the [LoadingPageFragment].
         * The [LoadingPageFragment] uses [CalculationHelperViewModel], which is part of the activity,
         * to calculate prayer times, generate a Map of all the required data, and pop it's back stack.
         *
         * 2) When the calculation results exist in the database. [checkResultsFromDatabase] checks
         * whether the results exist and uses [CalculationHelperViewModel], which is part of the activity,
         * to generate a Map of all the required data from the received database data.
         */
        calculationHelperViewModel.isCalculated.observe(viewLifecycleOwner) {
            if(it) {
                // Number of fragments in the pager.
                ++pagerItemCount

                // The number should always be less than 2. Since we only need 2 fragments.
                if(pagerItemCount > 2) --pagerItemCount

                // Set Pager Adapter
                pager.adapter = adapter

                // Newer fragment is added with a little delay, set the currentItem once it's added.
                pager.post {
                    pager.setCurrentItem(1, true)
                }

                // Show the scrollButton
                scrollButton.visibility = View.VISIBLE

                return@observe
            }

            // Set Pager Adapter
            pager.adapter = adapter
        }
    }

    /**
     * Adapter for [pager].
     */
    private inner class HomePagerAdapter(fragment : FragmentActivity)
        : FragmentStateAdapter(fragment) {
        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        override fun getItemCount(): Int {
            return pagerItemCount
        }

        /**
         * Provide a new Fragment associated with the specified position.
         *
         *
         * The adapter will be responsible for the Fragment lifecycle:
         *
         * The Fragment will be used to display an item.
         * The Fragment will be destroyed when it gets too far from the viewport, and its state
         * will be saved. When the item is close to the viewport again, a new Fragment will be
         * requested, and a previously saved state will be used to initialize it.
         *
         * @see ViewPager2.setOffscreenPageLimit
         */
        override fun createFragment(position: Int): Fragment {
            return when(position) {
                1 -> PrayerTimesFragment()
                else -> LandingPageFragment()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        pager.unregisterOnPageChangeCallback(pageChangeListener)
    }
}
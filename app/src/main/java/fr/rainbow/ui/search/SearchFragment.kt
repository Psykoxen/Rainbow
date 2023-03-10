package fr.rainbow.ui.search

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.MapFragment
import com.google.android.libraries.places.api.Places
import fr.rainbow.BuildConfig
import fr.rainbow.R
import fr.rainbow.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val TAG = "DEBUGMAP"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //API Places

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.GOOGLE_MAPS_API_KEY)
        }

        val searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, place.name)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: $status")
            }
        })

        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        super.onActivityResult(requestCode, resultCode, data)
    }

}
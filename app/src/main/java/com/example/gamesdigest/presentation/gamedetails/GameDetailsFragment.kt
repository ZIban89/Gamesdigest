package com.example.gamesdigest.presentation.gamedetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gamesdigest.R
import com.example.gamesdigest.common.loadImage
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.data.remote.UNEXPECTED_ERROR_MESSAGE
import com.example.gamesdigest.databinding.FragmentGameDetailsBinding
import com.example.gamesdigest.domain.model.GameDetails
import com.example.gamesdigest.domain.model.Genre
import com.example.gamesdigest.domain.model.Publisher
import com.example.gamesdigest.presentation.gameeditions.GameEditionsFragment.Companion.GAME_NAME
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null
    private val binding: FragmentGameDetailsBinding get() = _binding!!

    private var currentGameDetails: GameDetails? = null

    private val viewModel: GameDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(RAWG_GAME_ID) ?: 0
        viewModel.getGameDetailsById(id)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameDetailsState.collectLatest { state ->

                    when {
                        state.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        state.error.isNotBlank() -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(requireContext(), UNEXPECTED_ERROR_MESSAGE, true)
                        }
                        else -> {
                            binding.progressBar.visibility = View.GONE
                            currentGameDetails = state.data
                            currentGameDetails?.let { initViews(it) }
                        }
                    }
                }
            }
        }

        binding.buttonFindBestPrice.setOnClickListener {
            findNavController().navigate(
                R.id.action_gameDetailsFragment_to_gameEditionsFragment,
                Bundle().apply {
                    putString(GAME_NAME, currentGameDetails?.slug)
                }
            )
        }
    }

    private fun initViews(gameDetails: GameDetails) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            binding.imageGameBackground.loadImage(gameDetails.backgroundImage)
        }
        binding.metacriticsRating.text = gameDetails.metacritic.toString()
        binding.playersRating.text = gameDetails.rating.toString()
        binding.fieldGameName.text = gameDetails.name
        binding.fieldReleaseDate.text = gameDetails.released
        initGenresChipGroup(gameDetails.genres)
        initPublishersFlow(gameDetails.publishers)
        binding.textViewWebsite.text = gameDetails.website
        binding.textViewDescription.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(gameDetails.description, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(gameDetails.description)
            }
        binding.metacriticsRatingContainer.visibility = View.VISIBLE
        binding.usersRatingContainer.visibility = View.VISIBLE
    }

    private fun initPublishersFlow(publishers: List<Publisher>) {
        val list = mutableListOf<Int>()
        for (publisher in publishers) {
            val textView = TextView(requireContext())
            textView.id = ViewCompat.generateViewId()
            textView.text = publisher.name
            binding.layoutGameDetails.addView(textView)
            list.add(textView.id)
        }
        binding.flowPublishers.referencedIds = list.toIntArray()
    }

    private fun initGenresChipGroup(genres: List<Genre>) {
        binding.chipGroupGenres.removeAllViews()
        for (genre in genres) {
            val chip = Chip(requireContext())
            chip.text = genre.name
            binding.chipGroupGenres.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RAWG_GAME_ID = "rawg_game_id"
        fun newInstance(gameId: Int) =
            GameDetailsFragment().apply {
                arguments = Bundle().apply { putInt(RAWG_GAME_ID, gameId) }
            }
    }
}

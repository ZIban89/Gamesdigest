package com.example.gamesdigest.presentation.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesdigest.R
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.databinding.FragmentGamesBinding
import com.example.gamesdigest.domain.model.Game
import com.example.gamesdigest.domain.model.Genre
import com.example.gamesdigest.presentation.gamedetails.GameDetailsFragment.Companion.RAWG_GAME_ID
import com.example.gamesdigest.presentation.games.adapter.GamesAdapter
import com.example.gamesdigest.presentation.games.adapter.GamesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding: FragmentGamesBinding get() = _binding!!
    private var selectedGenre = 0

    private val detailsCallback: (Game) -> Unit = { game ->
        NavHostFragment.findNavController(this).navigate(
            R.id.action_gamesFragment_to_gameDetailsFragment,
            Bundle().apply { putInt(RAWG_GAME_ID, game.id) })
    }

    private val viewModel: GamesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GamesAdapter(detailsCallback)
        initRecyclerView(adapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gamesListState.collectLatest { state ->
                    when {
                        state.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        state.error.isNotBlank() -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(
                                requireContext(),
                                "Loading error. Try later.",
                                true
                            )
                        }
                        else -> {
                            binding.progressBar.visibility = View.GONE
                            state.data?.let {
                                adapter.submitData(it)
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genresListState.collectLatest { state ->
                    when {
                        state.isLoading -> {
                            binding.spinnerGenres.setText(getString(R.string.loading_message))
                        }
                        state.error.isNotBlank() -> {
                            binding.spinnerGenres.setText(getString(R.string.loading_error_message))
                        }
                        else -> {
                            binding.spinnerGenres.setText(getString(R.string.all_label))
                            binding.spinnerGenres.setAdapter(
                                ArrayAdapter(
                                    requireContext(),
                                    R.layout.support_simple_spinner_dropdown_item,
                                    mutableListOf(
                                        Genre(null, getString(R.string.all_label))
                                    ) + (state.data as List<Genre>)
                                )
                            )
                        }
                    }
                }
            }
        }
        initViews()
    }

    private fun initRecyclerView(adapter: GamesAdapter) {
        binding.gamesRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GamesLoadStateAdapter { adapter.retry() },
            footer = GamesLoadStateAdapter { adapter.retry() }
        )
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.recycler_view_decoration
        )?.let {
            dividerItemDecoration.setDrawable(it)
        }

        binding.gamesRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.gamesRecyclerView.setHasFixedSize(true)
    }

    fun initViews() {

        binding.spinnerGenres.setOnItemClickListener { _, _, position, _ ->
            selectedGenre = position
        }

        binding.buttonOpenFilterParameters.setOnClickListener {
            setFilterFieldsVisibility(true)
        }

        binding.applyFilterBtn.setOnClickListener {
            setFilterFieldsVisibility(false)
            with(binding) {
                viewModel.setFilterParameters(
                    RawgGameFilter(
                        genre = (spinnerGenres.adapter?.getItem(selectedGenre) as? Genre)?.id?.toString(),
                        releaseYearSince = editTextReleaseDate.text.toString(),
                        minMetacriticRating = metacriticRatingSeekbar.value.toInt().toString()
                    )
                )
            }
        }
    }

    private fun setFilterFieldsVisibility(isVisible: Boolean) {
        val filterFieldsVisibility = if (isVisible) View.VISIBLE else View.GONE
        val filterBtnVisibility = if (!isVisible) View.VISIBLE else View.GONE

        with(binding) {
            buttonOpenFilterParameters.visibility = filterBtnVisibility
            containerGenresSpinner.visibility = filterFieldsVisibility
            layoutReleaseDate.visibility = filterFieldsVisibility
            metacriticRatingLayout.visibility = filterFieldsVisibility
            applyFilterBtn.visibility = filterFieldsVisibility
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

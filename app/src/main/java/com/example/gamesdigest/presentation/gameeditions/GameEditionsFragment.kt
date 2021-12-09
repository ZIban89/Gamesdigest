package com.example.gamesdigest.presentation.gameeditions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gamesdigest.R
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.data.remote.UNEXPECTED_ERROR_MESSAGE
import com.example.gamesdigest.databinding.FragmentGameEditionsBinding
import com.example.gamesdigest.domain.model.GameEdition
import com.example.gamesdigest.presentation.deals.DealsFragment.Companion.CHEAPSHARK_GAME_ID
import com.example.gamesdigest.presentation.deals.DealsFragment.Companion.GAME_EDITION_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameEditionsFragment : Fragment() {

    private var _binding: FragmentGameEditionsBinding? = null
    private val binding: FragmentGameEditionsBinding get() = _binding!!
    private val callback: (GameEdition) -> Unit = { gameEdition ->
        findNavController().navigate(
            R.id.action_gameEditionsFragment_to_dealsFragment,
            Bundle().apply {
                putInt(CHEAPSHARK_GAME_ID, gameEdition.gameId.toInt())
                putString(GAME_EDITION_NAME, gameEdition.name)
            })
    }

    private val viewModel: GameEditionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameEditionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameName = arguments?.getString(GAME_NAME) ?: "The Witcher 3"
        viewModel.getEditions(gameName)
        val adapter = GameEditionAdapter(callback)
        binding.recyclerViewGameEdition.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gamesEditionState.collectLatest { state ->
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
                            adapter.submitList(state.data)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val GAME_NAME = "game_name"
        fun newInstance(gameName: String) = GameEditionsFragment().apply {
            arguments = Bundle().apply {
                putString(
                    GAME_NAME, gameName
                )
            }
        }
    }
}

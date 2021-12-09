package com.example.gamesdigest.presentation.deals

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
import com.example.gamesdigest.common.loadImage
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.data.remote.UNEXPECTED_ERROR_MESSAGE
import com.example.gamesdigest.databinding.FragmentDealsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null
    private val binding: FragmentDealsBinding get() = _binding!!
    private val viewModel: DealsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = arguments?.getInt(CHEAPSHARK_GAME_ID) ?: 0
        val gameName = arguments?.getString(GAME_EDITION_NAME) ?: ""
        viewModel.getDeals(gameId)
        val adapter = DealsAdapter()
        binding.recyclerViewDeals.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dealsState.collectLatest { state ->

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
                            state.data?.let {
                                withContext(Dispatchers.IO) {
                                    binding.imageViewGameThumb.loadImage(
                                        it.thumb
                                    )
                                }
                                binding.textViewGameName.text = it.gameName
                                adapter.submitList(it.list)
                            }
                        }
                    }
                    state.data?.let {
                        withContext(Dispatchers.IO) { binding.imageViewGameThumb.loadImage(it.thumb) }
                        binding.textViewGameName.text = it.gameName
                        adapter.submitList(it.list)
                    }
                }
            }
        }
        binding.buttonOpenSubscribeDialog.setOnClickListener {
            findNavController().navigate(
                R.id.action_dealsFragment_to_subscriptionDialogFragment,
                Bundle().apply {
                    putInt(CHEAPSHARK_GAME_ID, gameId)
                    putString(GAME_EDITION_NAME, gameName)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CHEAPSHARK_GAME_ID = "cheapshark_game_id"
        const val GAME_EDITION_NAME = "game_edition_name"
        fun newInstance(cheapSharkId: Int, gameEditionName: String) = DealsFragment().apply {
            arguments = Bundle().apply {
                putInt(CHEAPSHARK_GAME_ID, cheapSharkId)
                putString(GAME_EDITION_NAME, gameEditionName)
            }
        }
    }
}

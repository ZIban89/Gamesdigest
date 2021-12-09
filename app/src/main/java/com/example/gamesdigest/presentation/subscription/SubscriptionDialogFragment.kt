package com.example.gamesdigest.presentation.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import com.example.gamesdigest.R
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.databinding.FragmentSubscriptionDialogBinding
import com.example.gamesdigest.presentation.deals.DealsFragment.Companion.GAME_EDITION_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionDialogFragment : DialogFragment() {

    private var _binding: FragmentSubscriptionDialogBinding? = null
    private val binding: FragmentSubscriptionDialogBinding get() = _binding!!

    private val viewModel: SubscriptionDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = arguments?.getInt(CHEAPSHARK_GAME_ID) ?: 0
        val gameName = arguments?.getString(GAME_EDITION_NAME) ?: ""
        val emailFromPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString(getString(R.string.email_pref_key), "")
        binding.editTextEmail.setText(emailFromPreference)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subscriptionState.collectLatest { state ->
                    when {
                        state.isLoading -> {
                        }
                        state.error.isNotBlank() -> {
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            if (state.data == true) {
                                showToast(
                                    requireActivity(),
                                    getString(R.string.success_subscription_message),
                                    true
                                )
                                this@SubscriptionDialogFragment.dismiss()
                            } else {
                                showToast(requireActivity(), state.error, true)
                            }
                        }
                    }
                }
            }
        }

        binding.buttonSubscribe.setOnClickListener {
            viewModel.subscribe(
                binding.editTextEmail.text.toString(),
                gameId,
                gameName,
                binding.editTextDesiredPrice.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CHEAPSHARK_GAME_ID = "cheapshark_game_id"
        fun newInstance() = SubscriptionDialogFragment()
    }
}

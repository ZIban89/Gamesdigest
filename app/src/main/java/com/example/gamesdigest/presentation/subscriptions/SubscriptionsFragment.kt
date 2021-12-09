package com.example.gamesdigest.presentation.subscriptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.data.remote.UNEXPECTED_ERROR_MESSAGE
import com.example.gamesdigest.databinding.FragmentSubscriptionsBinding
import com.example.gamesdigest.presentation.subscriptions.adapter.SubscriptionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionsFragment : Fragment() {

    private var _binding: FragmentSubscriptionsBinding? = null
    private val binding: FragmentSubscriptionsBinding get() = _binding!!
    private val viewModel: SubscriptionsViewModel by viewModels()
    private val deleteSubscriptionCallback: (String) -> Unit = { documentId ->
        viewModel.deleteSubscriptionByDocId(documentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = arguments?.getString(SUBSCRIPTIONS_EMAIL) ?: ""
        viewModel.getSubscriptionsByEmail(email)
        val adapter = SubscriptionAdapter(deleteSubscriptionCallback)
        binding.recyclerViewSubscriptions.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subscriptionsState.collectLatest { state ->
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
        const val SUBSCRIPTIONS_EMAIL = "subscriptions_email"
        fun newInstance(email: String) = SubscriptionsFragment()
            .apply {
                arguments = Bundle().apply { putString(SUBSCRIPTIONS_EMAIL, email) }
            }
    }
}

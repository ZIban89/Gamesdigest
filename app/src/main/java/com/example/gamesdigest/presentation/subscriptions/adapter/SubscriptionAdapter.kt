package com.example.gamesdigest.presentation.subscriptions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesdigest.databinding.ItemSubscriptionBinding
import com.example.gamesdigest.domain.model.Subscription

class SubscriptionAdapter(
    private val callback: (String) -> Unit
) : ListAdapter<Subscription, SubscriptionViewHolder>(SubscriptionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        return SubscriptionViewHolder(
            ItemSubscriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class SubscriptionViewHolder(
    private val binding: ItemSubscriptionBinding,
    private val callback: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(subscription: Subscription) {
        binding.textViewSubscriprionGameName.text = subscription.gameEditionName
        binding.textViewSubscriptionDesiredPrice.text = "$${subscription.desiredPrice}"
        binding.buttonSubscriptionUnsubscribe.setOnClickListener { callback(subscription.id) }
    }
}

class SubscriptionDiffCallback : DiffUtil.ItemCallback<Subscription>() {
    override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
        return oldItem == newItem
    }
}

package com.example.gamesdigest.presentation.deals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesdigest.common.loadImage
import com.example.gamesdigest.databinding.ItemDealBinding
import com.example.gamesdigest.domain.model.Deal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealsAdapter : ListAdapter<Deal, DealViewHolder>(DealDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DealViewHolder(
            ItemDealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}

class DealDiffCallback : DiffUtil.ItemCallback<Deal>() {
    override fun areItemsTheSame(oldItem: Deal, newItem: Deal) =
        oldItem.store.storeID == newItem.store.storeID

    override fun areContentsTheSame(oldItem: Deal, newItem: Deal) =
        oldItem == newItem
}

class DealViewHolder(private val binding: ItemDealBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Deal) {
        CoroutineScope(Dispatchers.IO).launch {
            binding.imageViewStoreLogo.loadImage(item.store.image)
        }
        binding.textViewStoreName.text = item.store.storeName
        binding.textViewGamePrice.text = "$${item.price}"
        binding.textViewSavings.text = "-${item.saving}%"
    }
}

package com.example.gamesdigest.presentation.gameeditions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamesdigest.common.loadImage
import com.example.gamesdigest.databinding.ItemGameEditionBinding
import com.example.gamesdigest.domain.model.GameEdition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameEditionAdapter(
    private val callback: (GameEdition) -> Unit
) : ListAdapter<GameEdition, GameEditionViewHolder>(GameEditionDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameEditionViewHolder {
        return GameEditionViewHolder(
            ItemGameEditionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )
    }

    override fun onBindViewHolder(holder: GameEditionViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}

class GameEditionDiffCallback : DiffUtil.ItemCallback<GameEdition>() {
    override fun areItemsTheSame(oldItem: GameEdition, newItem: GameEdition): Boolean {
        return oldItem.gameId == newItem.gameId
    }

    override fun areContentsTheSame(oldItem: GameEdition, newItem: GameEdition): Boolean {
        return oldItem == newItem
    }
}

class GameEditionViewHolder(
    private val binding: ItemGameEditionBinding,
    private val callback: (GameEdition) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var gameEdition: GameEdition? = null

    init {
        itemView.setOnClickListener {
            gameEdition?.let { callback(it) }
        }
    }

    fun onBind(gameEdition: GameEdition) {
        this.gameEdition = gameEdition
        CoroutineScope(Dispatchers.IO).launch {
            binding.imageViewGameEditionThumb.loadImage(gameEdition.thumb)
        }
        binding.textViewGameEditionName.text = gameEdition.name
        binding.textViewBestPrice.text = "Best price: $${gameEdition.cheapest}"
    }
}

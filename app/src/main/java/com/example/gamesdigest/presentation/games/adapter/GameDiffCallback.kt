package com.example.gamesdigest.presentation.games.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.gamesdigest.domain.model.Game

class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}

package com.example.gamesdigest.presentation.games.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.gamesdigest.databinding.ItemGameBinding
import com.example.gamesdigest.domain.model.Game

class GamesAdapter(
    private val callback: (Game) -> Unit
) : PagingDataAdapter<Game, GameViewHolder>(GameDiffCallback()) {
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )
    }
}

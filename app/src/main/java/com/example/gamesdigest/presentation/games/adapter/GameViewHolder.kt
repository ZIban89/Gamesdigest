package com.example.gamesdigest.presentation.games.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.gamesdigest.R
import com.example.gamesdigest.common.loadImage
import com.example.gamesdigest.databinding.ItemGameBinding
import com.example.gamesdigest.domain.model.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GameViewHolder(
    private val binding: ItemGameBinding,
    private val callback: (Game) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var game: Game? = null
    var job: Job? = null

    init {
        itemView.setOnClickListener {
            game?.let { callback(it) }
        }
    }

    fun onBind(game: Game) {
        if (this.game != game) {
            this.game = game
            job?.cancel()
            binding.imageGameBackground.setImageResource(R.drawable.img_rawg_banner)
            job = CoroutineScope(Dispatchers.IO).launch {
                binding.imageGameBackground.loadImage(game.backgroundImage)
            }
        }

        binding.metacriticsRating.text = game.metacritic.toString()
        binding.playersRating.text = game.rating.toString()
        binding.gamesName.text = game.name
        binding.releaseDate.text = game.released
    }
}

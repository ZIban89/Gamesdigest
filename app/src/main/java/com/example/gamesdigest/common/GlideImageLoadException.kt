package com.example.gamesdigest.common

import com.bumptech.glide.load.engine.GlideException

class GlideImageLoadException(
    val imgUrl: String,
    message: String?,
    glideException: GlideException?
) : IllegalStateException(message, glideException)

package com.example.gamesdigest.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gamesdigest.R
import com.example.gamesdigest.data.remote.IMG_HEIGHT
import com.example.gamesdigest.data.remote.IMG_WIDTH
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

suspend fun ImageView.loadImage(imageUrl: String) {
    try {
        val drawable = Glide.with(this@loadImage.context)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    GlideImageLoadException(imageUrl, e?.message, e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .submit(IMG_WIDTH, IMG_HEIGHT)
            .get() ?: AppCompatResources.getDrawable(context, R.drawable.img_not_found)
        if (currentCoroutineContext().isActive) {
            withContext(Dispatchers.Main) {
                this@loadImage.setImageDrawable(drawable)
            }
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) { this@loadImage.setImageResource(R.drawable.img_not_found) }
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun String.isValidEmail(): Boolean {
    val regex =
        "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"

    val pattern: Pattern = Pattern.compile(regex)
    return pattern.matcher(this).matches()

    /**
     * Tests failed with it
     *android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()*/

}

fun Query.asFlow(): Flow<QuerySnapshot> {
    return callbackFlow {
        val callback = addSnapshotListener { querySnapshot, ex ->
            if (ex != null) {
                close(ex)
            } else {
                trySend(querySnapshot!!)
            }
        }
        awaitClose {
            callback.remove()
        }
    }
}

fun ViewModel.getViewModelScope(coroutineScope: CoroutineScope?) =
    coroutineScope ?: this.viewModelScope

fun showToast(context: Context, message: String, isLongDuration: Boolean) {
    Toast.makeText(context, message, if (isLongDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        .show()
}

fun String.toLocalDate(): String {

    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
}

package com.example.apipractice.application

import android.graphics.drawable.Drawable
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.apipractice.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CustomBindings {
    companion object {

        @BindingAdapter("error", "isValid", "isErrorShown")
        @JvmStatic
        fun setError(
            editText: TextInputEditText,
            errorMessage: String?,
            isValid: Boolean,
            isErrorShown: Boolean
        ) {
            if (errorMessage != "") {
                if (isErrorShown && !isValid) {
                    editText.error = errorMessage
                }
            }
        }


        @BindingAdapter("imageurl")
        @JvmStatic
        fun bindImageView(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_account).centerCrop()
                        .error(R.drawable.ic_account)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .transform(CenterCrop(), RoundedCorners(1000))
                )
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("onClick")
        fun View.onClick(listener: (() -> Unit)?) {
            setOnClickListener {
                listener?.invoke()
            }
        }
    }
}


@BindingAdapter("setError")
fun EditText.setError(
    errorModel: BaseModel?
) {
    errorModel?.let {
        if (!it.status)
            error = it.message
    }
}

@BindingAdapter("setImageResource")
fun ImageView.setImageResource(reference: Int?) {
    if (reference != null) {
        setImageResource(reference)
    }
}

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("visibleOrInvisible")
fun View.visibleOrInvisible(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}

@BindingAdapter("scrollable")
fun TextView.scrollable(scrollable: Boolean?) {
    if (scrollable != null && scrollable) {
        movementMethod = ScrollingMovementMethod()
    }
}

@BindingAdapter("snackbarMessage")
fun View.snackBarMessage(message: String?) {
    if (!message.isNullOrEmpty()) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

}
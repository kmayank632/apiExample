package com.example.apipractice.custombinding

import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.apipractice.R
import com.example.apipractice.basemodel.BaseModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CustomBindings {
    companion object {

        /** Set Image
         */
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
                )
                .into(view)
        }

        /** Click Binding
         */
        @JvmStatic
        @BindingAdapter("onClick")
        fun View.onClick(listener: (() -> Unit)?) {
            setOnClickListener {
                listener?.invoke()
            }
        }
    }
}

/** Set Error
 */
@BindingAdapter("setError")
fun EditText.setError(
    errorModel: BaseModel?
) {
    errorModel?.let {
        error = if (!it.status)
            it.message
        else{
            null
        }
    }
}

/** Set Image Resources
 */
@BindingAdapter("setImageResource")
fun ImageView.setImageResource(reference: Int?) {
    if (reference != null) {
        setImageResource(reference)
    }
}

/**
 * Visibility Visible or Gone
 */
@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

/**
 * Visibility Visible or Invisible
 */
@BindingAdapter("visibleOrInvisible")
fun View.visibleOrInvisible(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}

/**
 * Scrollable or Not
 */
@BindingAdapter("scrollable")
fun TextView.scrollable(scrollable: Boolean?) {
    if (scrollable != null && scrollable) {
        movementMethod = ScrollingMovementMethod()
    }
}
/**
 * Set Snackbar
 */
@BindingAdapter("snackbarMessage")
fun View.snackBarMessage(message: String?) {
    if (!message.isNullOrEmpty()) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

}
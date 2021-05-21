package com.example.apipractice.view.listener


import android.content.Context
import android.content.res.Resources
import androidx.annotation.*
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

class ResourceProvider(private val context: Context) {

    /** @return Resources */
    private fun getResources(): Resources = context.resources

    /** @return String Value from StringResource */
    fun getString(@StringRes stringId: Int): String = getResources().getString(stringId)

    /** @return String Value from StringResource */
    fun getString(@StringRes stringID: Int, vararg formatArgs: Any?): String =
        getResources().getString(stringID, formatArgs)

    /** @return Drawable Value from Drawable Resource */
    fun getDrawable(@DrawableRes drawableId: Int) = ContextCompat.getDrawable(context, drawableId)

    /** @return Color Value from Color Resource */
    fun getColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(context, colorId)

    /** @return Integer Value from Integer Resource */
    fun getInteger(@IntegerRes integerId: Int) = getResources().getInteger(integerId)

    /** @return String Array from StringResource */
    fun getStringArray(@ArrayRes stringID: Int): Array<String> =
        getResources().getStringArray(stringID)

    /** @return String Array from StringResource */
    fun getDimension(@DimenRes dimenId: Int) = getResources().getDimension(dimenId)

}
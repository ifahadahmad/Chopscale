package com.github.developer__.extensions

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

/**
 * Created by Jemo on 12/5/16.
 */
private val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
    .setDuration(1800) // how long the shimmering animation takes to do one full sweep
    .setBaseAlpha(0.7f) //the alpha of the underlying children
    .setHighlightAlpha(0.6f) // the shimmer alpha amount
    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
    .setAutoStart(true)
    .build()

fun ImageView.loadImage(imgUrl: String){
    Log.e("Glide",imgUrl.toString())
    try{
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
        Glide.with(context).load(imgUrl).placeholder(shimmerDrawable).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).into(this)
    }catch (error:Throwable){
        Log.e("GLIDE",error.toString())
    }

}

fun ImageView.loadImage(imgDrawable: Drawable?){
    this.setImageDrawable(imgDrawable)
}

fun View.stayVisibleOrGone(stay: Boolean){
    this.visibility = if (stay) View.VISIBLE else View.GONE
}
package com.github.developer__.asycn

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ClipDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.R

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

import java.lang.ref.WeakReference
import kotlin.reflect.typeOf

/**
 * Created by Jemo on 12/5/16.
 */

class ClipDrawableProcessorTask<T>(imageView: ImageView, seekBar: SeekBar, private val context: Context, private val loadedFinishedListener: OnAfterImageLoaded? = null) : AsyncTask<T, Void, ClipDrawable>() {
    private val imageRef: WeakReference<ImageView>
    private val seekBarRef: WeakReference<SeekBar>

    init {
        this.imageRef = WeakReference(imageView)
        this.seekBarRef = WeakReference(seekBar)
    }



    override fun doInBackground(vararg args: T): ClipDrawable? {
        Looper.myLooper()?.let { Looper.prepare() }
        try {
            var theBitmap: Bitmap
            if (args[0] is Uri) {




                theBitmap = Glide.with(context)
                    .asBitmap()
                    .load(args[0])
                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .submit()
                    .get()


            } else {
                theBitmap = (args[0] as BitmapDrawable).bitmap

            }

//            val tmpBitmap = getScaledBitmap(theBitmap)
//            if (tmpBitmap != null)
//                theBitmap = tmpBitmap

            val bitmapDrawable = BitmapDrawable(context.resources, theBitmap)
            Log.e("Kya hai",theBitmap.byteCount.toString())
            return ClipDrawable(bitmapDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL)
        } catch (e: GlideException) {
            e.logRootCauses("GLIDERROR")

        }catch (e:Exception){

            Log.e("errors",e.toString())
        }

        return null
    }

    private fun getScaledBitmap(bitmap: Bitmap): Bitmap? {
        try {
            if (imageRef.get() == null)
                return bitmap
            val imageWidth = imageRef.get()?.width!!
            val imageHeight = imageRef.get()?.height!!

            if (imageWidth > 0 && imageHeight > 0) {

                return Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(clipDrawable: ClipDrawable?) {
        Log.e("CHECK",clipDrawable.toString())
        Log.e("CHECK2",imageRef.get().toString())
        if (imageRef.get() != null) {
            if (clipDrawable != null) {
                initSeekBar(clipDrawable)
                imageRef.get()?.setImageDrawable(clipDrawable)
                Log.e("set",clipDrawable.toString())
                if (clipDrawable.level != 0) {
                    val progressNum = 5000
                    clipDrawable.level = progressNum
                } else
                    clipDrawable.level = seekBarRef.get()!!.progress
                loadedFinishedListener?.onLoadedFinished(true)
            } else {
                loadedFinishedListener?.onLoadedFinished(false)
            }
        } else {
            loadedFinishedListener?.onLoadedFinished(false)
        }
    }

    private fun initSeekBar(clipDrawable: ClipDrawable) {
        seekBarRef.get()!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if(i==8500){

                    val layoutParams = imageRef.get()?.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.setMargins(0,0,0,0)
                    imageRef.get()?.layoutParams = layoutParams
                }
                if(i==8000){
                    val layoutParams = imageRef.get()?.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.setMargins(3,0,0,0)
                    imageRef.get()?.layoutParams = layoutParams
                }
                clipDrawable.level = i

            }


            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    interface OnAfterImageLoaded {
        fun onLoadedFinished(loadedSuccess: Boolean)
    }
}
package com.example.imagediff.view.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagediff.R
import com.example.imagediff.service.model.ImageModel
import com.example.imagediff.view.ui.LandingFragmentDirections

class CustomAdapter(private var list:List<ImageModel>, private val context:Context): RecyclerView.Adapter<CustomAdapter.customViewHolder>() {







    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): customViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return customViewHolder(view)
    }

    override fun onBindViewHolder(holder: customViewHolder, position: Int) {

        val uri:Uri? = list.get(position).Image

        if(uri!=null){
            Glide.with(context).load(uri).override(200,200).centerCrop().placeholder(R.drawable.image_placeholder).into(holder.imageView)
            holder.imageView.setOnClickListener {
                val action = LandingFragmentDirections.landingToDialog(uri)
                holder.imageView.findNavController().navigate(action)
//                val imageDetails = ImageDetails(uri)
//
//                imageDetails.show((context as AppCompatActivity).supportFragmentManager,"fragment")

            }
        }




    }

    fun updateImageList(imageList:List<ImageModel>){
        list = imageList
        notifyDataSetChanged()

    }

    class customViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imageView:ImageView = view.findViewById(R.id.image_view)



    }
}
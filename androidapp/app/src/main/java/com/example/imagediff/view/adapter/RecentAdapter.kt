package com.example.imagediff.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagediff.R
import com.example.imagediff.RecentFragmentDirections
import com.example.imagediff.service.model.RecentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentAdapter(val context: Context,val allRecent: GetAllRecent):RecyclerView.Adapter<RecentAdapter.ViewHolder>() {
    private var list:ArrayList<Uri> = ArrayList<Uri>()


    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val imageView:ImageView = itemView.findViewById(R.id.recent_image_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recent_list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri:Uri = list[position]

        Glide.with(context).load(uri).override(200,200).centerCrop().placeholder(R.drawable.image_placeholder).into(holder.imageView)
        holder.imageView.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO){


                val recentList:List<RecentModel> = allRecent.get(uri)
                withContext(Dispatchers.Main){
                    if(recentList.size==2){

                        val action = RecentFragmentDirections.actionRecentFragmentToNavigationDialogFragment(uri)
                        holder.imageView.findNavController().navigate(action)

                    }else if(recentList.size==1){

                        val action = RecentFragmentDirections.actionRecentFragmentToImageResultFragment(recentList[0].url,recentList[0].uri)
                        holder.imageView.findNavController().navigate(action)
                    }
                }

            }



        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun updateRecent(rec:List<Uri>){
        list.clear()
        list.addAll(rec)
        notifyDataSetChanged()
    }

}
interface GetAllRecent {

    suspend fun get(uri:Uri):List<RecentModel>
}
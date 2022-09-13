package com.example.imagediff.view.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.imagediff.R
import com.example.imagediff.viewmodel.SuperViewModel
import com.example.imagediff.databinding.ImageDetailsBinding
import com.example.imagediff.service.model.RecentModel
import com.example.imagediff.viewmodel.RecentViewModel
import com.facebook.shimmer.Shimmer
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ImageDetails():DialogFragment() {
    lateinit var binding:ImageDetailsBinding
    private lateinit var superViewModel: SuperViewModel
    private lateinit var recentViewModel: RecentViewModel
    private val args: ImageDetailsArgs by navArgs()
    private lateinit var uri:Uri
    private lateinit var builder:AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private var isProcess = false




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        isCancelable = true
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.image_details,container,false)
        uri = args.uri
        Glide.with(this).load(uri).override(1000,1000).centerCrop().into(binding.imageView)
        superViewModel = ViewModelProvider(this).get(SuperViewModel::class.java)
        recentViewModel = ViewModelProvider(this).get(RecentViewModel::class.java)
        binding.lifecycleOwner = this
        binding.data = superViewModel
        builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Are you Sure")
        builder.setMessage("Server is Processing the Image")
        builder.setPositiveButton("Yes") {
            _,_->
            findNavController().navigateUp()
        }
        builder.setNegativeButton("No") {
            _,_ ->

        }
        alertDialog = builder.create()
        binding.upscale.setOnClickListener { _ ->
            onClickHandler("upscale")
        }

        binding.removebg.setOnClickListener { _ ->
            onClickHandler("remove")
        }
        binding.closeDialog.setOnClickListener {
                if(isProcess){
                    alertDialog.show()
                }else
                findNavController().navigateUp()
        }
        return binding.root
    }

    private fun onClickHandler(str:String){
        isCancelable = false
        binding.shimmerView.startShimmer()
        isProcess = true
        viewLifecycleOwner.lifecycleScope.launch {

            val recentModel:RecentModel? = recentViewModel.getFromUriAction(uri, str)
            if(recentModel!=null){

                val action = ImageDetailsDirections.dialogToResult(recentModel.url,recentModel.uri)
                findNavController().navigate(action)

            }else
                sendData(str)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertDialog.cancel()

    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = 1000
        params?.height = 1230
        dialog?.window?.attributes = params
    }

    private fun sendData(str:String){
        val selectedImage: Uri = uri
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor: Cursor =
            context?.contentResolver?.query(selectedImage, filePathColumn, null, null, null)!!
        cursor.moveToFirst()

        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
        val mediaPath = cursor.getString(columnIndex)
        val file = File(mediaPath)
        val filePart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        )
        superViewModel.sendDataToAPI(str,filePart)
        getDataLive(str)
        setupError()

    }


    private fun getDataLive(action:String){
        superViewModel.super_response.observe(this) { data ->

            data?.let {

// Make Another Fragment for Response
                recentViewModel.addRecent(RecentModel(uri, it.data,action,null))
                val action =
                    ImageDetailsDirections.dialogToResult(it.data, uri)

                findNavController().navigate(action)

            }

        }
    }
    private fun setupError(){
        superViewModel.error.observe(this) { data ->
            data?.let{

                val action = ImageDetailsDirections.dialogToError(data.errorMessage,data.errorCode)
                this.findNavController().navigate(action)
            }
        }

    }




}
package com.example.imagediff.view.ui

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.imagediff.R
import com.example.imagediff.databinding.FragmentImageResultBinding
import com.example.imagediff.viewmodel.RecentViewModel
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ImageResultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val args: ImageResultFragmentArgs by navArgs()
    private lateinit var uri: Uri
    private lateinit var url:String
    private lateinit var binding:FragmentImageResultBinding
    private lateinit var recentViewModel: RecentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uri = args.uri
        url = args.url
        url = url.replace("\\","/").trim()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_result, container, false)

        binding.closeImg.setOnClickListener {
            findNavController().navigate(ImageResultFragmentDirections.actionImageResultFragmentToLandingFragment())

        }
        binding.downloadImg.setOnClickListener {

            downloadImageNew(url.split("/").last().split('.')[0],url)

        }

        binding.mySlider.setBeforeImage(url).setAfterImage(uri)

        return binding.root
    }
    private fun downloadImageNew(filename: String, downloadUrlOfImage: String) {
        try {
            val dm = requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(downloadUrlOfImage)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/png") // Your file type. You can use this code to download other file types also.
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DCIM,
                    File.separator.toString() + filename + ".jpg"
                )
            dm!!.enqueue(request)
            Toast.makeText(requireContext(), "Image download started.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Image download failed.", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.example.imagediff.view.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.imagediff.R
import com.example.imagediff.databinding.FragmentLandingBinding
import com.example.imagediff.service.model.ImageModel
import com.example.imagediff.view.adapter.CustomAdapter
import com.example.imagediff.viewmodel.ImageViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.jar.Manifest


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LandingFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentLandingBinding
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var adapter: CustomAdapter
    private var imageList = ArrayList<ImageModel>()
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
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_landing,container,false)
        imageViewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)
        binding.lifecycleOwner = this
        if(context?.let {
                ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } ==PackageManager.PERMISSION_GRANTED)
            imageViewModel.getAllImages()
        val recyclerView = binding.photoList
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = CustomAdapter(imageList,requireContext())
        recyclerView.addItemDecoration(EqualSpaceItemDecoration(15))
        recyclerView.adapter = adapter
        binding.selectButton.setOnClickListener {


            val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


            startForResult.launch(intent)

        }
        getImages()

        return binding.root
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode== Activity.RESULT_OK){


            val intent = result.data
            val uri:Uri? = intent?.data
            if(uri!=null){
                val action = LandingFragmentDirections.landingToDialog(uri)
                findNavController().navigate(action)
            }

        }
    }


    private fun getImages(){
        imageViewModel.getImagesList().observe(requireActivity(), Observer <List<ImageModel>>{ data ->
            data?.let {

                adapter.updateImageList(data)


            }
        })
    }
    class EqualSpaceItemDecoration(private val mSpaceHeight: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = mSpaceHeight
            outRect.top = mSpaceHeight
            outRect.left = mSpaceHeight
            outRect.right = mSpaceHeight
        }
    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


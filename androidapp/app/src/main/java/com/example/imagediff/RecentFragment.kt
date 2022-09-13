package com.example.imagediff

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imagediff.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagediff.databinding.FragmentRecentBinding
import com.example.imagediff.service.model.RecentModel
import com.example.imagediff.view.adapter.GetAllRecent
import com.example.imagediff.view.adapter.RecentAdapter
import com.example.imagediff.view.ui.LandingFragment
import com.example.imagediff.viewmodel.RecentViewModel
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RecentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentRecentBinding
    private lateinit var recentViewModel: RecentViewModel
    private lateinit var adapter: RecentAdapter
    private var recentList = ArrayList<Uri>()
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

        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_recent,container,false)

        recentViewModel = ViewModelProvider(requireActivity()).get(RecentViewModel::class.java)
        binding.lifecycleOwner =this
        val recyclerView = binding.recentList
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = RecentAdapter(requireContext(), object : GetAllRecent {
            override suspend fun get(uri: Uri): List<RecentModel> {

                return recentViewModel.getFromUri(uri)

            }
        })
        recyclerView.addItemDecoration(LandingFragment.EqualSpaceItemDecoration(15))
        recyclerView.adapter = adapter

        getAllRecent()

        return binding.root
    }
    private fun getAllRecent(){

        recentViewModel.allUris.observe(requireActivity()) { data ->
            data?.let {
                recentList = data as ArrayList<Uri>
                if(recentList.isEmpty()){
                    binding.recentList.visibility = View.GONE
                    binding.placeholderContainer.visibility = View.VISIBLE

                }else{

                    binding.recentList.visibility = View.VISIBLE
                    binding.placeholderContainer.visibility = View.GONE

                }
                adapter.updateRecent(recentList)


            }
        }

    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.example.imagediff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.imagediff.ErrorFragmentArgs
import com.example.imagediff.ErrorFragmentDirections
import com.example.imagediff.R
import com.example.imagediff.databinding.FragmentErrorBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ErrorFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentErrorBinding
    private val args: ErrorFragmentArgs by navArgs()
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
        val message = args.message
        val error = args.errorCode
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_error,container,false)
        binding.error.text = message
        binding.errorBtn.setOnClickListener {

            findNavController().navigate(ErrorFragmentDirections.actionErrorFragmentToLandingFragment())

        }
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ErrorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
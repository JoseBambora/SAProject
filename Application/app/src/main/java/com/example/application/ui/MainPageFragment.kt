package com.example.application.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.network.csstats.StatsAPI
import com.example.application.model.config.ConfigTableFuns
import com.example.application.model.csstats.Cache
import com.example.application.model.csstats.Stats
import com.example.application.databinding.FragmentFirstBinding
import retrofit2.Response


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainPageFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.graphsButton.setOnClickListener {
            if(Cache.getInstance().hasInfo())
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            else
                Toast.makeText(activity, "No performance Data", Toast.LENGTH_SHORT).show()
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
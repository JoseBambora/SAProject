package com.example.application.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.api.csstats.StatsAPI
import com.example.application.data.csstats.Cache
import com.example.application.data.config.ConfigTableFuns
import com.example.application.data.csstats.Stats
import com.example.application.databinding.FragmentFirstBinding
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

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
        teste_api()
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun suc(res : Response<Stats>) {
        Cache.getInstance().saveInfo(res)
        Log.d("DebugApp","Guardou na cache")
        Log.d("DebugApp","Cache: " + Cache.getInstance().toString())
        Log.d("DebugApp","Cache: " + Cache.getInstance().getDailyPerformance())
    }

    fun erro1(res : Response<Stats>) {
        Log.d("DebugApp","Error 1 when contacting the API")
    }
    fun erro2(t : Throwable) {
        Log.d("DebugApp","Error 2 when contacting the API " + t.message.toString())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun teste_api() {
        val config = ConfigTableFuns.getLastVersion()
        if(config != null) {
            if (Cache.getInstance().needsUpdate()) {
                Toast.makeText(activity,"Dados não estão em cache", Toast.LENGTH_SHORT).show()
                StatsAPI.getData(config.csstatsID,::suc,::erro1, ::erro2)
            }
            else
                Toast.makeText(activity,"Dados já em cache", Toast.LENGTH_SHORT).show()
        }

        else
            Toast.makeText(activity, "No Configs in the system", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
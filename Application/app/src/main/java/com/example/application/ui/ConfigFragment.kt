package com.example.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.model.config.Config
import com.example.application.model.config.ConfigTableFuns

class ConfigFragment : Fragment() {

    private var config : Config? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_config, container, false)
        config = ConfigTableFuns.getLastVersion()
        fillEditText(view)
        setOnClicks(view)
        return view
    }
    private fun fillEditText(view : View) {
        if (config != null) {
            view.findViewById<EditText>(R.id.csstatsid)?.setText(config?.csstatsID)
            view.findViewById<EditText>(R.id.bodyWeight)?.setText(config?.bodyWeight.toString())
            view.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchLocationSensor)?.isChecked = true
        }
    }

    private fun loadConfig() {

    }

    private fun setOnClicks(view : View) {
        view.findViewById<Button>(R.id.submit_config)?.setOnClickListener {v -> saveConfig()}
        view.findViewById<Button>(R.id.load_config)?.setOnClickListener {v -> loadConfig()}

    }
    private fun saveConfig() {
        val csstatsid = view?.findViewById<EditText>(R.id.csstatsid)?.text.toString()
        val bodyWeight = view?.findViewById<EditText>(R.id.bodyWeight)?.text.toString().toFloat()
        val sensorLocation = view?.findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchLocationSensor)?.isChecked ?: false
        ConfigTableFuns.newConfig(config, csstatsid, bodyWeight, sensorLocation)
        Toast.makeText(activity, "Config saved", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_to_inicial_menu)
    }
}
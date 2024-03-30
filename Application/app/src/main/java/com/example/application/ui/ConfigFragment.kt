package com.example.application.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.data.Config
import com.example.application.data.convertEntriesConfigs
import com.example.application.data.getNumberLast
import com.example.application.data.getTableConfig
import com.example.application.data.updateTableConfig
import com.example.application.data.whereClauseLast
import com.example.application.data.whereClauseLastList
import com.example.application.db.ManagerDB

class ConfigFragment : Fragment() {

    var config : Config? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_config, container, false)
        val mdb = ManagerDB.getInstance()
        val list = mdb?.select(getTableConfig(), whereClauseLast(), whereClauseLastList(), ::convertEntriesConfigs )
        val list2 = mdb?.select(getTableConfig(), null,null, ::convertEntriesConfigs)
        Log.d("DebugApp", list2.toString())
        if (list != null && list.isNotEmpty()) {
            config = list.first()
            view?.findViewById<EditText>(R.id.csstatsid)?.setText(config?.csstatsID)
        }
        val buttonSubmit = view?.findViewById<Button>(R.id.submit_config)
        buttonSubmit?.setOnClickListener {v -> saveConfig()}
        return view
    }

    fun saveConfig() {
        Log.d("DebugApp","Entrou")
        val csstatsid = view?.findViewById<EditText>(R.id.csstatsid)?.text.toString()
        Log.d("DebugApp","CS id: $csstatsid")
        if(config != null) {
            ManagerDB.getInstance()?.update(getTableConfig(), updateTableConfig(config!!.copy(current_version = 0)), whereClauseLast(), whereClauseLastList())
        }
        ManagerDB.getInstance()?.insert(getTableConfig(), updateTableConfig(Config(0,csstatsid, getNumberLast())))
        Toast.makeText(activity, "Config saved", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_to_inicial_menu)

    }

}
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
import com.example.application.data.ConfigTableFuns
import com.example.application.db.ManagerDB

class ConfigFragment : Fragment() {

    private var config : Config? = null
    private val selectedVersion : Int = ConfigTableFuns.getNumberSelected()
    private val tableName : String = ConfigTableFuns.getTableConfig()
    private val whereClause : String = ConfigTableFuns.whereClauseLast()
    private val whereClauseValues : List<String> = ConfigTableFuns.whereClauseLastList()
    private val mdb : ManagerDB? = ManagerDB.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_config, container, false)
        val list = mdb?.select(tableName, whereClause, whereClauseValues, ConfigTableFuns::convertEntriesConfigs )
        fillEditText(view,list)
        setOnClicks(view)
        return view
    }
    private fun fillEditText(view : View, list : List<Config>?) {
        if (!list.isNullOrEmpty()) {
            config = list.first()
            view.findViewById<EditText>(R.id.csstatsid)?.setText(config?.csstatsID)
        }
    }

    private fun setOnClicks(view : View) {
        view.findViewById<Button>(R.id.submit_config)?.setOnClickListener {v -> saveConfig()}
    }
    private fun saveConfig() {
        val csstatsid = view?.findViewById<EditText>(R.id.csstatsid)?.text.toString()
        if(config != null) {
            val updateClause = ConfigTableFuns.updateTableConfig(config!!.noMoreCurrentVersion())
            mdb?.update(tableName, updateClause, whereClause, whereClauseValues)
        }
        mdb?.insert(tableName, ConfigTableFuns.updateTableConfig(Config(0,csstatsid, selectedVersion)))
        Toast.makeText(activity, "Config saved", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_to_inicial_menu)
    }
}
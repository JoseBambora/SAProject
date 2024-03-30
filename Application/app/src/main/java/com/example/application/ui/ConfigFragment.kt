package com.example.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.data.getTableConfig
import com.example.application.data.whereClauseLast
import com.example.application.data.whereClauseLastList
import com.example.application.db.ManagerDB

/**
 * A simple [Fragment] subclass.
 * Use the [ConfigFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfigFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mdb = ManagerDB.getInstance()

        mdb?.select(getTableConfig(), whereClauseLast(), whereClauseLastList())

        return inflater.inflate(R.layout.fragment_config, container, false)
    }

}
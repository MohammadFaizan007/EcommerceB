package com.inlog.ecommerce.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.inlog.ecommerce.R
import com.inlog.ecommerce.model.SearchData
import com.inlog.ecommerce.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {
    private val REQUEST_CODE_SPEAK_TO_SEARCH = 10
    private var bSpeakToSearch = false
    private var adapter: SearchAdapter? = null
    private  var layout: LinearLayout? = null
    private lateinit var list: ArrayList<SearchData>
            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout = view.getRootView().findViewById(R.id.layout) as LinearLayout
        layout?.visibility = View.GONE
        onClicked()
        addingData()
        //adapter?.addData(list)
    }

    private fun onClicked() {
        img_back_search?.setOnClickListener {

        }
        btn_clear?.setOnClickListener { et_search_txt?.text?.clear() }
        btn_speak_search?.setOnClickListener {
            getSpeechInput()
            bSpeakToSearch = true }
        img_scan?.setOnClickListener { }
        et_search_txt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                et_search_txt?.let {
                    val length = s.trim().length ?: 0
                    when {
                        length > 0 -> {
                            btn_clear?.visibility = View.VISIBLE
                        }
                        else -> {
                            btn_clear?.visibility = View.GONE
                        }
                    }

                    val handler = Handler()
                    handler.postDelayed({
                        when {
                            length >= 1 -> {
                                //Api call
                                setSearchAdapter(s.toString())
                            }
                        }
                    }, 2500)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    /*
    * set Search Adapter
    * */
    private fun setSearchAdapter(string: String){
        adapter = SearchAdapter()
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val mDividerItemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        rv_search?.layoutManager = linearLayoutManager
        rv_search?.adapter = adapter
        rv_search?.addItemDecoration(mDividerItemDecoration)
        filterData(string)
    }

    /*
     * Speak to search
     * */
    private fun getSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        activity?.packageManager?.let {
            if (intent.resolveActivity(it) != null) {
                startActivityForResult(intent, REQUEST_CODE_SPEAK_TO_SEARCH)
            } else {
                Toast.makeText(
                        context,
                        "Your Device Don't Support Speech Input",
                        Toast.LENGTH_SHORT
                )
                        .show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEAK_TO_SEARCH) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                et_search_txt?.setText(result?.get(0))
            }
            return
        }
    }

    private fun addingData(): ArrayList<SearchData>{
        list = ArrayList<SearchData>()
        for (i in 0..10){
            val search = SearchData()
            search.id = i
            search.name = "$i abc"
            search.imgUrl = ""
            list.add(search)
        }
        return list
    }

    private fun filterData(string: String){
        val listFilter = ArrayList<SearchData>()
        list.forEach { event ->
            if (event.name?.contains(string) == true) {
                listFilter.add(event)
                adapter?.addData(listFilter)
            }
        }

        /*for (j in list){
            if(list.contains(string)){
                val searchD = SearchData()
                searchD.name = list[j].name
                listFilter.add(searchD)
                adapter?.addData(listFilter)
            }
        }*/
    }

    /*private fun searchData(listData : ArrayList<SearchData>): ArrayList<SearchData>{
        val data = ArrayList<SearchData>()
        for (i in listData){
            val search = SearchData()
            search.id = i.id
            search.name = i.name
            search.imgUrl = i.imgUrl
            data.add(search)
        }
        return data
    }*/

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}
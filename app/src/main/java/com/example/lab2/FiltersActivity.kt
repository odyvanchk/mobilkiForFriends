package com.example.lab2

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab2.adapters.FilterAdapter
import com.example.lab2.databinding.ActivityFiltersBinding
import com.example.lab2.entity.LANGUAGES


class FiltersActivity : AppCompatActivity() {


    private var languages = mutableListOf<String>()
    private  var ageFrom: Int = 18
    private var ageTo: Int = 100
    private var priceFrom: Double = 5.0
    private var priceTo: Double = 300.0
    private lateinit var languageAdapter : FilterAdapter

    lateinit var binding : ActivityFiltersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initSizesSpinners()
        setLanguageRecycler(LANGUAGES.values.toMutableList())
        FilterAdapter.languageList.clear()

        binding.filterButton.setOnClickListener {
            validateFilterData()
        }
    }

    private fun validateFilterData() {
        if(binding.priceFromET.text.isNotEmpty()){
            priceFrom = binding.priceFromET.text.toString().toDouble()
        }
        if(binding.priceToET.text.isNotEmpty()){
            priceTo = binding.priceToET.text.toString().toDouble()
        }

        if(binding.ageFrom.text.isNotEmpty()){
            ageFrom = binding.ageFrom.text.toString().toInt()
        }
        if(binding.ageTo.text.isNotEmpty()){
            ageTo = binding.ageTo.text.toString().toInt()
        }


        languages = FilterAdapter.languageList
        //storeProductInfo()

        if(priceFrom > priceTo){
            Toast.makeText(
                this,
                "Неправильный диапазон цены",
                Toast.LENGTH_SHORT
            ).show()
        } else if (ageFrom > ageTo) {
            Toast.makeText(
                this,
                "Неправильный диапазон размера",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            MainActivity.showPersonsByFilters(languages, ageTo, ageFrom, priceFrom, priceTo)
        }
    }

    private fun setLanguageRecycler(categoryList: MutableList<String>) {
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.languageRV.layoutManager = layoutManager

        languageAdapter = FilterAdapter(this, categoryList)
        binding.languageRV.adapter = languageAdapter
    }

//    private fun initSizesSpinners() {
//        val brandAdapter = ArrayAdapter(
//            this,
//            R.layout.simple_spinner_item, Constants.SIZES
//        )
//        binding.spinnerSizeFrom.adapter = brandAdapter
//        binding.spinnerSizeTo.adapter = brandAdapter
//
//        binding.spinnerSizeTo.setSelection(Constants.SIZES.lastIndex)
//
//        binding.spinnerSizeFrom.onItemSelectedListener = object :
//
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { }
//            override fun onNothingSelected(parent: AdapterView<*>) { }
//        }
//
//        binding.spinnerSizeTo.onItemSelectedListener = object :
//
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected( parent: AdapterView<*>, view: View, position: Int, id: Long) { }
//            override fun onNothingSelected(parent: AdapterView<*>) { }
//        }
//    }
}
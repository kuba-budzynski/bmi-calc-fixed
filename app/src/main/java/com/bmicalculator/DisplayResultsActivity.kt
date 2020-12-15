package com.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmicalculator.databinding.ResultBinding
import com.bmicalculator.recyclerview.Result
import com.bmicalculator.recyclerview.ResultAdapter
import db.DBHelper
import kotlinx.android.synthetic.main.results_view.*

class DisplayResultsActivity : AppCompatActivity() {

    private lateinit var  binding: ResultBinding
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_view)
        binding = ResultBinding.inflate(layoutInflater)
        setContentView(R.layout.results_view)
        db = DBHelper(this)
        val rows = db.numberOfRows()
        supportActionBar?.title = if(rows > 0) "Twoje pomiary" else "Brak wykonanych pomiarow"

        var results = db.getResults(10);
        var resultsToDisplay = results.map{ value -> Result(value.bmi,
            value.weight, value.height, value.date)}

        val adapter = ResultAdapter(resultsToDisplay)
        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvResults.adapter = adapter
            rvResults.layoutManager = layoutManager
        }
    }
}
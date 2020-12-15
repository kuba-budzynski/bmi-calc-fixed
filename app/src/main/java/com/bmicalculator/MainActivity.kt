package com.bmicalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bmi.BMI
import com.bmicalculator.databinding.ActivityMainBinding
import db.DBHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DBHelper

    private var lastHeight: Double? = null
    private var lastWeight: Double? = null
    private var lastUnits: String? = null
    private var lastBMI: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        // Run this to clear DB
        //db.deleteAllRows()

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultUnits = resources.getString(R.string.default_units)
        val current = sharedPref.getString("currentUnits", defaultUnits)

        binding.apply {
            if (current == "metric") {
                weightLabel.text = resources.getString(R.string.metric_weight)
                heightLabel.text = resources.getString(R.string.metric_height)
            } else {
                weightLabel.text = resources.getString(R.string.imperial_weight)
                heightLabel.text = resources.getString(R.string.imperial_height)
            }
            resultLabel.isClickable = false
            resultLabel.linksClickable = false
            resultLabel.isLongClickable = false
            resultLabel.isContextClickable = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.run {
            putString("WIDTH", weightInput.text.toString())
            putString("HEIGHT", heightInput.text.toString())
            putString("RESULT", resultLabel.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        weightInput.setText(savedInstanceState.getString("WIDTH"))
        heightInput.setText(savedInstanceState.getString("HEIGHT"))
        resultLabel.text = savedInstanceState.getString("RESULT")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultUnits = resources.getString(R.string.default_units)
        val current = sharedPref.getString("currentUnits", defaultUnits)
        when(item.itemId){
            R.id.change -> {
                binding.apply {
                    heightInput.setText("")
                    weightInput.setText("")
                    weightInput.clearFocus()
                    heightInput.clearFocus()
                    resultLabel.text = ""
                    if(current == "metric"){
                        with(sharedPref.edit()){
                            putString("currentUnits", "imperial")
                            apply()
                        }
                        weightLabel.text = resources.getString(R.string.imperial_weight)
                        heightLabel.text = resources.getString(R.string.imperial_height)
                        resultLabel.text = ""
                        resultLabel.isClickable = false
                        resultLabel.linksClickable = false
                        resultLabel.isLongClickable = false
                        resultLabel.isContextClickable = false
                    }
                    else{
                        with(sharedPref.edit()){
                            putString("currentUnits", "metric")
                            apply()
                        }
                        weightLabel.text = resources.getString(R.string.metric_weight)
                        heightLabel.text = resources.getString(R.string.metric_height)
                        resultLabel.text = ""
                        resultLabel.isClickable = false
                        resultLabel.linksClickable = false
                        resultLabel.isLongClickable = false
                        resultLabel.isContextClickable = false
                    }
                }
                Toast.makeText(this, "Units changed to: " + sharedPref.getString
                    ("currentUnits", defaultUnits), Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.results -> {
                val intent = Intent(this, DisplayResultsActivity::class.java).apply {
                    putExtra("units", current)
                }
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchColor(bmi: Double): Int{
        return when {
            bmi < 18.5 -> R.color.underweight
            bmi < 25 -> R.color.normal
            bmi < 30 -> R.color.overweight
            else -> R.color.obese
        }
    }

    fun count(view: View) {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultUnits = resources.getString(R.string.default_units)
        val current = sharedPref.getString("currentUnits", defaultUnits)

        binding.apply {

            var heightStr = heightInput.text.toString()
            var weightStr = weightInput.text.toString()

            if(checkConstraints(heightStr, weightStr, current)){

                var height = heightStr.toDouble()
                var weight = weightStr.toDouble()

                var res = BMI.calculate(weight, height, current)
                val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                var date = simpleDateFormat.format(Calendar.getInstance().time)

                resultLabel.text = res.toString()
                resultLabel.setTextColor(getColor(switchColor(res)))
                resultLabel.isClickable = true
                resultLabel.linksClickable = true
                resultLabel.isLongClickable = true
                resultLabel.isContextClickable = true

                lastHeight = height
                lastWeight = weight
                lastUnits = current
                lastBMI = res

                db.insertResult(res.toString(),
                    "${weight.toString()} ${if(current == "metric") "kg" else "lb"}"
                    , "${height.toString()} ${if(current == "metric") "cm" else "inch"}"
                    , date)
            }
            heightInput.setText("")
            weightInput.setText("")
            weightInput.clearFocus()
            heightInput.clearFocus()
        }
    }

    private fun checkConstraints(_height: String, _weight: String, units: String?): Boolean{

        var check = true
        val sb = StringBuilder()

        if(!_height.isNullOrEmpty() && !_weight.isNullOrEmpty()){
            var height = _height.toDouble()
            var weight = _weight.toDouble()

            if( units == "metric" && (height < 100 || height > 250)){
                sb.append(" height")
                check = false
            }
            else if( units == "imperial" && (height < 40 || height > 100)){
                sb.append(" height ")
                check = false
            }
            if(units == "metric" && (weight < 20 || weight > 500)){
                sb.append(" weight")
                check = false
            }
            else if(units == "imperial" && (weight < 45 || weight > 1100)){
                sb.append(" weight ")
                check = false
            }
            if(!check) {
                Toast.makeText(this, "Invalid\n" + sb.toString(), Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this, "Enter all required data", Toast.LENGTH_SHORT).show()
            check = false
        }
        return check
    }

    fun openMoreInfo(view: View){

        val intent = Intent(this, DetailsActivity::class.java)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultUnits = resources.getString(R.string.default_units)
        val current = sharedPref.getString("currentUnits", defaultUnits)
        intent.putExtra("bmi", lastBMI)
        intent.putExtra("weight", lastWeight)
        intent.putExtra("height", lastHeight)
        intent.putExtra("units", lastUnits)
        startActivityForResult(intent, 1);
    }
}
package com.bmicalculator.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmicalculator.R
import kotlinx.android.synthetic.main.result.view.*

class ResultAdapter (var results: List<Result>)
    : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>(){

    inner class ResultViewHolder(resultView: View) : RecyclerView.ViewHolder(resultView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result, parent, false)
        return ResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    private fun switchColor(bmi: Double): Int{
        return when {
            bmi < 18.5 -> R.color.underweight
            bmi < 25 -> R.color.normal
            bmi < 30 -> R.color.overweight
            else -> R.color.obese
        }
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.itemView.apply {
            bmiLabel.text = results[position].bmi
            bmiLabel.setTextColor(resources.getColor(switchColor((results[position].bmi).toDouble())))
            weightLabel.text = "waga: " + results[position].weight
            heightLabel.text = "wzrost: " + results[position].height
            dateLabel.text = results[position].data
        }
    }
}
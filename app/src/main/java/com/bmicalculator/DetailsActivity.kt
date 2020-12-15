package com.bmicalculator

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bmicalculator.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "BMI -> ${intent.getDoubleExtra("bmi", 0.0)}"
        binding.apply {
            val units = intent.getStringExtra("units")
            val bmi = intent.getDoubleExtra("bmi", 0.0)
            imageTop.setImageDrawable(switchImage(bmi))
            titleBMI.text = switchTitle(bmi)
            weightResult.text = "waga: ${intent.getDoubleExtra("weight", 0.0)}" +
                    "${if(units == "metric") "kg" else "lb"}"
            heightResult.text = "wzrost: ${intent.getDoubleExtra("height", 0.0)}" +
                    "${if(units == "metric") "cm" else "inch"}"
            bmiResult.text = "BMI: ${bmi}"
            opis.text = switchOpis(bmi)
        }
    }

    private fun switchColor(bmi: Double): Int{
        return when {
            bmi < 18.5 -> R.color.underweight
            bmi < 25 -> R.color.normal
            bmi < 30 -> R.color.overweight
            else -> R.color.obese
        }
    }

    private fun switchTitle(bmi: Double): String{
        return when {
            bmi < 18.5 -> getString(R.string.underweight)
            bmi < 25 -> getString(R.string.normal)
            bmi < 30 -> getString(R.string.overweight)
            else -> getString(R.string.obese)
        }
    }

    private fun switchOpis(bmi: Double): String{
        return when {
            bmi < 18.5 -> resources.getString(R.string.opis_underweight)
            bmi < 25 -> resources.getString(R.string.opis_normal)
            bmi < 30 -> resources.getString(R.string.opis_overweight)
            else -> resources.getString(R.string.opis_obese)
        }
    }

    private fun switchImage(bmi: Double): Drawable{
        return when {
            bmi < 18.5 -> resources.getDrawable(R.drawable.ic_underweight)
            bmi < 25 -> resources.getDrawable(R.drawable.ic_normal)
            bmi < 30 -> resources.getDrawable(R.drawable.ic_overweight)
            else -> resources.getDrawable(R.drawable.ic_obese)
        }
    }

}
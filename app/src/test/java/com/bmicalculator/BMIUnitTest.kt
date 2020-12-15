package com.bmicalculator

import bmi.BMI
import org.junit.Test
import org.junit.Assert.*
import java.lang.IllegalArgumentException
import java.security.InvalidParameterException

class BMIUnitTest {
    @Test
    fun bmiMetric() {
        assertEquals(30.86, BMI.calculate(100.0,180.0, "metric"), 0.01 )
    }

    @Test
    fun bmiImperial() {
        assertEquals(22.96, BMI.calculate(160.0,70.0, "imperial"), 0.01 )
    }

    @Test(expected = InvalidParameterException::class)
    fun bmiUnits() {
       BMI.calculate(160.0,70.0, "other")
    }

    @Test(expected = IllegalArgumentException::class)
    fun bmiParams() {
        BMI.calculate(100.0,0.0, "metric")
    }

}
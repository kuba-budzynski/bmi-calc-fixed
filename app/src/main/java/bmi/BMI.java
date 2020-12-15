package bmi;

import java.security.InvalidParameterException;

public class BMI {
    public static double calculate(double weight, double height, String unit)
            throws InvalidParameterException, IllegalArgumentException{

        if(weight <= 0 || height <= 0) throw new IllegalArgumentException();

        if(unit.equals("metric")){
            double res = (10000 * weight) / (height * height);
            return Math.round(res * 100.0) / 100.0;
        }
        else if(unit.equals("imperial")){
            double res = (703 * weight) / (height * height);
            return Math.round(res * 100.0) / 100.0;
        }
        else{
            throw new InvalidParameterException("No such unit");
        }
    }
}

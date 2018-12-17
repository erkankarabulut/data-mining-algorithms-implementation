package main.java.util;

public class FormatStringUtil {

    public String formatDoubleToRate(Double rate){
        if((rate * 100.0) < 10){
            return ((Double) (rate * 100.0)).toString().substring(0,4) + "%";
        }else if((rate * 100.0) < 100){
            return ((Double) (rate * 100.0)).toString().substring(0, 5) + "%";
        }else {
            return "100%";
        }
    }

}

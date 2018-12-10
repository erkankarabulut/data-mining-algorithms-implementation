package main.java.util;

import main.java.bean.Line;

import java.util.ArrayList;

public class MathUtil {

    public Double pi = new Double(3.14);
    public Double e  = new Double(2.718);

    public Double getVariance(Integer attributeOrder, ArrayList<Line> lines, Integer classLabel){
        Double variance     = new Double(0);
        Double mean         = getMeanValue(attributeOrder, lines, classLabel);
        Integer lineCount   = new Integer(0);

        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).getClassLabel() == classLabel){
                variance += (lines.get(i).getAttributeList().get(attributeOrder) - mean) * (lines.get(i).getAttributeList().get(attributeOrder) - mean);
                lineCount++;
            }
        }

        variance /= lineCount;

        return variance;
    }

    public Double getMeanValue(Integer attributeOrder, ArrayList<Line> lines, Integer classLabel){
        // The mean average
        Double mean         = new Double(0);
        Integer lineCount   = new Integer(0);

        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).getClassLabel() == classLabel){
                mean += lines.get(i).getAttributeList().get(attributeOrder);
                lineCount++;
            }
        }

        mean /= lineCount;

        return mean;
    }
}

package main.java.algorithms;

import main.java.bean.Line;
import main.java.controller.MainController;
import main.java.util.MathUtil;

import java.util.ArrayList;
import java.util.Random;

public class NaivaBayesAlgorithm {

    MathUtil mathUtil;

    public NaivaBayesAlgorithm(){
        mathUtil = new MathUtil();
    }

    // This is a naive bayes classifier implementation for continious attributes
    public void applyNaiveBayesAlgorithm(MainController controller, ArrayList<Line> lines){
        Integer tp = new Integer(0);
        Integer fp = new Integer(0);
        Integer tn = new Integer(0);
        Integer fn = new Integer(0);

        ArrayList<Double> classLabelRates = findClassLabelRates(lines);
        Double zeroRate = classLabelRates.get(0);
        Double oneRate  = classLabelRates.get(1);

        ArrayList splittedDatasets = splitDataset(lines, controller.getTestDataRate(), controller.getTrainingDataRate());
        ArrayList<Line> training   = (ArrayList<Line>) splittedDatasets.get(0);
        ArrayList<Line> test       = (ArrayList<Line>) splittedDatasets.get(1);

        ArrayList<Double> meanListForZero        = new ArrayList<Double>();
        ArrayList<Double> varianceListForZero    = new ArrayList<Double>();
        ArrayList<Double> meanListForOne        = new ArrayList<Double>();
        ArrayList<Double> varianceListForOne    = new ArrayList<Double>();

        for(int i=0; i<lines.get(0).getAttributeList().size(); i++){
            meanListForZero.add(mathUtil.getMeanValue(i, training, 0));
            meanListForOne.add(mathUtil.getMeanValue(i, training, 1));

            varianceListForOne.add(mathUtil.getVariance(i, training, 1));
            varianceListForZero.add(mathUtil.getVariance(i, training, 0));
        }

        for(Line line : test){
            Double zeroProbability = new Double(1);
            Double oneProbability  = new Double(1);
            Double mean            = new Double(0);
            Double variance        = new Double(0);

            for(int i=0; i<line.getAttributeList().size(); i++){
                mean      = meanListForZero.get(i);
                variance  = varianceListForZero.get(i);
                zeroProbability *= ((1 / (Math.sqrt(2 * mathUtil.pi * (variance * variance))))
                        * Math.pow(mathUtil.e, -((line.getAttributeList().get(i) - mean) / (2 * variance * variance))));
            }
            zeroProbability *= zeroRate;

            for(int i=0; i<line.getAttributeList().size(); i++){
                mean      = meanListForOne.get(i);
                variance  = varianceListForOne.get(i);
                oneProbability *= ((1 / (Math.sqrt(2 * mathUtil.pi * (variance * variance))))
                        * Math.pow(mathUtil.e, -((line.getAttributeList().get(i) - mean) / (2 * variance * variance))));
            }
            oneProbability *= oneRate;

            Integer tempResult = (oneProbability > zeroProbability ? 1 : 0);
            if(findClassLabel(lines, line.getAttributeList()) == 0){
                if(tempResult == findClassLabel(lines, line.getAttributeList())){
                    tn++;
                }else {
                    fn++;
                }
            }else {
                if(tempResult == findClassLabel(lines, line.getAttributeList())){
                    tp++;
                }else {
                    fp++;
                }
            }
        }

        System.out.println("Metrics: " + tn + " - " + tp + " - " + fn + " - " + fp);
    }

    public Integer findClassLabel(ArrayList<Line> lines, ArrayList<Double> values){
        Integer result = new Integer(-1);
        for(Line line : lines){
            if(line.getAttributeList() == values){
                result = line.getClassLabel();
                break;
            }
        }

        return result;
    }

    public ArrayList<Double> findClassLabelRates(ArrayList<Line> lines){
        ArrayList<Double> results    = new ArrayList<Double>();
        Integer zeroCounter         = new Integer(0);
        for (Line line : lines){
            if(line.getClassLabel() == 0){
                zeroCounter++;
            }
        }

        results.add((zeroCounter.doubleValue() / lines.size())); // Add possibility of being 0 in the data
        results.add((lines.size()-zeroCounter.doubleValue()) / lines.size()); // Add possibility of being 1 in the data

        return results;
    }

    public ArrayList<ArrayList<Line>> splitDataset(ArrayList<Line> lines, Integer testRate, Integer trainingRate){
        ArrayList result = new ArrayList();

        Integer randomPickupCount = new Integer(0);
        if(testRate > trainingRate){
            randomPickupCount = lines.size() * trainingRate / 100;
        }else {
            randomPickupCount = lines.size() * testRate / 100;
        }

        ArrayList<Line> dataset1        = new ArrayList<Line>();
        ArrayList<Line> dataset2        = new ArrayList<Line>();
        ArrayList<Integer> choosenLines = new ArrayList<Integer>();
        for (int i=0; i<randomPickupCount; i++){
            Random random = new Random();
            int n = random.nextInt(lines.size()-1);
            while (choosenLines.contains(n)){
                n = random.nextInt(lines.size()-1);
            }

            choosenLines.add(n);
            dataset1.add(lines.get(n));
        }

        for(int i=0; i<lines.size(); i++){
            if(!choosenLines.contains(i)){
                dataset2.add(lines.get(i));
            }
        }

        if(testRate > trainingRate){
            result.add(dataset1);
            result.add(dataset2);
        }else {
            result.add(dataset2);
            result.add(dataset1);
        }

        return result;
    }

}

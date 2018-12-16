package main.java.algorithms;

import main.java.bean.Line;
import main.java.controller.MainController;
import main.java.util.MathUtil;

import java.util.ArrayList;

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

        Integer crossValidationCount = controller.getCrossValidOrClusterCount();
        for(int c=0; c<crossValidationCount; c++){
            ArrayList splittedDatasets = splitDataset(lines, c, crossValidationCount);
            ArrayList<Line> training   = lines;
            ArrayList<Line> test       = (ArrayList<Line>) splittedDatasets.get(1);

            ArrayList<Double> classLabelRates = findClassLabelRates(training);
            Double zeroRate = classLabelRates.get(0);
            Double oneRate  = classLabelRates.get(1);

            ArrayList<Double> meanListForZero       = new ArrayList<Double>();
            ArrayList<Double> varianceListForZero   = new ArrayList<Double>();
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

                zeroProbability = calculateAttributepossilibities(meanListForZero, varianceListForZero, line, zeroProbability);
                zeroProbability *= zeroRate;

                oneProbability = calculateAttributepossilibities(meanListForOne, varianceListForOne, line, oneProbability);
                oneProbability *= oneRate;

                Integer tempResult = (zeroProbability > oneProbability ? 0 : 1);
                if(line.getClassLabel() == 0){
                    if(tempResult == 0){
                        tp++;
                    }else {
                        fn++;
                    }
                }else {
                    if(tempResult == 1){
                        tn++;
                    }else {
                        fp++;
                    }
                }

            }
        }

        controller.setAccuracy(((tp.doubleValue() + tn.doubleValue()) / (tp.doubleValue() + fp.doubleValue() + fn.doubleValue() + tn.doubleValue())));
        controller.setPrecision((tp.doubleValue()/(tp.doubleValue() + fp.doubleValue())));
        controller.setRecall((tp.doubleValue()/(tp.doubleValue()+fn.doubleValue())));

        controller.getResultLabel().setText("Algorithm: " + controller.getSelectAlgorithmComboBox().getSelectionModel().getSelectedItem().toString() + "\n"
            + "Test Data Rate: " + controller.getTestDataRate() + "\nTraining Data Rate: " + controller.getTrainingDataRate()
            + "\nFold count: " + controller.getCrossValidOrClusterCount() + "\nAccuracy: " + controller.getAccuracy()
            + "\nAccuracy: " + controller.getAccuracy() + "\nPrecision: " + controller.getPrecision() + "\nRecall: " + controller.getRecall());
    }

    private Double calculateAttributepossilibities(ArrayList<Double> meanListForOne, ArrayList<Double> varianceListForOne,
                                                   Line line, Double probability) {
        Double mean;
        Double variance;
        for(int i = 0; i<line.getAttributeList().size(); i++){
            mean      = meanListForOne.get(i);
            variance  = varianceListForOne.get(i);
            probability *= ((1 / (Math.sqrt(2 * mathUtil.pi * (variance * variance))))
                    * Math.pow(mathUtil.e, -((line.getAttributeList().get(i) - mean) / (2 * variance * variance))));
            if(probability == 0 || Double.isInfinite(probability)){
                break;
            }
        }
        return probability;
    }

    public ArrayList<Double> findClassLabelRates(ArrayList<Line> lines){
        ArrayList<Double> results    = new ArrayList<Double>();
        Integer zeroCounter          = new Integer(0);
        for (Line line : lines){
            if(line.getClassLabel() == 0){
                zeroCounter++;
            }
        }

        results.add((zeroCounter.doubleValue() / lines.size())); // Add possibility of being 0 in the data
        results.add((lines.size()-zeroCounter.doubleValue()) / lines.size()); // Add possibility of being 1 in the data

        return results;
    }

    public ArrayList<ArrayList<Line>> splitDataset(ArrayList<Line> lines, Integer crossValidationPointer, Integer crossValidationCount){
        ArrayList result = new ArrayList();

        ArrayList<Line> test            = new ArrayList<Line>();
        ArrayList<Line> training        = new ArrayList<Line>();
        Integer testDatasetSize         = lines.size() / crossValidationCount;
        Integer startPoint              = testDatasetSize * crossValidationPointer;

        for(int i=0; i<testDatasetSize; i++){
            test.add(lines.get(startPoint));
            startPoint++;
        }

        startPoint = testDatasetSize * crossValidationPointer;
        for(int i=0; i<lines.size(); i++){
            if(i != startPoint){
                training.add(lines.get(i));
            }else {
                i += testDatasetSize - 1;
            }
        }

        result.add(training);
        result.add(test);

        return result;
    }

}

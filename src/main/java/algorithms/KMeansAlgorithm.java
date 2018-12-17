package main.java.algorithms;

import main.java.bean.Line;
import main.java.controller.MainController;
import main.java.util.FormatStringUtil;
import main.java.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KMeansAlgorithm {

    private MathUtil mathUtil;
    private FormatStringUtil formatStringUtil;
    private static final Integer maxIteration = 20;

    public KMeansAlgorithm(){
        this.mathUtil           = new MathUtil();
        this.formatStringUtil   = new FormatStringUtil();
    }

    public void applyKMeansAlgorithm(MainController controller, ArrayList<Line> lines, Integer clusterCount){
        ArrayList<Line> centroids                   = getRandomCentroids(lines, clusterCount);
        HashMap<Integer, ArrayList<Line>> labels    = new HashMap<Integer, ArrayList<Line>>();

        Integer counter                 = new Integer(0);
        ArrayList<Line> oldCentroids    = new ArrayList<Line>();

        while (!shouldStop(oldCentroids, centroids, counter)){
            oldCentroids    = centroids;
            counter         = counter + 1;

            labels          = getLabels(lines, centroids);
            centroids       = getNewCentroids(labels);
        }

        Double tp = new Double(0);
        Double fp = new Double(0);
        Double tn = new Double(0);
        Double fn = new Double(0);

        // Since the cluster with 0 label has more nodes in our dataset, let the labels with higher data size be the 0.
        if(labels.get(1).size() > labels.get(0).size()){
            ArrayList<Line> temp = labels.get(0);
            labels.put(0, labels.get(1));
            labels.put(1, temp);
        }

        for (int i=0; i<labels.size(); i++){
            for(int k=0; k<labels.get(i).size(); k++){
                if(i == 0){
                    if(labels.get(i).get(k).getClassLabel() == i){
                        tp++;
                    }else {
                        fn++;
                    }
                }else if(i == 1){
                    if(labels.get(i).get(k).getClassLabel() == i){
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

        controller.getResultLabel().setText("Algorithm: " + controller.getSelectAlgorithmComboBox().getSelectionModel().getSelectedItem().toString()
                + "\nCluster count: 2\nCluster 0 size: " + labels.get(0).size() + "\nCluster 1 size: " + labels.get(1).size()
                + "\nMax Iteration: 20" + "\nAccuracy: " + formatStringUtil.formatDoubleToRate(controller.getAccuracy())
                + "\nPrecision: " + formatStringUtil.formatDoubleToRate(controller.getPrecision()) + "\nRecall: " + formatStringUtil.formatDoubleToRate(controller.getRecall()));

        System.out.println("Algorithm: " + controller.getSelectAlgorithmComboBox().getSelectionModel().getSelectedItem().toString()
                + "\nCluster count: 2\nCluster 0 size: " + labels.get(0).size() + "\nCluster 1 size: " + labels.get(1).size()
                + "\nMax Iteration: 20" + "\nAccuracy: " + controller.getAccuracy()
                + "\nPrecision: " + controller.getPrecision() + "\nRecall: " + controller.getRecall());
    }

    public ArrayList<Line> getNewCentroids(HashMap<Integer, ArrayList<Line>> labels){
        ArrayList<Line> centroids = new ArrayList<Line>();
        for(int i=0; i<labels.size(); i++){
            ArrayList<Line> nodeList = labels.get(i);
            Line newCentroid = new Line();
            for(int j=0; j<nodeList.get(0).getAttributeList().size(); j++){
                newCentroid.getAttributeList().add(getMean(j, nodeList));
            }

            centroids.add(newCentroid);
        }

        return centroids;
    }

    public Double getMean(Integer attributePointer, ArrayList<Line> nodeList){
        Double mean = new Double(0);
        for(Line line : nodeList){
            mean += line.getAttributeList().get(attributePointer);
        }

        mean /= nodeList.size();
        return mean;
    }

    public HashMap<Integer, ArrayList<Line>> getLabels(ArrayList<Line> lines, ArrayList<Line> centroids){
        HashMap<Integer, ArrayList<Line>> labels = new HashMap<Integer, ArrayList<Line>>();
        Integer centroidPointer = 0;
        for(Line temp : centroids){
            labels.put(centroidPointer++, new ArrayList<Line>());
        }

        for(Line line: lines){
            Double minDistance   = new Double(99999999);
            centroidPointer      = 0;
            for(Integer i=0; i<centroids.size(); i++){
                Double distance = findDistance(centroids.get(i), line);
                if(distance < minDistance){
                    minDistance     = distance;
                    centroidPointer = i;
                }
            }

            labels.get(centroidPointer).add(line);
        }


        return labels;
    }

    public Double findDistance(Line node1, Line node2){
        Double distance = new Double(0);
        for(int i=0; i<node1.getAttributeList().size(); i++){
            distance += Math.pow(Math.abs(node1.getAttributeList().get(i) - node2.getAttributeList().get(i)), 2);
        }

        return Math.sqrt(distance);
    }

    public Boolean shouldStop(ArrayList<Line> oldCentroids, ArrayList<Line> centroids, Integer counter){
        if(counter >= maxIteration){
            return true;
        }

        return doesCentroidsEqual(oldCentroids, centroids);
    }

    public Boolean doesCentroidsEqual(ArrayList<Line> oldCentroids, ArrayList<Line> centroids){
        Boolean result = true;
        if(centroids.size() != oldCentroids.size()){
            result = false;
        }else {
            for(int i=0; i<oldCentroids.size(); i++){
                if(!doesNodesSame(oldCentroids.get(i).getAttributeList(), centroids.get(i).getAttributeList())){
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public Boolean doesNodesSame(ArrayList<Double> node1, ArrayList<Double> node2){
        Boolean result = true;
        node1.removeAll(node2);

        if(node1.size() != 0){
            result = false;
        }

        return result;
    }

    public ArrayList<Line> getRandomCentroids(ArrayList<Line> lines, Integer clusterCount){
        ArrayList<Line> centroids   = new ArrayList<Line>();
        Random random               = new Random();

        for(int i=0; i<clusterCount; i++){
            Integer clusterPointer = random.nextInt(lines.get(0).getAttributeList().size());
            while (doesClusterChoosedBefore(clusterPointer, centroids)){
                clusterPointer = random.nextInt(lines.get(0).getAttributeList().size());
            }

            centroids.add(lines.get(clusterPointer));
        }

        return centroids;
    }

    public Boolean doesClusterChoosedBefore(Integer clusterPointer, ArrayList<Line> centroids){
        Boolean result = false;
        for(Line line : centroids){
            if(line.getOrder() == clusterPointer){
                result = true;
                break;
            }
        }

        return result;
    }

}

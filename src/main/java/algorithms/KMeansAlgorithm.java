package main.java.algorithms;

import main.java.bean.Line;
import main.java.controller.MainController;
import main.java.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KMeansAlgorithm {

    private MathUtil mathUtil;
    private static final Integer maxIteration = 20;

    public KMeansAlgorithm(){
        this.mathUtil       = new MathUtil();
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

        System.out.println("Counter: " + counter);
        for (int i=0; i<labels.size(); i++){
            System.out.println("Centroid " + i + " node count: " + labels.get(i).size());
        }
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

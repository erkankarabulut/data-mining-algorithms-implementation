package main.java.bean;

import java.util.ArrayList;

public class Line {

    private ArrayList<Double> attributeList;
    private Integer classLabel;
    private Integer order;

    public Line(){
        attributeList = new ArrayList<Double>();
    }

    public ArrayList<Double> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(ArrayList<Double> attributeList) {
        this.attributeList = attributeList;
    }

    public Integer getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(Integer classLabel) {
        this.classLabel = classLabel;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}

package main.java.util;

import main.java.bean.Line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileReaderUtil {

    public ArrayList<Line> readLogFile(String path){
        ArrayList<Line> lineList = new ArrayList<Line>();

        try{
            BufferedReader br   = new BufferedReader(new FileReader(new File(path)));
            String lineContent         = new String();
            while (!(lineContent = br.readLine()).equals("@data")){
                // Skip the attribute names etc. parts
            }


            while ((lineContent = br.readLine()) != null){
                String values[] = lineContent.split(",");
                Line line = new Line();
                for(int i=0; i<values.length-1; i++){
                    line.getAttributeList().add(Double.parseDouble(values[i]));
                }

                line.setClassLabel(Integer.parseInt(values[values.length-1]));
                lineList.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return lineList;
    }

}

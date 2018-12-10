package main.java.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.algorithms.NaivaBayesAlgorithm;
import main.java.bean.Line;
import main.java.util.FileReaderUtil;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML Button chooseLogFileButton;
    @FXML Button runButton;

    @FXML ComboBox selectAlgorithmComboBox;

    @FXML ImageView ytuLogo;

    @FXML Spinner testRate;
    @FXML Spinner iterationCount;

    @FXML Label fileName;
    @FXML Label algorithmName;
    @FXML Label trainingDataRateLabel;
    @FXML Label testDataRateLabel;
    @FXML Label resultLabel;
    @FXML Label errorMessageLabel;
    @FXML Label iterationCountLabel;

    private String logFileName;
    private String logFilePath;
    private String fileWithBinaryLabelsPath;
    private String sparseVectorFilePath;
    private String fileWithMultiClassLabelsPath;

    private Integer algorithmPointer;
    private Integer testDataRate;
    private Integer trainingDataRate;
    private Integer iterationCountValue;

    private Double accuracy;
    private Double recall;
    private Double precision;

    private FileReaderUtil fileReaderUtil;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testDataRate             = 20;
        trainingDataRate         = 80;
        iterationCountValue      = 100;
        fileReaderUtil           = new FileReaderUtil();

        trainingDataRateLabel.setText(trainingDataRate.toString());
        testDataRateLabel.setText(testDataRate.toString());
        resultLabel.setText("Please choose a data file and an algorithm then run the program to see the results.");

        ytuLogo.setImage(new Image(getClass().getResourceAsStream("../../resources/ytu.png")));
        testRate.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99));
        testRate.getValueFactory().setValue(new Integer(20));
        testRate.setEditable(true);

        iterationCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99999));
        iterationCount.getValueFactory().setValue(new Integer(100));
        iterationCountLabel.setText(new Integer(100).toString());
        iterationCount.setEditable(true);

        chooseLogFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    logFileName = chooser.getSelectedFile().getName();
                    logFilePath = chooser.getSelectedFile().getPath();

                    fileName.setText(logFileName);
                }
            }
        });

        selectAlgorithmComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                algorithmPointer = selectAlgorithmComboBox.getSelectionModel().getSelectedIndex();
                algorithmName.setText(selectAlgorithmComboBox.getSelectionModel().getSelectedItem().toString());
            }
        });

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(selectAlgorithmComboBox.getSelectionModel().getSelectedIndex() >= 0 && logFileName != null){
                    ArrayList<Line> lineList = fileReaderUtil.readLogFile(logFilePath);
                    if(algorithmPointer == 0){
                        NaivaBayesAlgorithm naivaBayesAlgorithm = new NaivaBayesAlgorithm();
                        naivaBayesAlgorithm.applyNaiveBayesAlgorithm(getInstance(), lineList);
                    }
                }else {
                    errorMessageLabel.setText("Warning: Please choose a log file and an algorithm.");
                }

            }
        });

        testRate.getValueFactory().valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                setTestDataRate(Integer.parseInt(t1.toString()));;
                setTrainingDataRate(Math.abs(100 - Integer.parseInt(t1.toString())));

                trainingDataRateLabel.setText(getTrainingDataRate().toString());
                testDataRateLabel.setText(getTestDataRate().toString());
            }
        });

        iterationCount.getValueFactory().valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                setIterationCountValue(Integer.parseInt(t1.toString()));
                iterationCountLabel.setText(t1.toString());
            }
        });
    }

    public Integer getTestDataRate() {
        return testDataRate;
    }

    public void setTestDataRate(Integer testDataRate) {
        this.testDataRate = testDataRate;
    }

    public Integer getTrainingDataRate() {
        return trainingDataRate;
    }

    public void setTrainingDataRate(Integer trainingDataRate) {
        this.trainingDataRate = trainingDataRate;
    }

    public MainController getInstance(){
        return this;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getRecall() {
        return recall;
    }

    public void setRecall(Double recall) {
        this.recall = recall;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public Integer getIterationCountValue() {
        return iterationCountValue;
    }

    public void setIterationCountValue(Integer iterationCountValue) {
        this.iterationCountValue = iterationCountValue;
    }

    public String getFileWithBinaryLabelsPath() {
        return fileWithBinaryLabelsPath;
    }

    public void setFileWithBinaryLabelsPath(String fileWithBinaryLabelsPath) {
        this.fileWithBinaryLabelsPath = fileWithBinaryLabelsPath;
    }

    public String getSparseVectorFilePath() {
        return sparseVectorFilePath;
    }

    public void setSparseVectorFilePath(String sparseVectorFilePath) {
        this.sparseVectorFilePath = sparseVectorFilePath;
    }

    public String getFileWithMultiClassLabelsPath() {
        return fileWithMultiClassLabelsPath;
    }

    public void setFileWithMultiClassLabelsPath(String fileWithMultiClassLabelsPath) {
        this.fileWithMultiClassLabelsPath = fileWithMultiClassLabelsPath;
    }
}

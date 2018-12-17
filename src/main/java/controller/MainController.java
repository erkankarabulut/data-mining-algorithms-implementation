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
import main.java.algorithms.KMeansAlgorithm;
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
    @FXML Button chooseTestFileButton;

    @FXML CheckBox checkBox;

    @FXML ComboBox selectAlgorithmComboBox;

    @FXML ImageView ytuLogo;

    @FXML Spinner crossValidOrCluster;

    @FXML Label fileName;
    @FXML Label algorithmName;
    @FXML Label resultLabel;
    @FXML Label errorMessageLabel;
    @FXML Label crossValidationCountLabel;
    @FXML Label parameterLabel;
    @FXML Label parameterLabel2;
    @FXML Label testSetFileName;

    private String logFileName;
    private String logFilePath;
    private String fileWithBinaryLabelsPath;
    private String sparseVectorFilePath;
    private String fileWithMultiClassLabelsPath;
    private String testFileName;
    private String testFilePath;

    private Integer algorithmPointer;
    private Integer testDataRate;
    private Integer trainingDataRate;
    private Integer crossValidOrClusterCount;

    private Double accuracy;
    private Double recall;
    private Double precision;

    private FileReaderUtil fileReaderUtil;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testDataRate                = 20;
        trainingDataRate            = 80;
        crossValidOrClusterCount    = 10;
        fileReaderUtil              = new FileReaderUtil();

        resultLabel.setText("Please choose a data file and an algorithm then run the program to see the results.");
        ytuLogo.setImage(new Image(getClass().getResourceAsStream("../../resources/ytu.png")));
        selectAlgorithmComboBox.getSelectionModel().select(0);
        algorithmPointer = selectAlgorithmComboBox.getSelectionModel().getSelectedIndex();
        algorithmName.setText(selectAlgorithmComboBox.getSelectionModel().getSelectedItem().toString());

        crossValidOrCluster.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99999));
        crossValidOrCluster.getValueFactory().setValue(new Integer(10));
        crossValidationCountLabel.setText(new Integer(10).toString());
        crossValidOrCluster.setEditable(true);

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

                if(algorithmPointer == 0){
                    parameterLabel.setText("Cross-validation folds: ");
                    parameterLabel2.setText("Cross-validation folds: ");
                }else if(algorithmPointer == 1){
                    parameterLabel.setText("Cluster count: ");
                    parameterLabel2.setText("Cluster count: ");
                }
            }
        });

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(selectAlgorithmComboBox.getSelectionModel().getSelectedIndex() >= 0 && logFileName != null){
                    ArrayList<Line> lineList = fileReaderUtil.readLogFile(logFilePath);
                    if (algorithmPointer == 0){
                        NaivaBayesAlgorithm naivaBayesAlgorithm = new NaivaBayesAlgorithm();
                        // In here, crossValidOrCluster represents fold count
                        naivaBayesAlgorithm.applyNaiveBayesAlgorithm(getInstance(), lineList);
                    }else if (algorithmPointer == 1){
                        KMeansAlgorithm kMeansAlgorithm = new KMeansAlgorithm();
                        // Here, crossValidOrCluster represents cluster count
                        kMeansAlgorithm.applyKMeansAlgorithm(getInstance(), lineList, crossValidOrClusterCount);
                    }
                }else {
                    errorMessageLabel.setText("Warning: Please choose a log file and an algorithm.");
                }

            }
        });

        crossValidOrCluster.getValueFactory().valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                setCrossValidOrClusterCount(Integer.parseInt(t1.toString()));
                crossValidationCountLabel.setText(t1.toString());
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

    public Integer getCrossValidOrClusterCount() {
        return crossValidOrClusterCount;
    }

    public void setCrossValidOrClusterCount(Integer crossValidOrClusterCount) {
        this.crossValidOrClusterCount = crossValidOrClusterCount;
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

    public Label getResultLabel() {
        return resultLabel;
    }

    public void setResultLabel(Label resultLabel) {
        this.resultLabel = resultLabel;
    }

    public ComboBox getSelectAlgorithmComboBox() {
        return selectAlgorithmComboBox;
    }

    public void setSelectAlgorithmComboBox(ComboBox selectAlgorithmComboBox) {
        this.selectAlgorithmComboBox = selectAlgorithmComboBox;
    }
}

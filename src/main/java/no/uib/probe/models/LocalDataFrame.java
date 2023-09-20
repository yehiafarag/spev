/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.models;

import java.util.LinkedHashMap;
import java.util.Map;
import smile.data.DataFrame;

/**
 *
 * @author yfa041
 */
public class LocalDataFrame {
    private DataFrame dataFrame;
    private double[][] data;
    private Double[][] columnsAsRows;
    
    private double[][] scaledData;
    private Double[][] scaledColumnsAsRows;
    
    private double[] tagsEValues;
    private final Map<String,Integer>dataMapToIndex=new LinkedHashMap<>();
    private Map<String, SpectrumModel> spectraMap;

    public Map<String, SpectrumModel> getSpectraMap() {
        return spectraMap;
    }

    public void setSpectraMap(Map<String, SpectrumModel> spectraMap) {
        this.spectraMap = spectraMap;
    }

    public int getSpectrumIndex(String key) {
        return dataMapToIndex.get(key);
    }

    public void addDataMapToIndex(String key,Integer index) {
        this.dataMapToIndex.put(key, index);
    }
    
    

    
    
    public double[] getTagsEValues() {
        return tagsEValues;
    }

    public void setTagsEValues(double[] tagsEValues) {
        this.tagsEValues = tagsEValues;
    }

    public Double[][] getColumnsAsRows() {
        return columnsAsRows;
    }

    public void setColumnsAsRows(Double[][] columnsAsRows) {
        this.columnsAsRows = columnsAsRows;
    }
    private String[] columnId;
    private String[] rowsId;
    private  String[] dataClass;

    public String[] getDataClass() {
        return dataClass;
    }

    public void setDataClass(String[] dataClass) {
        this.dataClass = dataClass;
    }

    public DataFrame getDataFrame() {
        return dataFrame;
    }

    public void setDataFrame(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public String[] getColumnId() {
        return columnId;
    }

    public void setColumnId(String[] columnId) {
        this.columnId = columnId;
    }

    public String[] getRowsId() {
        return rowsId;
    }

    public void setRowsId(String[] rowsId) {
        this.rowsId = rowsId;
    }

    public double[][] getScaledData() {
        return scaledData;
    }

    public void setScaledData(double[][] scaledData) {
        this.scaledData = scaledData;
    }

    public Double[][] getScaledColumnsAsRows() {
        return scaledColumnsAsRows;
    }

    public void setScaledColumnsAsRows(Double[][] scaledColumnsAsRows) {
        this.scaledColumnsAsRows = scaledColumnsAsRows;
    }
    
}

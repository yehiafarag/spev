/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.handlers;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import no.uib.probe.models.LocalDataFrame;
import no.uib.probe.models.MachineLearningDataset;
import no.uib.probe.models.SpectrumModel;
import no.uib.probe.plots.BoxAndWhisker;
import no.uib.probe.utils.Parameters;
import no.uib.probe.utils.Util;
import no.uib.probe.utils.statistics.SNRCalculator;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.feature.transform.WinsorScaler;
import smile.plot.swing.ScatterPlot;

/**
 *
 * @author yfa041
 */
public class DataProcessHandler {

    public static void analyseDataCategory(Set<String>[][] classfiedDataMatrix, Map<String, SpectrumModel> fullSpectra) {

        Map<String, LocalDataFrame> dataCateogories = new LinkedHashMap<>();
        int count = 0;
        int i = 0;
        for (Set<String>[] cats : classfiedDataMatrix) {
            int j = 0;

            for (Set<String> cat : cats) {
                count += cat.size();
                String catId = "Cat " + i + "-" + j;
                Map<String, SpectrumModel> catMap = new HashMap<>();
                for (String key : cat) {
                    fullSpectra.get(key).setIdClass(catId);
                    catMap.put(key, fullSpectra.get(key));
                }
                LocalDataFrame dataset = spectraMapToDataFrame(catMap);
                dataCateogories.put(catId, dataset);
                System.out.println("at data category " + catId + "  number " + catMap.size());
                j++;
            }
            i++;

        }
        System.out.println("at total is " + count + "   full size " + fullSpectra.size());
        Set<Integer> unScaledColumnIndex = new HashSet<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 9, 11, 12, 13, 14, 20}));

        for (int columnIndex = 1; columnIndex < dataCateogories.values().iterator().next().getColumnId().length - 1; columnIndex++) {

            Map<String, List<Double>> inputData = new LinkedHashMap<>();
            for (String key : dataCateogories.keySet()) {
                LocalDataFrame ds = dataCateogories.get(key);
                List<Double> values = new ArrayList<>();
                Double[] columnValues;
                if (unScaledColumnIndex.contains(columnIndex)) {
                    columnValues = ds.getColumnsAsRows()[columnIndex - 1];
                } else {
                    columnValues = ds.getScaledColumnsAsRows()[columnIndex - 1];
                }
                values.addAll(Arrays.asList(columnValues));
                inputData.put(key, values);

            }
            System.out.println("at column index " + columnIndex + "  " + dataCateogories.values().iterator().next().getColumnId()[columnIndex]);
            BoxAndWhisker bp = new BoxAndWhisker(inputData, "Compare categories for column " + dataCateogories.values().iterator().next().getColumnId()[columnIndex]);
        }

    }

    public static LocalDataFrame spectraMapToDataFrame(Map<String, SpectrumModel> spectraMap, Map<String, Double[]> directTagIdentifications) {
        double[][] oreginalData = new double[spectraMap.size()][22];
        String[] dataId = new String[spectraMap.size()]; 
        LocalDataFrame localDataFrame = new LocalDataFrame();
        localDataFrame.setSpectraMap(spectraMap);
        //1     2                   3                   4       5       6       7       8           9           10      11      12              13                      14                  15                  16          17                  18              19      2           21          22          23              24  
        String[] columnIds = new String[]{"ID", "TheoreticalCharge", "TheoreticalMass", "Mean", "Median", "SD", "Skews", "Kurtosis", "Variance", "Range", "IQR", "PeaksNumber", "AveragePeakIntensity", "TotalIntensity", "PeakIntensityRatios", "AUC", "CoefficientVariations", "PeakSNR90", "PeakSNR70", "SNR90", "SNR70", "DynamicRange", "Tags-E-value", "Class"};
        String[] dataClass = new String[spectraMap.size()];
        int i = 0;
        for (String spectrumTitle : spectraMap.keySet()) {
            dataId[i] = spectrumTitle;
            SpectrumModel spectrum = spectraMap.get(spectrumTitle);
            if (directTagIdentifications.containsKey(spectrumTitle)) {
                spectrum.setTagsEValues(directTagIdentifications.get(spectrumTitle));
                double minE = 100;
                for (int k = 0; k < spectrum.getTagsEValues().length; k++) {
                    if (spectrum.getTagsEValues()[k] == null) {
                        spectrum.getTagsEValues()[k] = 100.0;
                    }
                    minE = Math.min(minE, spectrum.getTagsEValues()[k]);
                }
                spectrum.setTagEvalue(minE);
            }

            dataClass[i] = spectrum.getIdClass();
            double[] rowData = new double[]{spectrum.getTheoreticalCharge(), spectrum.getTheoreticalMass(), spectrum.getMean(), spectrum.getMedian(), spectrum.getSd(), spectrum.getSkews(), spectrum.getKurtosis(), spectrum.getVariance(), spectrum.getRange(), spectrum.getIQR(), spectrum.getNumPeaks(), spectrum.getAveragePeakIntensity(), spectrum.getTotalIntensity(), spectrum.getPeakIntensityRatios(), spectrum.getAuc(), spectrum.getCoefficientVariations(), spectrum.getPeakSNR90(), spectrum.getPeakSNR70(), spectrum.getSNR90(), spectrum.getSNR70(), spectrum.getDynamicRange(), spectrum.getTagEvalue()};
            oreginalData[i] = rowData;
            localDataFrame.addDataMapToIndex(spectrumTitle, i);
            
            i++;
        }
        //covnert data rows to columns
        Double[][] columnsAsData = new Double[oreginalData[0].length][oreginalData.length];
        for (int ci = 0; ci < oreginalData.length; ci++) {
            for (int j = 0; j < oreginalData[0].length; j++) {
                columnsAsData[j][ci] = oreginalData[ci][j];
            }
        }

        DataFrame dataframe = MachineLearningDataHandler.initDataFrame(oreginalData, dataId, columnIds, dataClass);
        Formula formula = Formula.lhs("Class");
        DataFrame train = dataframe.drop(0).factorize("Class");
        var scaler = WinsorScaler.fit(train, 0.01, 0.99);
        DataFrame scaledDataFrame = scaler.apply(train);
        double[][] scaledData = formula.x(scaledDataFrame).toArray();
        //covnert data rows to columns
        Double[][] scaledColumnsAsData = new Double[scaledData[0].length][scaledData.length];
        for (int ci = 0; ci < scaledData.length; ci++) {
            for (int j = 0; j < scaledData[0].length; j++) {
                scaledColumnsAsData[j][ci] = scaledData[ci][j];
            }
        }

       
        localDataFrame.setColumnId(columnIds);
        localDataFrame.setRowsId(dataId);
        localDataFrame.setData(oreginalData);
        localDataFrame.setDataFrame(dataframe);
        localDataFrame.setDataClass(dataClass);
        localDataFrame.setScaledData(scaledData);
        localDataFrame.setScaledColumnsAsRows(scaledColumnsAsData);
        localDataFrame.setColumnsAsRows(columnsAsData);
        return localDataFrame;

    }

    public static LocalDataFrame spectraMapToDataFrame(Map<String, SpectrumModel> spectraMap) {
        double[][] oreginalData = new double[spectraMap.size()][22];
        String[] dataId = new String[spectraMap.size()];
        //1     2                   3                   4       5       6       7       8           9           10      11      12              13                      14                  15                  16          17                  18              19      2           21          22          23              24  
        String[] columnIds = new String[]{"ID", "TheoreticalCharge", "TheoreticalMass", "Mean", "Median", "SD", "Skews", "Kurtosis", "Variance", "Range", "IQR", "PeaksNumber", "AveragePeakIntensity", "TotalIntensity", "PeakIntensityRatios", "AUC", "CoefficientVariations", "PeakSNR90", "PeakSNR70", "SNR90", "SNR70", "DynamicRange", "Tags-E-value", "Class"};
        String[] dataClass = new String[spectraMap.size()];
        int i = 0;
        for (String spectrumTitle : spectraMap.keySet()) {
            dataId[i] = spectrumTitle;
            SpectrumModel spectrum = spectraMap.get(spectrumTitle);
            dataClass[i] = spectrum.getIdClass();
            double[] rowData = new double[]{spectrum.getTheoreticalCharge(), spectrum.getTheoreticalMass(), spectrum.getMean(), spectrum.getMedian(), spectrum.getSd(), spectrum.getSkews(), spectrum.getKurtosis(), spectrum.getVariance(), spectrum.getRange(), spectrum.getIQR(), spectrum.getNumPeaks(), spectrum.getAveragePeakIntensity(), spectrum.getTotalIntensity(), spectrum.getPeakIntensityRatios(), spectrum.getAuc(), spectrum.getCoefficientVariations(), spectrum.getPeakSNR90(), spectrum.getPeakSNR70(), spectrum.getSNR90(), spectrum.getSNR70(), spectrum.getDynamicRange(), spectrum.getTagEvalue()};
            oreginalData[i] = rowData;
            i++;
        }
        //covnert data rows to columns
        Double[][] columnsAsData = new Double[oreginalData[0].length][oreginalData.length];
        for (int ci = 0; ci < oreginalData.length; ci++) {
            for (int j = 0; j < oreginalData[0].length; j++) {
                columnsAsData[j][ci] = oreginalData[ci][j];
            }
        }

        DataFrame dataframe = MachineLearningDataHandler.initDataFrame(oreginalData, dataId, columnIds, dataClass);
        Formula formula = Formula.lhs("Class");
        DataFrame train = dataframe.drop(0).factorize("Class");
        var scaler = WinsorScaler.fit(train, 0.01, 0.99);
        DataFrame scaledDataFrame = scaler.apply(train);
        double[][] scaledData = formula.x(scaledDataFrame).toArray();
        //covnert data rows to columns
        Double[][] scaledColumnsAsData = new Double[scaledData[0].length][scaledData.length];
        for (int ci = 0; ci < scaledData.length; ci++) {
            for (int j = 0; j < scaledData[0].length; j++) {
                scaledColumnsAsData[j][ci] = scaledData[ci][j];
            }
        }

        LocalDataFrame localDataFrame = new LocalDataFrame();
        localDataFrame.setColumnId(columnIds);
        localDataFrame.setRowsId(dataId);
        localDataFrame.setData(oreginalData);
        localDataFrame.setDataFrame(dataframe);
        localDataFrame.setDataClass(dataClass);
        localDataFrame.setScaledData(scaledData);
        localDataFrame.setScaledColumnsAsRows(scaledColumnsAsData);
        localDataFrame.setColumnsAsRows(columnsAsData);
        return localDataFrame;

    }

    public static void performFeatureSelection(Map<String, SpectrumModel> fullSpectra) {
        //prpare the data
        //double[][] data, String[] dataId, String[] columnIds, String[] dataClass
        LocalDataFrame localDataFrame = spectraMapToDataFrame(fullSpectra);
        MachineLearningDataset dataset = new MachineLearningDataset(localDataFrame.getDataFrame(), localDataFrame.getColumnId(), new String[]{});

        String[] toScaleColumns = new String[]{"Mean", "Median", "SD", "Skews", "Kurtosis", "Variance", "Range", "IQR", "PeakIntensityRatios", "AUC", "CoefficientVariations", "PeakSNR90", "PeakSNR70", "SNR90", "SNR70", "DynamicRange"};
        var scaler = WinsorScaler.fit(dataset.train, 0.01, 0.99);
        DataFrame scaledDataFrame = scaler.apply(dataset.train);
        double[][] scaledData = dataset.formula.x(scaledDataFrame).toArray();

        //covnert data rows to columns
        double[][] columnsAsData = new double[scaledData[0].length][scaledData.length];
        for (int ci = 0; ci < scaledData.length; ci++) {
            for (int j = 0; j < scaledData[0].length; j++) {
                columnsAsData[j][ci] = scaledData[ci][j];
            }
        }

        //calculate Nose/sSigna aio
        int i = 0;
        Map<String, Double> columnSNR = new LinkedHashMap<>();
        for (double[] column : columnsAsData) {
            DescriptiveStatistics ds = new DescriptiveStatistics(column);
            double columnSnr = SNRCalculator.calculateSignalNoiseRatioOnLevel(ds, 0.9);
            columnSNR.put(localDataFrame.getColumnId()[i + 1], columnSnr);
            i++;
        }

        TreeMap<Integer, TreeSet<Integer>> correlatedColumns = Util.reduceDatasetMeasurments(columnsAsData);
        Set<Integer> activeColumns = new LinkedHashSet<>();
        for (int columnIndex = 0; columnIndex < scaledData[0].length; columnIndex++) {
            activeColumns.add(columnIndex);
        }
        for (int columnIndex = 0; columnIndex < scaledData[0].length; columnIndex++) {
            for (TreeSet<Integer> cols : correlatedColumns.values()) {
                if (cols.contains(columnIndex)) {
                    activeColumns.removeAll(cols);
                    //select column with hghest imprtancy
                    double snr = -1000.0;
                    int selectedColumn = -1;
                    for (int j : cols) {
                        if (columnSNR.get(localDataFrame.getColumnId()[j + 1]) > snr) {
                            selectedColumn = j;
                        }
                    }
                    activeColumns.add(selectedColumn);
                }
            }

        }
        String[] deactivate = new String[scaledData[0].length - activeColumns.size()];
        int k = 0;
        for (int h = 1; h < localDataFrame.getColumnId().length - 1; h++) {
            if (!activeColumns.contains(h - 1)) {
                deactivate[k] = localDataFrame.getColumnId()[h];
                k++;
            }

        }

//        XMeans clusters = XMeans.fit(dataset.x, 8);
//        var clusters = KMeans.fit(dataset.x, 20);
//        System.out.println("at cluster " + clusters.k + "   " + clusters.centroids.length + "    " );
        System.out.println("start clustering 2");
        KMeansPlusPlusClusterer<DataProcessHandler.DataPoint> clusterer = new KMeansPlusPlusClusterer<>(4);
        DataProcessHandler dph = new DataProcessHandler();
        List<DataProcessHandler.DataPoint> datapointsList = dph.convertToDataPoints(scaledData);
        System.out.println("init clustering");
        List<CentroidCluster<DataProcessHandler.DataPoint>> clusters = clusterer.cluster(datapointsList);
        System.out.println("done clustering");
        Map<String, Integer> clusterData = new HashMap<>();
        for (CentroidCluster<DataProcessHandler.DataPoint> cluster : clusters) {
            System.out.println("at points " + cluster.getPoints().size() + "");
            for (DataProcessHandler.DataPoint dp : cluster.getPoints()) {
                String classIn = localDataFrame.getDataClass()[dp.index];
                if (!clusterData.containsKey(classIn)) {
                    clusterData.put(classIn, 0);
                }
                clusterData.put(classIn, clusterData.get(classIn) + 1);
            }
            for (String cl : clusterData.keySet()) {
                double ratio = (double) clusterData.get(cl) / (double) cluster.getPoints().size();
                if (ratio > 0.5) {
                    for (DataProcessHandler.DataPoint dp : cluster.getPoints()) {
                        localDataFrame.getDataClass()[dp.index] = cl;
                    }
                    System.out.println("at claster " + cluster.getPoints().size() + "  updated to " + cl);
                    break;
                }

            }

            clusterData.clear();
        }

        var x = localDataFrame.getDataFrame().drop(0);
        var y = localDataFrame.getDataFrame().column("Class").toIntArray();
//
//        SumSquaresRatio[] ssrs = SumSquaresRatio.fit(x, "Class");
        TreeMap<Double, String> sortingFeaturesMap = new TreeMap<>(Collections.reverseOrder());
//        for (SumSquaresRatio ssr : ssrs) {
//            if (!Double.isNaN(ssr.ssr)) {
//                sortingFeaturesMap.put(ssr.ssr, ssr.feature);
//            }
//        }
//
//        int index = 1;
//        for (double key : sortingFeaturesMap.keySet()) {
//            System.out.println(index++ + "feature with high SSQ " + sortingFeaturesMap.get(key) + "   v: " + key);
//        }
        var x2 = localDataFrame.getDataFrame().select(22, 14).toArray();
        showScatter(x2, y, "Tags-E-value", "PeakIntensityRatios", "Selecting highest SSR Feature selection");
//

        var model = smile.classification.RandomForest.fit(Formula.lhs("Class"), scaledDataFrame);
//
//        sortingFeaturesMap.clear();
        for (int t = 0; t < dataset.train.names().length - 1; t++) {
            sortingFeaturesMap.put(model.importance()[t], dataset.train.names()[t]);
        }
        int index = 1;
//
        for (double key : sortingFeaturesMap.keySet()) {
            System.out.println(index++ + "  feature with high importace  " + sortingFeaturesMap.get(key) + "   importance: " + key);
        }
        var x3 = localDataFrame.getDataFrame().select(22, 2).toArray();
        showScatter(x3, y, "Tags-E-value", "TheoreticalMass", "Selecting highest RandomForest Feature selection");
    }

    private static void showScatter(double[][] x, int[] y, String xLable, String yLable, String title) {

        var canvas = ScatterPlot.of(x, y, '*');
        canvas.canvas().setTitle(title);
        canvas.canvas().setAxisLabel(0, xLable);
        canvas.canvas().setAxisLabel(1, yLable);

        try {
            canvas.canvas().window();
//SNR calculator
        } catch (InterruptedException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private class DataPoint implements org.apache.commons.math3.ml.clustering.Clusterable, Serializable {

        private final double[] point;
        private int index;

        public DataPoint(double[] point) {
            this.point = point;
        }

        @Override
        public double[] getPoint() {
            return point;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

    }

    private List<DataProcessHandler.DataPoint> convertToDataPoints(double[][] points) {
        List<DataProcessHandler.DataPoint> clusterPoints = new ArrayList<>();
        int dataIndex = 0;
        for (double[] row : points) {
            DataProcessHandler.DataPoint point = new DataProcessHandler.DataPoint(row);
            point.setIndex(dataIndex);
            clusterPoints.add(point);
            dataIndex++;
        }
        return clusterPoints;
    }

    public static Map<String, Integer[]> calculateOptimumEThreshold(Map<String, Double[]> directTagIdentifications, Map<String, double[]> searchEngineidintifications, int totalSpectaNumber, double threshold) {

        //Check agreements between both
        Map<String, Integer[]> agreedDataCount = new LinkedHashMap<>();

        agreedDataCount.put("tag length 1", new Integer[]{0, totalSpectaNumber});
        agreedDataCount.put("tag length 2", new Integer[]{0, totalSpectaNumber});
        agreedDataCount.put("tag length 3", new Integer[]{0, totalSpectaNumber});
        agreedDataCount.put("tag length 4", new Integer[]{0, totalSpectaNumber});
        agreedDataCount.put("full tags", new Integer[]{0, totalSpectaNumber});
        Set<String> fullIdentSpectIds = new LinkedHashSet<>();
        fullIdentSpectIds.addAll(directTagIdentifications.keySet());
        fullIdentSpectIds.addAll(searchEngineidintifications.keySet());

        for (String specTitle : fullIdentSpectIds) {

            if (searchEngineidintifications.containsKey(specTitle) && directTagIdentifications.containsKey(specTitle)) {
//                Double qvalue = searchEngineidintifications.get(specTitle)[0];
                Double pepvalue = searchEngineidintifications.get(specTitle)[1];
                double conf = pepvalue;
                Double[] subData = directTagIdentifications.get(specTitle);
                int i = 0;
                for (String key : agreedDataCount.keySet()) {
                    //green color
                    if (subData[i] != null && subData[i] <= threshold && (conf <= Parameters.searchEngine_threshold)) {
                        agreedDataCount.get(key)[0] = agreedDataCount.get(key)[0] + 1;
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] - 1;

                    } //pink
                    else if (subData[i] != null && subData[i] <= threshold && (conf > Parameters.searchEngine_threshold)) {
//                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] + 1;
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] - 1;
                    }//orange
                    else if ((conf <= threshold)) {
//                        agreedDataCount.get(key)[0] = agreedDataCount.get(key)[0] + 1;
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] - 1;
                    }
                    i++;

                }

            }//pink 
            else if (directTagIdentifications.containsKey(specTitle) && !searchEngineidintifications.containsKey(specTitle)) {
                Double[] subData = directTagIdentifications.get(specTitle);
                int i = 0;
                for (String key : agreedDataCount.keySet()) {
                    if (subData[i] != null && subData[i] <= threshold) {
//                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] + 1;
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] - 1;
                    }
                    i++;

                }
            }//orange 
            else if (!directTagIdentifications.containsKey(specTitle) && searchEngineidintifications.containsKey(specTitle)) {
//                Double qvalue = searchEngineidintifications.get(specTitle)[0];
                Double pepvalue = searchEngineidintifications.get(specTitle)[1];
                double conf = pepvalue;
                if ((conf <= threshold)) {
                    for (String key : agreedDataCount.keySet()) {
//                        agreedDataCount.get(key)[0] = agreedDataCount.get(key)[0] + 1;
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] - 1;

                    }

                }

            }

        }

        return agreedDataCount;
    }

}

package no.uib.probe;

import java.util.LinkedHashMap;
import no.uib.probe.handlers.DataLoadHandler;
import no.uib.probe.utils.Util;
import no.uib.probe.utils.Parameters;
import java.util.Map;
import java.util.Set;
import no.uib.probe.handlers.DataProcessHandler;
import no.uib.probe.models.LocalDataFrame;
import no.uib.probe.models.SpectrumModel;
import no.uib.probe.view.VisualizationHandler;

/**
 *
 * @author yfa041
 */
public class SpectralEvaluator {

    public SpectralEvaluator() {
        /**
         * load directTag from direct tag files*
         */
        Map<String, Double[]> directTagIdentifications = DataLoadHandler.getTagData(Parameters.data_folder_url);
        /**
         * load identifed data fro SearchEngine - ionbot in this case*
         */
        Map<String, double[]> searchEngineidintifications = DataLoadHandler.getIdSet(Parameters.data_folder_url);
        System.out.println("at SE results ----------->> " + searchEngineidintifications.size());
        /**
         * load and calculate spectrum features*
         */
        Map<String, SpectrumModel> fullSpectra = DataLoadHandler.processMGFFile(Parameters.data_folder_url);
        System.out.println("very bad spectra size " + DataLoadHandler.badSpectrumMap.size() + " / " + fullSpectra.size());

        for (String title : DataLoadHandler.badSpectrumMap.keySet()) {
            if (searchEngineidintifications.containsKey(title) || directTagIdentifications.containsKey(title)) {
                System.out.println("---------------------->>>>  error super bAD spectra identified " + title);
            }
        }
        
        /**done loading data**/
        /**create data set ***/         
       LocalDataFrame dataset =  DataProcessHandler.spectraMapToDataFrame(fullSpectra,directTagIdentifications);
        
        

        VisualizationHandler.viewDirectTagData(directTagIdentifications, searchEngineidintifications, fullSpectra.size(),dataset);
//        Map<Double, Map<String, Integer[]>> optimisingMap = new LinkedHashMap<>();
//        int currUnid = -1;
//        int currIon = -1;
//
//        String selectedCat = "nothingselected";
//        double bestT = 0.01;
//        double initalRed = -1;
//        double initialGreen = -1;
//        double effect = 0;
//        double bestEffect = 0;
//
//        for (double t = 0.01; t <= 1.0;) {
////    double t=0.01;
//            Map<String, Integer[]> cd = DataProcessHandler.calculateOptimumEThreshold(directTagIdentifications, searchEngineidintifications, fullSpectra.size(), t);
//            if (initialGreen == -1) {
//                selectedCat = cd.keySet().toArray()[0] + "";
//                initalRed = cd.get(selectedCat)[1];
//                initialGreen = cd.get(selectedCat)[0];
//            }
//
//            for (String cat : cd.keySet()) {
//                double changeInRed = Math.abs(cd.get(cat)[1] - initalRed) / initalRed;
//                double changeInGreen = (cd.get(cat)[0] - initialGreen) / fullSpectra.size();
//                boolean posEffect = (changeInRed <= 10.0) && (changeInGreen > bestEffect);
//                if (posEffect) {
//                    System.out.println("at postive " + posEffect + "  " + changeInGreen + "  " + changeInRed);
//                }
//
//                if (posEffect) {
//                    selectedCat = cat;
//                    bestEffect = changeInGreen;
//                    bestT = t;
//                    currIon = cd.get(cat)[0];
//                    currUnid = cd.get(cat)[1];
//                    System.out.println("---- Thr--- " + t + "   cat " + selectedCat + "   " + currIon + "  " + currUnid);
//                }
//
//            }
//
//            t = t + 0.01;
//        }
//        System.out.println("Finally ---->>> Thr: " + bestT + "  with cat " + selectedCat + "   " + currIon + "    " + currUnid);
//        Parameters.e_value_threshold = 0.05;//bestT;
//        //visualse the tag diistribution
//        Set<String>[][] classfiedDataMatrix = VisualizationHandler.viewDirectTagData(directTagIdentifications, searchEngineidintifications, fullSpectra.size());
//        //analys data catigories      
//        DataProcessHandler.analyseDataCategory(classfiedDataMatrix, fullSpectra);
        // add rank to each spectra
//        Util.classifyDataOnQuality(directTagIdentifications, searchEngineidintifications, fullSpectra);
        //view all features distribution based on rank
        //feature selection process
//        DataProcessHandler.performFeatureSelection(fullSpectra);
        //now data is ready to cluster
        //update data classfiers based on the outcome results
//        perform classfication traning algorithms 
//        Set<String> fullIdSpectrum = new LinkedHashSet<>();
//        fullIdSpectrum.addAll(searchEngineidintifications.keySet());
//        fullIdSpectrum.addAll(directTagIdentifications.keySet());
//        Object[][] dataTable = new Object[fullIdSpectrum.size()][];
//        Set<String> onlyOnIonbot = new HashSet<>();
//        Set<String> onlyOnTag = new HashSet<>();
//        Set<String> onBoth = new HashSet<>();
//        int rowCount = 0;
//        for (String spec : fullIdSpectrum) {
//            Object[] row = new Object[2 + directTagIdentifications.values().size()];
//            row[0] = spec;
//            row[row.length - 1] = searchEngineidintifications.containsKey(spec);
//            if (directTagIdentifications.containsKey(spec)) {
//                int cellCount = 1;
//                for (Double d : directTagIdentifications.get(spec)) {
//                    row[cellCount++] = d;
//                }
//                if (!searchEngineidintifications.containsKey(spec)) {
//                    onlyOnTag.add(spec);
//                } else {
//                    onBoth.add(spec);
//                }
//            } else {
//                onlyOnIonbot.add(spec);
//            }
//            
//            dataTable[rowCount++] = row;
//        }
//        System.out.println("at only on ionbot " + onlyOnIonbot.size() + "  out of " + fullIdSpectrum.size());
//        System.out.println("at only on tag    " + onlyOnTag.size() + "  out of " + fullIdSpectrum.size());
//        System.out.println("at on both        " + onBoth.size() + "  out of " + fullIdSpectrum.size());
//      
//        Map<String, double[]> tag3_ionbot_qvalue = new LinkedHashMap<>();
//        Map<String, double[]> tag3_ionbot_pep = new LinkedHashMap<>();
//        for (String spec : onBoth) {
//            Double[] row = directTagIdentifications.get(spec);
//            if (row[0] != null) {
//                tage2.add(row[0]);
//            }
//            if (row[1] != null) {
//                tage3.add(row[1]);
//                tag3_ionbot_qvalue.put(spec, new double[]{row[1], searchEngineidintifications.get(spec)[0]});
//                tag3_ionbot_pep.put(spec, new double[]{row[1], searchEngineidintifications.get(spec)[1]});
//            }
//            if (row[2] != null) {
//                tage4.add(row[2]);
//            }
//        }
//        System.out.println("at only on tag    " + onlyOnTag.size() + "  out of " + fullIdSpectrum.size() + "   tag2 " + tage2.size() + "   tag3  " + tage3.size() + "   tag4   " + tage4.size());
//        double[] tag2Arr = new double[tage2.size()];
//        for (int i = 0; i < tag2Arr.length; i++) {
//            tag2Arr[i] = tage2.get(i);
//        }
////        new DensityPlot("2 Tags", "evalue", "", tag2Arr);
//
//        double[] tag3Arr = new double[tage3.size()];
//        for (int i = 0; i < tag3Arr.length; i++) {
//            tag3Arr[i] = tage3.get(i);
//        }
////        new DensityPlot("3 Tags", "evalue", "", tag3Arr);
//
//        double[] tag4Arr = new double[tage4.size()];
//        for (int i = 0; i < tag4Arr.length; i++) {
//            tag4Arr[i] = tage4.get(i);
//        }
////        new DensityPlot("4 Tags", "evalue", "", tag4Arr);
//
//        double[][] data = new double[3][];
//        data[0] = tag2Arr;
//        data[1] = tag3Arr;
//        data[2] = tag4Arr;
////        new DensityPlot("compare tags", "evalue", "", data);
//      
//
//        ///plot the e value vs the ionbot pep and score values
//        double[] xValues = new double[tag3_ionbot_qvalue.size()];
//        double[] yValues_qvalue = new double[tag3_ionbot_qvalue.size()];
//        double[] yValues_pep = new double[tag3_ionbot_qvalue.size()];
//        int i = 0;
//        for (String spec : tag3_ionbot_qvalue.keySet()) {
//            xValues[i] = tag3_ionbot_qvalue.get(spec)[0];
//            yValues_qvalue[i] = tag3_ionbot_qvalue.get(spec)[1];
//            yValues_pep[i] = tag3_ionbot_pep.get(spec)[1];
//            i++;
//        }
//        double[][] ds1 = new double[2][];
//        ds1[0] = xValues;
//        ds1[1] = yValues_qvalue;
//        
//        ScatterPlot plot = new ScatterPlot("evalue/ionbot pep", "evalue", "qvalue", ds1);
////        
//        double[][] ds2 = new double[2][];
//        ds2[0] = xValues;
//        ds2[1] = yValues_pep;
//        ScatterPlot plot2 = new ScatterPlot("evalue/ionbot score", "evalue", "pep", ds2);
//        
        //now we can  cluster to update classes
    }

    public static void main(String[] args) {
        new SpectralEvaluator();

        /**
         * calculate spectral statistical features
         */
        /**
         * calculate spectral peak features
         */
        /**
         * calculate SNR features
         */
        /**
         * calculate intensity relation
         */
        /**
         * calculate peak width information
         */
        /**
         * calculate peak intensity distribution
         */
        /**
         * write results out int file (create data-set) ds_training for training
         * data 60% ds_validation 20% ds_testing 20% of data ds_training_sample
         * 20% of ds_training
         */
        /**
         * select features (based on important score from RandomForest, Genetic
         * Algorithm Based Feature Selection, and/or TreeSHAP)
         */
        /**
         * reduce feature with high correlation feature
         */
        /**
         * cluster to get categories or run into tree if cluster get 10 samples
         * from each cluster to visualize and prove they are related re label
         * the training data class Train tree for data classification using tree
         * (regression or classification tree)
         */
        /**
         * load testing data test and measure accuracy
         *
         */
    }
}

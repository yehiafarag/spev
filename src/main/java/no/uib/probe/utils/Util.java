/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils;

import com.compomics.util.experiment.io.mass_spectrometry.MsFileIterator;
import com.compomics.util.experiment.mass_spectrometry.spectra.Spectrum;
import com.compomics.util.waiting.WaitingHandler;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import no.uib.probe.models.SpectrumModel;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author yfa041
 */
public class Util {

    public static double calculateAverageRatios(double[] data) {
        double sumRatio = 0;
        double countRatios = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (j <= i) {
                    continue;
                }

                sumRatio += data[i] / data[j];
                countRatios++;

            }
        }
        sumRatio = sumRatio / countRatios;
        return sumRatio;
    }

    public static double log2Scale(double value) {

        double a = 2.0 / Math.exp(value * 2.0);
        return a;
    }
    

//
//    public static void classifyDataOnQuality(Map<String, Double[]> tagsValues, Map<String, double[]> ionbot_idSpect, Map<String, SpectrumModel> fullIdSpectrum) {
//        //both agree with very high confedent
//        Map<Integer, Set<String>> specClassifires = new HashMap<>();
//        int rank_1_Count = 0;
//        int rank_2_count = 0;
//        int rank_3_count = 0;
//        int rank_5_count = 0;
//        int rank_6_count = 0;
//
//        specClassifires.put(1, new LinkedHashSet<>());
//        specClassifires.put(2, new LinkedHashSet<>());
//        specClassifires.put(3, new LinkedHashSet<>());
//        specClassifires.put(4, new LinkedHashSet<>());
//        specClassifires.put(5, new LinkedHashSet<>());
//        specClassifires.put(6, new LinkedHashSet<>());
//
//        int rank_4_count = 0;
//        int confid = 0;
//        int nConfid = 0;
//        for (String spec : fullIdSpectrum.keySet()) {
//            double lowE = Double.MAX_VALUE;
//            boolean tagExist = false;
//            if (tagsValues.containsKey(spec)) {
//
//                if (tagsValues.get(spec)[0] != null) {
//                    lowE = Math.min(tagsValues.get(spec)[0], lowE);
//                    tagExist = true;
//
//                }
//                if (tagsValues.get(spec)[1] != null) {
//
//                    lowE = Math.min(tagsValues.get(spec)[1], lowE);
//                    tagExist = true;
//                }
//                if (tagsValues.get(spec)[2] != null) {
////                    if(tagsValues.get(spec)[2]<=0.05)
////                        confid++;
////                    else
////                        nConfid++;
//                    lowE = Math.min(tagsValues.get(spec)[2], lowE);
//                    tagExist = true;
//                }
//            }
//            if (tagExist) {
//                fullIdSpectrum.get(spec).setTagEvalue(lowE);
//            }
//            if (tagExist && lowE < 0.05 && ((ionbot_idSpect.containsKey(spec) && ionbot_idSpect.get(spec)[0] < 0.05))) { // tagsValues.get(spec)[1] != null && tagsValues.get(spec)[1] < 0.05) {             
//                /**
//                 * here is a very confident spectra*
//                 */
////                 System.out.println("at found h q spec "+"  "+ionbot_idSpect.get(spec)[1]);
//                rank_1_Count++;
//                specClassifires.get(1).add(spec);
//
//            } else if (tagExist && lowE < 0.05 && (ionbot_idSpect.containsKey(spec))) {
//                rank_2_count++;
//                specClassifires.get(2).add(spec);
//            } else if (ionbot_idSpect.containsKey(spec) && ionbot_idSpect.get(spec)[0] < 0.05 && tagExist) {
//                rank_2_count++;
//                specClassifires.get(2).add(spec);
//            } else if (tagExist && lowE < 0.05) {
//                rank_3_count++;
//                specClassifires.get(3).add(spec);
//            } else if (ionbot_idSpect.containsKey(spec) && ionbot_idSpect.get(spec)[0] < 0.05) {
//                rank_3_count++;
//                specClassifires.get(3).add(spec);
//            } else if (ionbot_idSpect.containsKey(spec) && tagExist) {
//                rank_4_count++;
//                specClassifires.get(4).add(spec);
//            } else if (ionbot_idSpect.containsKey(spec) || tagExist) {
//                rank_5_count++;
//                specClassifires.get(5).add(spec);
//            } else {
//                specClassifires.get(6).add(spec);
//                rank_6_count++;
//            }
//
//        }
//        System.out.println("total Rank 1 spec " + rank_1_Count + "  total spec " + fullIdSpectrum.size() + "   ratio is  " + (rank_1_Count * 100.0 / fullIdSpectrum.size()));
//        System.out.println("total Rank 2 spec " + rank_2_count + "  total spec " + fullIdSpectrum.size() + "   ratio is  " + (rank_2_count * 100.0 / fullIdSpectrum.size()));
//        System.out.println("total Rank 3 spec " + rank_3_count + "  total spec " + fullIdSpectrum.size() + "   ratio is  " + (rank_3_count * 100.0 / fullIdSpectrum.size()));
//
//        System.out.println("total Rank 4 spec " + rank_4_count + "  total spec " + fullIdSpectrum.size() + "   ratio is  " + (rank_4_count * 100.0 / fullIdSpectrum.size()));
//        System.out.println("total Rank 5 spec " + rank_5_count + "  total spec " + fullIdSpectrum.size() + "   ratio is  " + (rank_5_count * 100.0 / fullIdSpectrum.size()));
//        System.out.println("total Rank 6 spec " + rank_6_count + "  total spec " + fullIdSpectrum.size() + "   ratio is  " + (rank_6_count * 100.0 / fullIdSpectrum.size()));
//        System.out.println("at confi " + confid + "   nconfid " + nConfid + "   total: " + (confid + nConfid));
//
//        /**
//         * add class for each spectra*
//         */
////        for (String title : fullIdSpectrum.keySet()) {
////
////            for (int rank : specClassifires.keySet()) {
////
////                if (specClassifires.get(rank).contains(title)) {
////                    fullIdSpectrum.get(title).setIdClass(rank);
////                    break;
////                }
////            }
////
////        }
//
////        return specClassifires;
//    }

    /**
     * Writes a cms file for the given mass spectrometry file.
     *
     * @param msFile The mass spectrometry file.
     * @param cmsFile The cms file.
     * @param waitingHandler The waiting handler.
     *
     * @throws IOException Exception thrown if an error occurred while reading
     * or writing a file.
     */
    public static void writeCmsFile(
            File msFile,
            File cmsFile,
            WaitingHandler waitingHandler
    ) throws IOException {

        try (MsFileIterator iterator = MsFileIterator.getMsFileIterator(msFile, waitingHandler)) {
            try (UpdatedCmsFileWriter writer = new UpdatedCmsFileWriter(cmsFile)) {

                String spectrumTitle;
                while ((spectrumTitle = iterator.next()) != null) {

                    Spectrum spectrum = iterator.getSpectrum();
                    writer.addSpectrum(spectrumTitle, spectrum);

                }
            }
        }
    }

    public static TreeMap<Integer, TreeSet<Integer>> reduceDatasetMeasurments(double[][] columnsAsData) {

        //remove outliers of the data       
        PearsonsCorrelation corr = new PearsonsCorrelation();

        TreeMap<Integer, TreeSet<Integer>> treemap = new TreeMap<>();
        int counter = 0;
        for (double[] col : columnsAsData) {
            int counter2 = 0;
            for (double[] col2 : columnsAsData) {
                if (counter2 <= counter) {
                    counter2++;
                    continue;
                }
                double correlatio = corr.correlation(col, col2);
                if (correlatio >= 0.8) {
                    if (!treemap.containsKey(counter)) {
                        treemap.put(counter, new TreeSet<>());
                    }
                    treemap.get(counter).add(counter2);
                }
                counter2++;

            }
            counter++;
        }
        TreeMap<Integer, TreeSet<Integer>> mergedMap = new TreeMap<>();
        for (int i : treemap.keySet()) {
            mergedMap.put(i, new TreeSet<>(treemap.get(i)));
        }
        for (int i : treemap.keySet()) {
            if (!mergedMap.containsKey(i)) {
                continue;
            }
            for (int j : treemap.get(i)) {
                if (treemap.containsKey(j)) {
                    mergedMap.get(i).add(i);
                    mergedMap.get(i).addAll(treemap.get(j));
                    mergedMap.remove(j);

                }
            }

        }
        return mergedMap;

    }

    public static void exportToImg(JFreeChart jfreeChart, String fileName) {
        BufferedImage objBufferedImage = jfreeChart.createBufferedImage(600, 800);
        File imgFile = new File(Parameters.data_folder_url, fileName);

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            imgFile.createNewFile();
            ImageIO.write(objBufferedImage, "png", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getCategoryIndex(double e) {
        if (e <= 0.01) {
            return 0;
        } 
        else// if (e < 0.1)
        {
            return 1;
        } 
//        else {
//            return 2;
//        }

    }

}

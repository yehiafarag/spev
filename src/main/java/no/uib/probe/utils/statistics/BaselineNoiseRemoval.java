package no.uib.probe.utils.statistics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yfa041
 */
public class BaselineNoiseRemoval {

    /*
 * use a technique called the Top Hat filter. The Top Hat filter is commonly used for baseline correction in mass spectrometry data analysis. Here's an example of how you can implement it:
     */
    public static double[][] removeBaselineNoise(double[] data, int windowSize, double[] mzData) {
        int n = data.length;

        // Apply the Top Hat filter
        double[] correctedData = new double[n];

        for (int i = 0; i < n; i++) {
            int start = Math.max(0, i - windowSize);
            int end = Math.min(n - 1, i + windowSize);

            double min = Double.MAX_VALUE;

            for (int j = start; j <= end; j++) {
                if (data[j] < min) {
                    min = data[j];
                }
            }

            correctedData[i] = data[i] - min;
        }
        List<Double> removeZerosList = new ArrayList<>();
        List<Double> removeMzZerosList = new ArrayList<>();
        int index = 0;
        for (double d : correctedData) {
            if (d > 0) {
                removeZerosList.add(d);
                removeMzZerosList.add(mzData[index]);
            }
            index++;
        }
        double[] correctedNoZer = new double[removeZerosList.size()];
        double[] correctedMzNoZer = new double[removeZerosList.size()];
        for (int i = 0; i < correctedNoZer.length; i++) {
            correctedNoZer[i] = removeZerosList.get(i);
            correctedMzNoZer[i] = removeMzZerosList.get(i);
        }
        double[][] fullData = new double[2][correctedMzNoZer.length];
        fullData[0] = correctedNoZer;
        fullData[1] = correctedMzNoZer;
        return fullData;
    }

    public static void main(String[] args) {
        double[] data = {1.2, 6, 1.5, 1.4, 1.6, 8, 1.7, 1.9, 1.8, 1.7};

        int windowSize = 10;

        double[][] correctedData = removeBaselineNoise(data, windowSize, data);
        // Print the corrected data
//        for (double value : correctedData) {
//            System.out.println(value);
//        }
//
//        System.out.println("SNR: " + SNRCalculator.calculateSNR(new double[]{6.0, 8.0}, new double[]{1.5, 1.4, 1.6, 8, 1.7, 1.9, 1.8, 1.7}));
//
//        System.out.println("SNR: " + SNRCalculator.calculateSNR(new double[]{4.8, 6.8}, new double[]{0.0, 0.3, 0.2, 0.4, 0.5, 0.7, 0.6, 0.5}));

    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils.statistics;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class SNRCalculator {

    public static double calculatePSNR(double peakIntensity, double backgroundNoiseStdDev) {
        return 20 * Math.log10(peakIntensity / backgroundNoiseStdDev);
    }

    public static double calculateSNR(double[] signal, double[] noise) {
        double signalAmplitude = calculateAmplitude(signal);
        double noiseStdDev = calculateStandardDeviation(noise);

        return signalAmplitude / noiseStdDev;
    }

    public static double calculateAmplitude(double[] data) {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (double value : data) {
            if (value > max) {
                max = value;
            }

            if (value < min) {
                min = value;
            }
        }

        return max - min;
    }

    public static double calculateStandardDeviation(double[] data) {
        double sum = 0.0;
        double mean = calculateMean(data);
        int n = data.length;

        for (double value : data) {
            sum += Math.pow(value - mean, 2);
        }

        return Math.sqrt(sum / n);
    }

    public static double calculateMean(double[] data) {
        double sum = 0.0;
        int n = data.length;

        for (double value : data) {
            sum += value;
        }

        return sum / n;
    }

    public static void main(String[] args) {
        double[] signal = {1.2, 1.3, 1.5, 1.4, 1.6};
        double[] noise = {0.1, 0.3, 0.2, 0.4, 0.5};

        double snr = calculateSNR(signal, noise);

        System.out.println("SNR: " + snr);
    }

    public static double[] calculateSignalNoiseRatioOnLevel(double[] sortedValues, double level) {
        int cutoff = (int) (sortedValues.length * level);
        double[] noise = new double[cutoff];
        double[] signal = new double[sortedValues.length - cutoff];
        System.arraycopy(sortedValues, 0, noise, 0, noise.length);
        for (int i = 0; i < signal.length; i++) {
            signal[i] = sortedValues[noise.length + i];
        }

        double signalToNoiseRatio = calculateSNR(signal, noise);
        double noiseSD = calculateStandardDeviation(noise);
        double pSNR = calculatePSNR(sortedValues[sortedValues.length - 1], noiseSD);
        return new double[]{signalToNoiseRatio, pSNR};

    }

    public static double calculateSignalNoiseRatioOnLevel(DescriptiveStatistics ds, double level) {
        int cutoff = (int) (ds.getSortedValues().length * level);
        double[] noise = new double[cutoff];
        double[] signal = new double[ds.getSortedValues().length - cutoff];
        System.arraycopy(ds.getSortedValues(), 0, noise, 0, noise.length);
        for (int i = 0; i < signal.length; i++) {
            signal[i] = ds.getSortedValues()[noise.length + i];
        }
        DescriptiveStatistics dsSignal = new DescriptiveStatistics(signal);
        DescriptiveStatistics dsNoise = new DescriptiveStatistics(noise);

        double signalToNoiseRatio = Math.abs(dsSignal.getMean() - dsNoise.getMean()) / (dsSignal.getStandardDeviation() + dsNoise.getStandardDeviation());

        return signalToNoiseRatio;

    }

    public static double calculateCoefficientVariations(DescriptiveStatistics dstatic) {
        return dstatic.getMean() / dstatic.getStandardDeviation();
    }

    public static double calculateSignalNoiseRatioOnBaseLinel(double[] sortedValues, double baseline) {

        List<Double> noiseList = new ArrayList();
        List<Double> signalList = new ArrayList();
        for (double inte : sortedValues) {
            if (inte > baseline) {
                signalList.add(inte);
            } else {
                noiseList.add(inte);
            }

        }
        double[] noise = new double[noiseList.size()];
        double[] signal = new double[signalList.size()];
        for (int i = 0; i < signal.length; i++) {
            signal[i] = signalList.get(i);
        }

        for (int i = 0; i < noise.length; i++) {
            noise[i] = noiseList.get(i);
        }

        double signalToNoiseRatio = SNRCalculator.calculateSNR(signal, noise);
        return signalToNoiseRatio;

    }
}

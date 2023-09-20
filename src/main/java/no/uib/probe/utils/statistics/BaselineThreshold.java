/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils.statistics;

public class BaselineThreshold {

    public static double calculateBaselineThreshold(double[] data, double factor) {
        double baseline = calculateBaseline(data);
        double noiseLevel = calculateNoiseLevel(data, baseline);

        return baseline + factor * noiseLevel;
    }

    public static double calculateBaseline(double[] data) {
        // Choose a baseline estimation method (e.g., moving average, polynomial fitting, etc.)
        // Implement the method to calculate the baseline value

        // For this example, we will use a simple moving average
        double sum = 0.0;
        int n = data.length;

        for (double value : data) {
            sum += value;
        }

        return sum / n;
    }

    public static double calculateNoiseLevel(double[] data, double baseline) {
        double sum = 0.0;
        int n = data.length;

        for (double value : data) {
            double diff = value - baseline;
            sum += diff * diff;
        }

        double meanSquareDiff = sum / n;

        return Math.sqrt(meanSquareDiff);
    }

    public static void main(String[] args) {
        double[] data = {11,18,50, 1.3, 1.5, 1.4, 1.6, 1.8, 1.7, 1.9, 1.8, 1.7};
        
        
        System.out.println("baseline : "+calculateBaseline(data));
        
        double factor = 1.0; // Adjust this factor to control the threshold level

        double baselineThreshold = calculateBaselineThreshold(data, factor);

        System.out.println("Baseline Threshold: " + baselineThreshold);
    }
}

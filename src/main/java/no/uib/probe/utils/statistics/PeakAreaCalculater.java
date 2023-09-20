/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils.statistics;

/**
 *
 * @author yfa041
 */
public class PeakAreaCalculater {
    
     
    public static void main(String[] args) {
        // Example intensity values for a peak
        double[] intensityValues = { 0.5, 1.2, 2.3, 3.7, 2.9, 1.8, 0.9, 0.3 };

        // Baseline value
        double baseline = BaselineThreshold.calculateBaseline(intensityValues);

        // Calculate peak height
        double peakHeight = calculatePeakHeight(intensityValues, baseline);
        System.out.println("Peak Height: " + peakHeight);

        // Calculate peak area
        double peakArea = calculatePeakArea(intensityValues, baseline);
        System.out.println("Peak Area: " + peakArea);
    }
    
    
     public static double calculatePeakHeight(double[] intensityValues, double baseline) {
        double maxIntensity = Double.MIN_VALUE;

        // Find the maximum intensity value
        for (double intensity : intensityValues) {
            if (intensity > maxIntensity) {
                maxIntensity = intensity;
            }
        }

        // Calculate the peak height
        double peakHeight = maxIntensity - baseline;
        return peakHeight;
    }

    public static double calculatePeakArea(double[] intensityValues, double baseline) {
        double peakArea = 0.0;

        // Calculate the area under the peak curve using the trapezoidal rule
        for (int i = 1; i < intensityValues.length; i++) {
            double segmentWidth = 1.0; // Assuming equal segment width of 1
            double segmentHeight1 = intensityValues[i - 1] - baseline;
            double segmentHeight2 = intensityValues[i] - baseline;

            double segmentArea = segmentWidth * (segmentHeight1 + segmentHeight2) / 2.0;
            peakArea += segmentArea; 
        }

        return peakArea;
    }
}

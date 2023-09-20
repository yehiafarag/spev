/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils.statistics;

/**
 *
 * @author yfa041
 */
public class AUCCalculator {

    public static double calculateAUC(double[] mzValues, double[] intensities) {
        if (mzValues.length != intensities.length || mzValues.length < 2) {
            throw new IllegalArgumentException("Invalid input data");
        }

        double auc = 0.0;

        for (int i = 0; i < mzValues.length - 1; i++) {
            double deltaX = mzValues[i + 1] - mzValues[i];
            double avgIntensity = (intensities[i] + intensities[i + 1]) / 2.0;
            auc += deltaX * avgIntensity;
        }

        return auc;
    }

    public static void main(String[] args) {
        // Example data: m/z values and corresponding intensities
        double[] mzValues = {100.0, 101.0, 102.0, 103.0, 104.0};
        double[] intensities = {500.0, 750.0, 1000.0, 800.0, 600.0};

        double auc = calculateAUC(mzValues, intensities);
        System.out.println("Area Under the Curve (AUC): " + auc);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils.statistics;

public class BaselineEstimation {
    
    public static double[] calculateBaseline(double[] data, int windowSize) {
        int n = data.length;
        double[] baseline = new double[n];
        
        for (int i = 0; i < n; i++) {
            int start = Math.max(0, i - windowSize);
            int end = Math.min(n - 1, i + windowSize);
            
            double sum = 0.0;
            int count = 0;
            
            for (int j = start; j <= end; j++) {
                sum += data[j];
                count++;
            }
            
            baseline[i] = sum / count;
        }
        
        return baseline;
    }
    
    public static void main(String[] args) {
        double[] data = {1.2, 1.3, 1.5, 1.4, 1.6, 1.8, 1.7, 1.9, 1.8, 1.7};
        int windowSize = 3;
        
        double[] baseline = calculateBaseline(data, windowSize);
        
        // Print the calculated baseline values
        for (double value : baseline) {
            System.out.println(value);
        }
    }
}

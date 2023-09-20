package no.uib.probe.models;

import java.util.Set;

/**
 * This class represent spectrum object where the spectrum data is stored
 *
 * @author Yehia Mokhtar Farag
 */
public class SpectrumModel {

    private String spectrumTitle;
    private String idClass = "Cat " + 1 + "-" + 1;
    private double theoreticalCharge;
    private double theoreticalMass;
    private double median;
    private double mean;
     private double sd;
    private double skews;
    private double kurtosis;
     private double variance;
    private double range;
     private double IQR;
     private int numPeaks;
private double averagePeakIntensity;
private double totalIntensity;
 private double peakIntensityRatios;
private double auc;
    private double coefficientVariations;
    private double PeakSNR90;
    private double PeakSNR70;
    private double SNR90;
    private double SNR70;
private double dynamicRange;
private double tagEvalue=100.0;
private Double[] tagsEValues=new Double[]{100.0,100.0,100.0,100.0,100.0};

    public double getTagEvalue() {
        return tagEvalue;
    }

    public void setTagEvalue(double tagEvalue) {
        this.tagEvalue = tagEvalue;
    }

    public Double[] getTagsEValues() {
        return tagsEValues;
    }

    public void setTagsEValues(Double[] tagsEValues) {
        this.tagsEValues = tagsEValues;
    }




//    private int index;
//    private double m_z;
//    
//    
    private String file;
    private String spectrumKey;
//    
//    private double maxIntensity;
//    private boolean identified = false;
      
//   
//    
//    
    public double getAveragePeakIntensity() {
        return averagePeakIntensity;
    }

    public void setAveragePeakIntensity(double averagePeakIntensity) {
        this.averagePeakIntensity = averagePeakIntensity;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }
//    
//   
//    private double sumsq;
//
//    
//
//   
//    private double peakHeigh;
//    private double peakArea;
//  
//    
//   ;

    public double getPeakIntensityRatios() {
        return peakIntensityRatios;
    }

    public void setPeakIntensityRatios(double peakIntensityRatios) {
        this.peakIntensityRatios = peakIntensityRatios;
    }

    public double getSNR90() {
        return SNR90;
    }

    public void setSNR90(double SNR90) {
        this.SNR90 = SNR90;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getSd() {
        return sd;
    }

    public void setSd(double sd) {
        this.sd = sd;
    }

    public double getSkews() {
        return skews;
    }

    public void setSkews(double skews) {
        this.skews = skews;
    }

    public double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

//    public double getSumsq() {
//        return sumsq;
//    }
//
//    public void setSumsq(double sumsq) {
//        this.sumsq = sumsq;
//    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

//    public boolean isIdentified() {
//        return identified;
//    }
//
//    public void setIdentified(boolean identified) {
//        this.identified = identified;
//    }
//    private double sequenceRank;

//    public double getSequenceRank() {
//        return sequenceRank;
//    }
//
//    public void setSequenceRank(double sequenceRank) {
//        this.sequenceRank = sequenceRank;
//    }
//
//    public double getMaxIntensity() {
//        return maxIntensity;
//    }
//
//    public void setMaxIntensity(double maxIntensity) {
//        this.maxIntensity = maxIntensity;
//    }

    public double getSignalToNoiseEstimation_avg() {
        return signalToNoiseEstimation_avg;
    }

    public void setSignalToNoiseEstimation_avg(double signalToNoiseEstimation_avg) {
        this.signalToNoiseEstimation_avg = signalToNoiseEstimation_avg;
    }
    private double signalToNoiseEstimation_avg;

    public double getTotalIntensity() {
        return totalIntensity;
    }

    public void setTotalIntensity(double totalIntensity) {
        this.totalIntensity = totalIntensity;
    }

    public int getNumPeaks() {
        return numPeaks;
    }

    public void setNumPeaks(int numPeaks) {
        this.numPeaks = numPeaks;
    }

//    public double getM_z() {
//        return m_z;
//    }
//
//    public void setM_z(double m_z) {
//        this.m_z = m_z;
//    }

    public double getTheoreticalCharge() {
        return theoreticalCharge;
    }

    public void setTheoreticalCharge(double theoreticalCharge) {
        this.theoreticalCharge = theoreticalCharge;
    }

    public double getTheoreticalMass() {
        return theoreticalMass;
    }

    public void setTheoreticalMass(double theoreticalMass) {
        this.theoreticalMass = theoreticalMass;
    }

//    public int getIndex() {
//        return index;
//    }
//
//    public void setIndex(int index) {
//        this.index = index;
//    }

    public String getSpectrumTitle() {
        return spectrumTitle;
    }

    public void setSpectrumTitle(String spectrumTitle) {
        this.spectrumTitle = spectrumTitle;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSpectrumKey() {
        return spectrumKey.toLowerCase();
    }

    public void setSpectrumKey(String spectrumKey) {
        this.spectrumKey = spectrumKey;
    }

    public double getDynamicRange() {
        return dynamicRange;
    }

    public void setDynamicRange(double dynamicRange) {
        this.dynamicRange = dynamicRange;
    }

    public double getCoefficientVariations() {
        return coefficientVariations;
    }

    public void setCoefficientVariations(double coefficientVariations) {
        this.coefficientVariations = coefficientVariations;
    }

    public double getPeakSNR90() {
        return PeakSNR90;
    }

    public void setPeakSNR90(double PeakSNR90) {
        this.PeakSNR90 = PeakSNR90;
    }

    public double getPeakSNR70() {
        return PeakSNR70;
    }

    public void setPeakSNR70(double PeakSNR70) {
        this.PeakSNR70 = PeakSNR70;
    }

//    public double getPeakHeigh() {
//        return peakHeigh;
//    }
//
//    public void setPeakHeigh(double peakHeigh) {
//        this.peakHeigh = peakHeigh;
//    }
//
//    public double getPeakArea() {
//        return peakArea;
//    }

//    public void setPeakArea(double peakArea) {
//        this.peakArea = peakArea;
//    }

    public double getIQR() {
        return IQR;
    }

    public void setIQR(double IQR) {
        this.IQR = IQR;
    }

    public double getAuc() {
        return auc;
    }

    public void setAuc(double auc) {
        this.auc = auc;
    }

    public double getSNR70() {
        return SNR70;
    }

    public void setSNR70(double SNR70) {
        this.SNR70 = SNR70;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }
}

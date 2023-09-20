/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.handlers;

import com.compomics.util.experiment.io.mass_spectrometry.MsFileExporter;
import com.compomics.util.experiment.io.mass_spectrometry.cms.CmsFileReader;
import com.compomics.util.experiment.io.mass_spectrometry.mgf.MgfFileWriter;
import com.compomics.util.experiment.mass_spectrometry.spectra.Spectrum;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.models.SpectrumModel;
import no.uib.probe.utils.LocalWaitingDialog;
import no.uib.probe.utils.Parameters;
import no.uib.probe.utils.Util;
import no.uib.probe.utils.statistics.AUCCalculator;
import no.uib.probe.utils.statistics.BaselineNoiseRemoval;
import no.uib.probe.utils.statistics.SNRCalculator;
import static no.uib.probe.utils.statistics.SNRCalculator.calculateSignalNoiseRatioOnLevel;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author yfa041
 */
public class CMSFileHandler {

    private final Set<String> badSpectrumTitles;

    public String getSpectrumFileName() {
        return spectrumFileName;
    }

    public Map<String, SpectrumModel> getFullSpectraMap() {
        return fullSpectraMap;
    }

    private final File cmsFile;
    private CmsFileReader cmsFileReader;
    private final String spectrumFileName;
    private final Map<String, SpectrumModel> fullSpectraMap;
    private final Map<String, SpectrumModel> badSpectraMap;
    private final LocalWaitingDialog waitingHandler;

    public CMSFileHandler(File cmsFile) {
        this.fullSpectraMap = new LinkedHashMap<>();
        badSpectraMap = new HashMap<>();
        this.cmsFile = cmsFile;
        this.spectrumFileName = cmsFile.getName().replace(".cms", "");//cmsFile.getName().toLowerCase().replace(".cms", "").replace(".mzid", "").replace(".mzml", "");
        this.badSpectrumTitles = new LinkedHashSet<>();
        this.waitingHandler = new LocalWaitingDialog();

    }

    public void processCMS() {
        if (cmsFileReader == null) {
            this.readCSMFile(cmsFile);
            this.normalizeInitFullSpectraMap();
            System.out.println("init the cms file data");
        }
    }

    private void readCSMFile(File cmsFile) {
        try {
            cmsFileReader = new CmsFileReader(cmsFile, null);
            System.out.println("at cms length " + cmsFileReader.getSpectrumTitles("").length);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Set<String> getBadSpectrumTitles() {
        return badSpectrumTitles;
    }

    private void normalizeInitFullSpectraMap() {
        fullSpectraMap.clear();
        String[] spectrumTitles = cmsFileReader.getSpectrumTitles("");

        for (String spectrumTitle : spectrumTitles) {
            SpectrumModel spectrumModel = new SpectrumModel();
            Spectrum spectrum = cmsFileReader.getSpectrum(spectrumTitle);
//            spectrumModel.setSpectrum(spectrum);
            spectrumModel.setSpectrumKey(spectrumFileName + "__" + spectrumTitle);
            spectrumModel.setSpectrumTitle(spectrumTitle);
            spectrumModel.setFile(spectrumFileName);
            double possibleCharge = 0;
            if (spectrum.getPrecursor().getPossibleChargesAsString() != null && !spectrum.getPrecursor().getPossibleChargesAsString().isBlank()) {
                possibleCharge = Double.parseDouble(spectrum.getPrecursor().getPossibleChargesAsString().replace("+", ""));
            }

            spectrumModel.setTheoreticalCharge(possibleCharge);
            spectrumModel.setTheoreticalMass(spectrum.getPrecursor().mz);
            //normalize the values removing baseline signal
            if (calculateNormalizedSpectrumFeatures(spectrumModel, spectrum)) {
                fullSpectraMap.put(spectrumModel.getSpectrumTitle(), spectrumModel);
            } else {
                badSpectraMap.put(spectrumModel.getSpectrumTitle(), spectrumModel);
            }

        }

    }
//

    private boolean calculateNormalizedSpectrumFeatures(SpectrumModel spectrumModel, Spectrum spectrum) {
        double[] intensities = spectrum.intensity;
        double[] mzValues = spectrum.mz;
        int windowNumber = Math.min(5, Math.max(1, (int) (intensities.length * 0.1)));
        double[][] correctedFullData = BaselineNoiseRemoval.removeBaselineNoise(intensities, windowNumber, mzValues);
        double[] correctedData = correctedFullData[0];
        double[] correctedMzData = correctedFullData[1];

        DescriptiveStatistics descriptiveStatisticsData = new DescriptiveStatistics(correctedData);
        if (correctedData.length < 2) {
            badSpectrumTitles.add(spectrumModel.getSpectrumTitle());
            return false;

        }
        /**
         * Basic Summary Statistics:*
         */
//Mean
        double mean = descriptiveStatisticsData.getMean();
        spectrumModel.setMean(mean);

//Median
        int medianIndex = correctedData.length / 2;
        double median;
        if (medianIndex % 2 == 0) {
            median = descriptiveStatisticsData.getSortedValues()[medianIndex];
            median = (median + descriptiveStatisticsData.getSortedValues()[medianIndex + 1]) / 2.0;
        } else {
            medianIndex = (medianIndex / 2) + 1;
            median = descriptiveStatisticsData.getSortedValues()[medianIndex];
        }

        spectrumModel.setMedian(median);
//Standard Deviation
        double sd = descriptiveStatisticsData.getStandardDeviation();
        spectrumModel.setSd(sd);

//Variance
        double variance = descriptiveStatisticsData.getVariance();
        spectrumModel.setVariance(variance);

//Skewness
        double skewness = descriptiveStatisticsData.getSkewness();
        spectrumModel.setSkews(skewness);
        if (Double.isNaN(skewness)) {
            return false;
        }
//Kurtosis
        double kurtosis = descriptiveStatisticsData.getKurtosis();
        spectrumModel.setKurtosis(kurtosis);
        if (Double.isNaN(kurtosis)) {
            return false;
        }
//Range (Max - Min)
        double range = descriptiveStatisticsData.getMax() - descriptiveStatisticsData.getMin();
        spectrumModel.setRange(range);
        double IQR = descriptiveStatisticsData.getPercentile(0.75) - descriptiveStatisticsData.getPercentile(0.25);
        spectrumModel.setIQR(IQR);

        /**
         * Peak-Based Features:
         */
//        Number of Peaks
        spectrumModel.setNumPeaks(correctedData.length);
//      Average Peak Intensity 
        double averagePeakIntensity = descriptiveStatisticsData.getSum() / (double) correctedData.length;
        spectrumModel.setAveragePeakIntensity(averagePeakIntensity);
//        Total Ion Count (Sum of all peak intensities)
        spectrumModel.setTotalIntensity(descriptiveStatisticsData.getSum());
        //        Peak Intensity Ratios (e.g. ratio of intensities between two prominent peaks)
        double peakIntensityRatios = Util.calculateAverageRatios(descriptiveStatisticsData.getSortedValues());
        spectrumModel.setPeakIntensityRatios(peakIntensityRatios);
        // calculate the Area Under the Curve (AUC) for peaks
        double auc = AUCCalculator.calculateAUC(correctedMzData, correctedData);
        spectrumModel.setAuc(auc);

        /**
         * *Signal-to-Noise Ratios: **
         */
        //CoefficientVariations
        double cv = SNRCalculator.calculateCoefficientVariations(descriptiveStatisticsData);
        spectrumModel.setCoefficientVariations(cv);

        //SNR and Peak SNR on 90% and 70%   
        double[] snr_n_90 = calculateSignalNoiseRatioOnLevel(descriptiveStatisticsData.getSortedValues(), 0.9);
        spectrumModel.setSNR90(snr_n_90[0]);
        spectrumModel.setPeakSNR90(snr_n_90[1]);
        if (Double.isNaN(snr_n_90[0])) {
            return false;
        }
        double[] snr_n_70 = SNRCalculator.calculateSignalNoiseRatioOnLevel(descriptiveStatisticsData.getSortedValues(), 0.7);
        spectrumModel.setSNR70(snr_n_70[0]);
        if (Double.isNaN(snr_n_70[0])) {
            return false;
        }
        spectrumModel.setPeakSNR70(snr_n_70[1]);
        double dynamicRange = Math.log10(descriptiveStatisticsData.getMax() / descriptiveStatisticsData.getMin());
        spectrumModel.setDynamicRange(dynamicRange);
        return true;

    }

    public Map<String, SpectrumModel> getBadSpectraMap() {
        return badSpectraMap;
    }

    public void exportAsMgfFile(String mgfFileName, Set<String> spectrumTitles) {

        if (spectrumTitles == null) {
            return;
        }
        File destinationFile = new File(Parameters.data_folder_url, mgfFileName + ".mgf");
        try (MgfFileWriter writer = new MgfFileWriter(destinationFile)) {
            for (String spectrumTitle : spectrumTitles) {
                Spectrum spectrum = cmsFileReader.getSpectrum(spectrumTitle);
                writer.writeSpectrum(spectrumTitle, spectrum);
            }
        }

    }

}

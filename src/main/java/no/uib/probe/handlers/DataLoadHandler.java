package no.uib.probe.handlers;

import com.compomics.util.experiment.io.mass_spectrometry.mgf.IndexedMgfReader;
import com.compomics.util.experiment.io.mass_spectrometry.mgf.MgfIndex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.probe.models.SpectrumModel;
import no.uib.probe.utils.LocalWaitingDialog;
import no.uib.probe.utils.Parameters;
import no.uib.probe.utils.Util;

/**
 * This class is responsible for loading and smoothing the spectra data by
 * removing baseline noise
 *
 * @author Yehia Farag
 */
public class DataLoadHandler {

    public static Map<String, Double[]> getTagData(String dataFolderUrl) {
        Map<String, Double[]> tagsValues = new LinkedHashMap<>();
        File dataFolder = new File(dataFolderUrl);
        if (!dataFolder.exists()) {
            return tagsValues;
        }

        TreeMap<String, Map<String, Double>> fileOrderMap = new TreeMap<>();
        for (File f : dataFolder.listFiles()) {

            if (f.getName().endsWith("_tag_matches.txt")) {
                Map<String, Double> tagResults = readTagFile(f);
                fileOrderMap.put(f.getName(), tagResults);
            }

        }
        int fileCounter = 0;
        for (Map<String, Double> tagResults : fileOrderMap.values()) {
            for (String specId : tagResults.keySet()) {
                if (!tagsValues.containsKey(specId)) {
                    tagsValues.put(specId, new Double[fileOrderMap.size() + 1]);
                }
                tagsValues.get(specId)[fileCounter] = tagResults.get(specId);

            }
            fileCounter++;
        }
        for (String spec : tagsValues.keySet()) {
            double lowE = Double.MAX_VALUE;
            for (int i = 0; i < tagsValues.get(spec).length - 1; i++) {
                if (tagsValues.get(spec)[i] != null) {
                    lowE = Math.min(tagsValues.get(spec)[i], lowE);
                }

            }

//            if (tagsValues.get(spec)[1] != null) {                
//                lowE = Math.min(tagsValues.get(spec)[1], lowE);                
//            }
//            if (tagsValues.get(spec)[2] != null) {
//                lowE = Math.min(tagsValues.get(spec)[2], lowE);
//                
//            }
//             if (tagsValues.get(spec)[2] != null) {
//                lowE = Math.min(tagsValues.get(spec)[2], lowE);
//                
//            }
            tagsValues.get(spec)[tagsValues.get(spec).length - 1] = lowE;

        }

        System.out.println("at tags " + tagsValues.values().iterator().next().length);

        return tagsValues;
    }

    private static Map<String, Double> readTagFile(File tagFile) {
        Map<String, Double> tagResults = new LinkedHashMap<>();
        try {
            BufferedReader buf = new BufferedReader(new FileReader(tagFile));

            String header = buf.readLine();

            while (true) {
                String str = buf.readLine();
                if (str == null) {
                    break;
                }
                double evalue = Double.valueOf(str.split("\t")[12]);
//                if (evalue <= Parameters.e_value_threshold) {
                tagResults.put(str.split("\t")[1].trim(), evalue);
                if (str.split("\t")[1].trim().equalsIgnoreCase("controllerType=0 controllerNumber=1 scan=9882")) {
                    System.out.println("at e value " + evalue + "   " + str);
                }
//                }

                buf.readLine();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return tagResults;
    }

    public static Set<SpectrumModel> getSpectraSet(String dataFolderUrl) {
        //read id files
        File dataFolder = new File(dataFolderUrl);
        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            return null;
        }
        for (File f : dataFolder.listFiles()) {
            if (f.getName().endsWith(".mzid")) {
                System.out.println("1 file is " + f.getName());
            } else if (f.getName().endsWith(".csv")) {
                System.out.println("2 file is " + f.getName());
            } else if (f.getName().endsWith(".mztab")) {
                System.out.println("2 file is " + f.getName());
                try {
                    BufferedReader buf = new BufferedReader(new FileReader(f));

                    while (true) {
                        String str = buf.readLine();
                        if (str.contains("sequence")) {
//                            int i = 0;
//                            for (String header : str.split("\t")) {
//                                System.out.println(i + " " + header);
//                                i++;
//                            }
                            break;
                        }
                    }
//                    int count = 0;
//                    while (true) {
//                        String str = buf.readLine();
//                        System.out.println("at first line " + str.split("\t")[2] + "   " + str.split("\t")[7] + "   " + str.split("\t")[8] + "   " + str.split("\t")[9] + "    " + str.split("\t")[21]);
//                        if (count == 1000) {
//                            break;
//                        }
//                        count++;
//                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        }

        Map<String, Boolean> identificationMap = new HashMap<>();

        Set<SpectrumModel> spectra = new LinkedHashSet<>();

        return spectra;
    }

    public static Map<String, double[]> getIdSet(String dataFolderUrl) {
        Map<String, double[]> idSet = new LinkedHashMap<>();
        File dataFolder = new File(dataFolderUrl);
        if (!dataFolder.exists()) {
            return idSet;
        }

        System.out.println("at datafolder " + dataFolder.getName());
        for (File f : dataFolder.listFiles()) {
            if (false && (f.getName().endsWith(".first.csv") || f.getName().endsWith(".lower.csv"))) {

                try {
                    try (BufferedReader buf = new BufferedReader(new FileReader(f))) {
                        String[] headers = buf.readLine().split(",");
//                    int i = 0;
//                    for (String header : headers) {
//                        System.out.println("at header " + i + " - " + header);
//                        i++;
//                    }
                        while (true) {
                            String line = buf.readLine();
                            if (line == null) {
                                break;
                            }
                            String[] row = line.split(",");
                            if (row[3].replace(".mgf", "").equalsIgnoreCase(dataFolder.getName())) {
                                idSet.put(row[1].trim(), new double[]{Double.parseDouble(row[17]), Double.parseDouble(row[18])});
                            }
//
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else if (f.getName().endsWith(".comet.txt")) {

                try {
                    try (BufferedReader buf = new BufferedReader(new FileReader(f))) {
                        buf.readLine().split("\t");
                        String[] headers = buf.readLine().split("\t");
                        int i = 0;
                        for (String header : headers) {
                            System.out.println("at header " + i + " - " + header);
                            i++;
                        }
                        while (true) {
                            String line = buf.readLine();
                            if (line == null) {
                                break;
                            }
                            String[] row = line.split("\t");
//                            if (row.length == 8 && !row[7].equalsIgnoreCase("Confidentd")) {
//                                idSet.put(row[3].trim(), new double[]{0, 001, 0, 001});
//                            } else if (row.length == 8 && !row[7].equalsIgnoreCase("Doubtful")) {
//                                idSet.put(row[3].trim(), new double[]{0, 1, 0, 1});
//                            } else {
//                                idSet.put(row[3].trim(), new double[]{1, 1});
//                            }
                            String title = "controllerType=0 controllerNumber=1 scan=" + row[0];
//                            if (row[3].replace(".mgf", "").equalsIgnoreCase(dataFolder.getName())) {
                            idSet.put(title.trim(), new double[]{Double.parseDouble(row[5]), Double.parseDouble(row[5])});
//                            }
//
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        }
        return idSet;

    }

    public static MgfIndex getMgfIndex(String mgfFilePath) {

        File mgfFile = new File(mgfFilePath);
        if (!mgfFile.exists()) {
            return null;
        }
        try {
            MgfIndex mgfIndex = IndexedMgfReader.getMgfIndex(mgfFile);

            return mgfIndex;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;

    }
    public static Map<String, SpectrumModel> badSpectrumMap = new HashMap<>();
    private static CMSFileHandler cmsFileHandler;

    public static Map<String, SpectrumModel> processMGFFile(String dataFolderUrl) {
        Map<String, SpectrumModel> fullSpectMap = new LinkedHashMap<>();
        badSpectrumMap.clear();
        File dataFolder = new File(dataFolderUrl);
        if (!dataFolder.exists()) {
            return fullSpectMap;
        }
        for (File f : dataFolder.listFiles()) {
            if (f.getName().endsWith(".mgf")) {
                File cmsFile = new File(f.getParent(), f.getName().replace(".mgf", ".cms"));
                if (cmsFile.exists()) {
                    cmsFile.delete();
                }
                try {
                    Util.writeCmsFile(f, cmsFile, new LocalWaitingDialog());
                    cmsFileHandler = new CMSFileHandler(cmsFile);
                    cmsFileHandler.processCMS();
                    fullSpectMap.putAll(cmsFileHandler.getFullSpectraMap());
                    badSpectrumMap.putAll(cmsFileHandler.getBadSpectraMap());
//                    for(String bad:cmsFileHandler.getBadSpectrumTitles()){
//                        if(full.contains(bad))
//                            System.out.println("bad spect identified "+bad);
//                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return fullSpectMap;

    }

    public static void exportToMGFFile(String mgfFileName, Set<String> spectrumTitles) {
        if (cmsFileHandler != null) {
            cmsFileHandler.exportAsMgfFile(mgfFileName, spectrumTitles);
        }
    }

}

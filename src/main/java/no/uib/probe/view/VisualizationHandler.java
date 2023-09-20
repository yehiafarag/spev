/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.handlers.DataLoadHandler;
import no.uib.probe.models.LocalDataFrame;
import no.uib.probe.plots.DensityPlot;
import no.uib.probe.plots.StackedBarChart;
import no.uib.probe.utils.Parameters;
import no.uib.probe.utils.Util;
import no.uib.probe.utils.VisualizationUtil;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author yfa041
 */
public class VisualizationHandler {

    public VisualizationHandler() {
    }

    public static Set<String>[][] viewDirectTagData(Map<String, Double[]> directTagIdentifications, Map<String, double[]> searchEngineidintifications, int totalSpectaNumber, LocalDataFrame dataset) {

        Map<String, List<Double>> inputData = new LinkedHashMap<>();
        Map<String, Integer[]> tagsDataCount = new LinkedHashMap<>();
        inputData.put("tag length 1", new ArrayList<>());
        inputData.put("tag length 2", new ArrayList<>());
        inputData.put("tag length 3", new ArrayList<>());
        inputData.put("tag length 4", new ArrayList<>());
        inputData.put("full tags", new ArrayList<>());
        inputData.put("SearchQvalue", new ArrayList<>());
        tagsDataCount.put("tag length 1", new Integer[]{0, totalSpectaNumber});
        tagsDataCount.put("tag length 2", new Integer[]{0, totalSpectaNumber});
        tagsDataCount.put("tag length 3", new Integer[]{0, totalSpectaNumber});
        tagsDataCount.put("tag length 4", new Integer[]{0, totalSpectaNumber});
        tagsDataCount.put("full tags", new Integer[]{0, totalSpectaNumber});
        tagsDataCount.put("SearchPEPvalue", new Integer[]{0, totalSpectaNumber});
        tagsDataCount.put("SearchQvalue", new Integer[]{0, totalSpectaNumber});

        for (String spectTitle : directTagIdentifications.keySet()) {
            Double[] subData = directTagIdentifications.get(spectTitle);
            int i = 0;
            for (Double d : subData) {
                if (d != null) {
                    String key = ("tag length " + (i + 1)).replace("tag length 5", "full tags");
                    inputData.get(key).add(d);
                    if (d <= Parameters.e_value_threshold) {
                        tagsDataCount.get(key)[0] = tagsDataCount.get(key)[0] + 1;
                        tagsDataCount.get(key)[1] = tagsDataCount.get(key)[1] - 1;
                    }
//                    else if (d < 0.1) {
//                        tagsDataCount.get(key)[1] = tagsDataCount.get(key)[1] + 1;
//                        tagsDataCount.get(key)[3] = tagsDataCount.get(key)[3] - 1;
//                    } 
//                    else {
//                        tagsDataCount.get(key)[2] = tagsDataCount.get(key)[2] + 1;
//                        tagsDataCount.get(key)[3] = tagsDataCount.get(key)[3] - 1;
//                    }
                }
                i++;
            }

        }
        for (String title : searchEngineidintifications.keySet()) {
            Double value = searchEngineidintifications.get(title)[0];
            Double value2 = searchEngineidintifications.get(title)[1];
            String key = "SearchQvalue";
            String key2 = "SearchPEPvalue";

            inputData.get(key).add(value);
            if (value <= Parameters.searchEngine_threshold) {
                tagsDataCount.get(key)[0] = tagsDataCount.get(key)[0] + 1;
                tagsDataCount.get(key)[1] = tagsDataCount.get(key)[1] - 1;
            }
//            else {
//                tagsDataCount.get(key)[1] = tagsDataCount.get(key)[1] + 1;
//                tagsDataCount.get(key)[3] = tagsDataCount.get(key)[3] - 1;
//            }
            if (value2 <= Parameters.searchEngine_threshold) {
                tagsDataCount.get(key2)[0] = tagsDataCount.get(key2)[0] + 1;
                tagsDataCount.get(key2)[1] = tagsDataCount.get(key2)[1] - 1;
            }
//            else {
//                tagsDataCount.get(key2)[1] = tagsDataCount.get(key2)[1] + 1;
//                tagsDataCount.get(key2)[3] = tagsDataCount.get(key2)[3] - 1;
//            }
        }

//         inputData.put("full tags", new ArrayList<>());
//         inputData.get("full tags").addAll(inputData.get("tag length 5"));
//         inputData.remove("tag length 5");
//        double[][] data = new double[tagsDataCount.size()][];
//        int i = 0;
//        for (List<Double> noNullList : inputData.values()) {
//            data[i] = new double[noNullList.size()];
//            for (int j = 0; j < data[i].length; j++) {
//                data[i][j] = noNullList.get(j);
//            }
//            i++;
//        }
//            DensityPlot dp = new DensityPlot("compare tags", "evalue", "", data);
//            BoxAndWhisker bp = new BoxAndWhisker(inputData, "Compare evalue for tags");
//
//            Util.exportToImg(dp.getLineChart(), "Compare_Tags_Density.png");
//            Util.exportToImg(bp.getChart(), "Compare_Tags_BoxPlot.png");
        StackedBarChart sbt = new StackedBarChart("Search & Tags (" + Parameters.e_value_threshold + ")", tagsDataCount, VisualizationUtil.createSearchTagLegendItems()) {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
            }

        };
        Util.exportToImg(sbt.getChart(), "Compare_Tags_Confident distribution.png");
        sbt.pack();
        RefineryUtilities.centerFrameOnScreen(sbt);
        sbt.setVisible(true);

        //Check agreements between both
        Map<String, Object[]> agreedDataIds = new LinkedHashMap<>();
        Map<String, Integer[]> agreedDataCount = new LinkedHashMap<>();
        agreedDataCount.put("tag length 1", new Integer[]{0, 0, 0, totalSpectaNumber});
        agreedDataCount.put("tag length 2", new Integer[]{0, 0, 0, totalSpectaNumber});
        agreedDataCount.put("tag length 3", new Integer[]{0, 0, 0, totalSpectaNumber});
        agreedDataCount.put("tag length 4", new Integer[]{0, 0, 0, totalSpectaNumber});
        agreedDataCount.put("full tags", new Integer[]{0, 0, 0, totalSpectaNumber});

        Object[] hsArr = new Object[]{new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()};

        agreedDataIds.put("tag length 1", new Object[]{new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()});

        agreedDataIds.put("tag length 2", new Object[]{new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()});
        agreedDataIds.put("tag length 3", new Object[]{new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()});
        agreedDataIds.put("tag length 4", new Object[]{new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()});
        agreedDataIds.put("full tags", new Object[]{new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()});

        Set<String> fullIdentSpectIds = new LinkedHashSet<>();
        fullIdentSpectIds.addAll(directTagIdentifications.keySet());
        fullIdentSpectIds.addAll(searchEngineidintifications.keySet());

        for (String specTitle : fullIdentSpectIds) {

            if (searchEngineidintifications.containsKey(specTitle) && directTagIdentifications.containsKey(specTitle)) {
//                Double qvalue = searchEngineidintifications.get(specTitle)[0];
                Double pepvalue = searchEngineidintifications.get(specTitle)[1];
                double conf = pepvalue;
                Double[] subData = directTagIdentifications.get(specTitle);
                int i = 0;
                for (String key : agreedDataCount.keySet()) {
                    if (subData[i] != null && subData[i] <= Parameters.e_value_threshold && (conf <= Parameters.searchEngine_threshold)) {
                        agreedDataCount.get(key)[0] = agreedDataCount.get(key)[0] + 1;
                        agreedDataCount.get(key)[3] = agreedDataCount.get(key)[3] - 1;
                        ((Set<String>) agreedDataIds.get(key)[0]).add(specTitle);

                    } else if (subData[i] != null && subData[i] <= Parameters.e_value_threshold && (conf > Parameters.searchEngine_threshold)) {
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] + 1;
                        agreedDataCount.get(key)[3] = agreedDataCount.get(key)[3] - 1;
                        ((Set<String>) agreedDataIds.get(key)[1]).add(specTitle);
                    } else if ((conf <= 0.01)) {
                        agreedDataCount.get(key)[2] = agreedDataCount.get(key)[2] + 1;
                        agreedDataCount.get(key)[3] = agreedDataCount.get(key)[3] - 1;
                        ((Set<String>) agreedDataIds.get(key)[2]).add(specTitle);
                    } else {
                        ((Set<String>) agreedDataIds.get(key)[3]).add(specTitle);
                    }
                    i++;

                }

            } else if (directTagIdentifications.containsKey(specTitle) && !searchEngineidintifications.containsKey(specTitle)) {
                Double[] subData = directTagIdentifications.get(specTitle);
                int i = 0;
                for (String key : agreedDataCount.keySet()) {
                    if (subData[i] != null && subData[i] <= Parameters.e_value_threshold) {
                        agreedDataCount.get(key)[1] = agreedDataCount.get(key)[1] + 1;
                        agreedDataCount.get(key)[3] = agreedDataCount.get(key)[3] - 1;
                        ((Set<String>) agreedDataIds.get(key)[1]).add(specTitle);
                    } else {
                        ((Set<String>) agreedDataIds.get(key)[3]).add(specTitle);
                    }
                    i++;

                }
            } else if (!directTagIdentifications.containsKey(specTitle) && searchEngineidintifications.containsKey(specTitle)) {
                Double pepvalue = searchEngineidintifications.get(specTitle)[1];
                double conf = pepvalue;
                if ((conf <= Parameters.searchEngine_threshold)) {
                    for (String key : agreedDataCount.keySet()) {
                        agreedDataCount.get(key)[2] = agreedDataCount.get(key)[2] + 1;
                        agreedDataCount.get(key)[3] = agreedDataCount.get(key)[3] - 1;
                        ((Set<String>) agreedDataIds.get(key)[2]).add(specTitle);

                    }

                } else {
                    for (String key : agreedDataCount.keySet()) {
                        ((Set<String>) agreedDataIds.get(key)[3]).add(specTitle);
                    }

                }

            } else {
                for (String key : agreedDataCount.keySet()) {
                    ((Set<String>) agreedDataIds.get(key)[3]).add(specTitle);
                }

            }

        }
        StackedBarChart sbt2 = new StackedBarChart("Correlation tags & search (" + Parameters.e_value_threshold + ")", agreedDataCount, VisualizationUtil.createAgreeSearchTagLegendItems()) {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (cme.getEntity() instanceof CategoryItemEntity cItem) {
                    String key = cItem.getColumnKey() + "";
                    Integer itemIndex = Integer.valueOf(cItem.getRowKey().toString().replace(key, "").trim());
                    System.out.println("at chart cliked " + key + "   " + itemIndex + "  " + ((Set<String>) agreedDataIds.get(key)[itemIndex]).size() + "   ");
                    Set<String> str = ((Set<String>) agreedDataIds.get(key)[itemIndex]);

                    //export the data into spectra
//                    DataLoadHandler.exportToMGFFile(key + "__" + itemIndex, str);
                    // DensityPlot dp
                    double[][] subData = new double[str.size()][];
                    int reIndex = 0;
                    for (String sid : str) {
                        int index = dataset.getSpectrumIndex(sid);
                        double[] dataRow = dataset.getData()[index];
                       
                        dataRow[21] = dataset.getSpectraMap().get(sid).getTagsEValues()[2];
                        subData[reIndex] = dataRow;
                        reIndex++;
                    }
                    double[][] columnsAsData = new double[subData[0].length][subData.length];
                    for (int ci = 0; ci < subData.length; ci++) {
                        for (int j = 0; j < subData[0].length; j++) {
                            columnsAsData[j][ci] = subData[ci][j];
                        }
                    }

                    for (int column=1;column<dataset.getColumnId().length-1;column++ ) {
                        System.out.println("at column ID " + dataset.getColumnId()[column]);
                        DensityPlot dp = new DensityPlot(dataset.getColumnId()[column], dataset.getColumnId()[column], "count", columnsAsData[column-1]);
                    }

//                    Map<String, Integer> data = new LinkedHashMap<>();
//                    data.put("key ", 5);
//                    data.put("key 1", 5);
//                    data.put("key 2", 5);
//                    data.put("key 3", 5);
//                    data.put("key 4", 5);
//                    PieChart pchart = new PieChart(key + "  cat " + itemIndex, data);
//                    pchart.pack();
//                    RefineryUtilities.centerFrameOnScreen(pchart);
//                    pchart.setVisible(true);
                }

            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
            }

        };
        Util.exportToImg(sbt2.getChart(), "Compare_Agreed_Confident distribution.png");
        sbt2.pack();
        RefineryUtilities.centerFrameOnScreen(sbt2);
        sbt2.setVisible(true);

        int[][] confidentCountMatrix = new int[2][2];
        confidentCountMatrix[1][1] = totalSpectaNumber;
        Set<String>[][] classfiedDataMatrix = new Set[2][2];

        for (String specTitle : fullIdentSpectIds) {
            int x;
            int y;
            if (searchEngineidintifications.containsKey(specTitle) && directTagIdentifications.containsKey(specTitle) && directTagIdentifications.get(specTitle)[Parameters.selected_tag_length] != null) {
//                double e = ;
                double q = searchEngineidintifications.get(specTitle)[1];
                x = 0;
                y = Util.getCategoryIndex(q);

            } else if (directTagIdentifications.containsKey(specTitle) && directTagIdentifications.get(specTitle)[Parameters.selected_tag_length] != null) {
//                double e = directTagIdentifications.get(specTitle)[4];
                x = 0;
                y = 1;
            } else if (searchEngineidintifications.containsKey(specTitle)) {
                double q = searchEngineidintifications.get(specTitle)[1];
                x = 1;
                y = Util.getCategoryIndex(q);
            } else {
                x = 1;
                y = 1;

            }
            if (classfiedDataMatrix[x][y] == null) {
                classfiedDataMatrix[x][y] = new LinkedHashSet<>();
            }

            confidentCountMatrix[x][y] = confidentCountMatrix[x][y] + 1;
            confidentCountMatrix[1][1] = confidentCountMatrix[1][1] - 1;
            classfiedDataMatrix[x][y].add(specTitle);
        }
        System.out.println("     ID   UnID");
int        i = 0;
        for (int[] row : confidentCountMatrix) {
            System.out.print("ID,UnID".split(",")[i++] + "   ");
            for (int v : row) {
                System.out.print(v + "    ");
            }
            System.out.println("");

        }
        return classfiedDataMatrix;

    }

    public static void viewPieChart() {

    }

}

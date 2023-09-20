/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.utils;

import java.awt.Color;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;

/**
 *
 * @author yfa041
 */
public class VisualizationUtil {
       /**
     * Creates the legend items for the chart. In this case, we set them
     * manually because we only want legend items for a subset of the data
     * series.
     *
     * @return The legend items.
     */
    public static  LegendItemCollection createSearchTagLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("Identified", Color.GREEN.darker().darker());
//        LegendItem item2 = new LegendItem("Less Confident", Color.GREEN.brighter().brighter());
//        LegendItem item3 = new LegendItem("Not Confident", Color.ORANGE.brighter().brighter());
        LegendItem item4 = new LegendItem("UnIdentified", Color.RED);
        result.add(item1);
//        result.add(item2);
//        result.add(item3);
        result.add(item4);
        return result;
    }
    
       /**
     * Creates the legend items for the chart. In this case, we set them
     * manually because we only want legend items for a subset of the data
     * series.
     *
     * @return The legend items.
     */
    public static  LegendItemCollection createAgreeSearchTagLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("Agreed Identified", Color.GREEN.darker().darker());
        LegendItem item2 = new LegendItem("Identified on Tag", Color.MAGENTA);
        LegendItem item3 = new LegendItem("Identified on SE", Color.ORANGE);
        LegendItem item4 = new LegendItem("Agreed UnIdentified", Color.RED);
        result.add(item1);
        result.add(item2);
        result.add(item3);
        result.add(item4);
        return result;
    }
    
}

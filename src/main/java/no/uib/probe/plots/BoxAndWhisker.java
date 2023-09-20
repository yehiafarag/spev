/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.plots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import no.uib.probe.utils.Util;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 *
 * @author yfa041
 */
public class BoxAndWhisker extends JPanel {
    private final JFreeChart chart ;
    private final Color[] defautColours = new Color[]{
        new Color(0, 128, 0, 125),
        new Color(255, 0, 0, 125),
        new Color(0, 0, 212, 125),
        new Color(213, 39, 183, 125),
        new Color(247, 130, 194, 125),
        new Color(249, 196, 107, 125),
        new Color(69, 77, 102, 125),
        new Color(48, 153, 117, 125),
        new Color(88, 179, 104, 125),
        new Color(218, 216, 115, 125)};

    public BoxAndWhisker(Map<String, List<Double>> inputData, final String title) {

        BoxAndWhisker.this.setPreferredSize(new Dimension(800, 600));
        BoxAndWhisker.this.setVisible(true);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);

        renderer.setSeriesPaint(0, defautColours[0]);
        renderer.setSeriesPaint(1, defautColours[1]);
        renderer.setSeriesPaint(2, defautColours[2]);
//        renderer.setUseOutlinePaintForWhiskers(true);
        Font legendFont = new Font("SansSerif", Font.PLAIN, 16);
        renderer.setLegendTextFont(0, legendFont);
        renderer.setLegendTextFont(1, legendFont);
        renderer.setMedianVisible(true);
        renderer.setMeanVisible(false);
//        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final BoxAndWhiskerCategoryDataset jfreedataset = createDataset(inputData, title);

        final CategoryAxis xAxis = new CategoryAxis("Group");
        final NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);

        final CategoryPlot plot = new CategoryPlot(jfreedataset, xAxis, yAxis, renderer);

       chart = new JFreeChart(
                title,
                new Font("SansSerif", Font.BOLD, 14),
                plot,
                true
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        BoxAndWhisker.this.add(chartPanel);
        JFrame frame = new JFrame();
        frame.setContentPane(BoxAndWhisker.this);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private BoxAndWhiskerCategoryDataset createDataset(Map<String, List<Double>> inputDat, String title) {

        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        for (String key : inputDat.keySet()) {
            final List list = new ArrayList();
            for (double b : inputDat.get(key)) {
                list.add(b);
//                list.add(Util.log2Scale(b));
            }
            // add some values...        
            dataset.add(list, key, title);
        }

        return dataset;

    }

    public JFreeChart getChart() {
        return chart;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.plots;

import com.compomics.util.math.statistics.distributions.NormalKernelDensityEstimator;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import no.uib.jsparklines.renderers.util.AreaRenderer;
import no.uib.probe.utils.Util;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author yfa041
 */
public class DensityPlot extends JPanel {

    private final JFreeChart lineChart;
    private final XYPlot plot;
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

    public DensityPlot(String title, String xAxisName, String yLabel, double[] data) {

        DensityPlot.this.setSize(800, 600);
        AreaRenderer renderer = new AreaRenderer();
        renderer.setOutline(true);
        XYSeriesCollection lineChartDataset = createDataset(data);

        LegendItemCollection lic = new LegendItemCollection();
        for (int i = 0; i < lineChartDataset.getSeriesCount(); i++) {

            XYSeries ser = lineChartDataset.getSeries(i);
            ser.setDescription("e value");
            // Java 'Color' class takes 3 floats, from 0 to 1.
            Color serColor = defautColours[0];

//            Color serColor = defautColours[i];//new Color(r, g, b, 0.5f);
//            renderer.setSeriesFillPaint(i, serColor);
//            renderer.setSeriesOutlinePaint(i, serColor.darker());
            lic.add(new LegendItem(ser.getDescription(), serColor));

        }

        lineChart = ChartFactory.createXYLineChart(
                title,
                xAxisName,
                "Density",
                lineChartDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        plot = lineChart.getXYPlot();
        plot.setRenderer(renderer);

        lineChart.getPlot().setBackgroundPaint(Color.WHITE);
        LegendItemSource lis = () -> lic;

        lineChart.addLegend(new LegendTitle(lis));
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        this.add(chartPanel);
        DensityPlot.this.setVisible(true);
        JFrame frame = new JFrame();
        frame.setContentPane(DensityPlot.this);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public JFreeChart getLineChart() {
        return lineChart;
    }

    public DensityPlot(String title, String xAxisName, String yLabel, double[][] data) {

        DensityPlot.this.setSize(800, 600);
        AreaRenderer renderer = new AreaRenderer();
        renderer.setOutline(true);
        XYSeriesCollection lineChartDataset = createDataset(data);

        LegendItemCollection lic = new LegendItemCollection();
        for (int i = 0; i < lineChartDataset.getSeriesCount(); i++) {

            XYSeries ser = lineChartDataset.getSeries(i);
            ser.setDescription(("Tag length "+(i+2)).replace("Tag length 5", "Full tags"));
            // Java 'Color' class takes 3 floats, from 0 to 1.
            Color serColor = defautColours[i];

//            Color serColor = defautColours[i];//new Color(r, g, b, 0.5f);
            renderer.setSeriesFillPaint(i, serColor);
            renderer.setSeriesOutlinePaint(i, serColor.darker());
            lic.add(new LegendItem(ser.getDescription(), serColor));

        }

        lineChart = ChartFactory.createXYLineChart(
                title,
                xAxisName,
                "Density",
                lineChartDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        plot = lineChart.getXYPlot();
        plot.setRenderer(renderer);
      

        lineChart.getPlot().setBackgroundPaint(Color.WHITE);
        LegendItemSource lis = () -> lic;

        lineChart.addLegend(new LegendTitle(lis));
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        this.add(chartPanel);
        DensityPlot.this.setVisible(true);
        JFrame frame = new JFrame();
        frame.setContentPane(DensityPlot.this);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private XYSeriesCollection createDataset(double[][] mdata) {

        XYSeriesCollection lineChartDataset = new XYSeriesCollection();

        NormalKernelDensityEstimator kernelEstimator = new NormalKernelDensityEstimator();
        int c=2;
        for (double[] data : mdata) {
            XYSeries activeSer = new XYSeries("e-value -Tag length "+c);
            ArrayList list = kernelEstimator.estimateDensityFunction(data);
            double[] xValues = (double[]) list.get(0);
            double[] yValues = (double[]) list.get(1);

            for (int i = 0; i < xValues.length; i++) {
                activeSer.add(Util.log2Scale(xValues[i]), yValues[i]);
            }
            lineChartDataset.addSeries(activeSer);
            c++;
        }

        return lineChartDataset;
    }
    
      private XYSeriesCollection createDataset(double[] data) {
        
        XYSeriesCollection lineChartDataset = new XYSeriesCollection();

        NormalKernelDensityEstimator kernelEstimator = new NormalKernelDensityEstimator();

        XYSeries activeSer = new XYSeries("e-value");

        ArrayList list = kernelEstimator.estimateDensityFunction(data);
        double[] xValues = (double[]) list.get(0);
        double[] yValues = (double[]) list.get(1);

        for (int i = 0; i < xValues.length; i++) {
            activeSer.add(xValues[i], yValues[i]);
        }
        lineChartDataset.addSeries(activeSer);

        return lineChartDataset;
    }


}

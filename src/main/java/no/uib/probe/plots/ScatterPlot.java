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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author yfa041
 */
public class ScatterPlot extends JPanel {

    private final JFreeChart scatterPlot;
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

    public ScatterPlot(String title, String xAxisLabel, String yAxisLabel, double[][] data) {

        ScatterPlot.this.setSize(800, 600);
//        AreaRenderer renderer = new AreaRenderer();
//        renderer.setOutline(true);
        XYDataset lineChartDataset = createDataset(data);

//        LegendItemCollection lic = new LegendItemCollection();
//        for (int i = 0; i < lineChartDataset.getSeriesCount(); i++) {
//
//            XYSeries ser = lineChartDataset.getSeries(i);
//            ser.setDescription("e value");
//            // Java 'Color' class takes 3 floats, from 0 to 1.
//            Color serColor = defautColours[0];
//
////            Color serColor = defautColours[i];//new Color(r, g, b, 0.5f);
//            renderer.setSeriesFillPaint(i, serColor);
//            renderer.setSeriesOutlinePaint(i, serColor.darker());
//            lic.add(new LegendItem(ser.getDescription(), serColor));
//
//        }

        scatterPlot = ChartFactory.createScatterPlot(
                title,
                xAxisLabel,
                yAxisLabel,
                lineChartDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        plot = scatterPlot.getXYPlot();
//        plot.setRenderer(renderer);

        scatterPlot.getPlot().setBackgroundPaint(Color.WHITE);
//        LegendItemSource lis = () -> lic;

//        scatterPlot.addLegend(new LegendTitle(lis));
        ChartPanel chartPanel = new ChartPanel(scatterPlot);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        this.add(chartPanel);
        ScatterPlot.this.setVisible(true);
        JFrame frame = new JFrame();
        frame.setContentPane(ScatterPlot.this);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

   
    private XYSeriesCollection createDataset(double[][] mdata) {

        XYSeriesCollection lineChartDataset = new XYSeriesCollection();

       
            XYSeries activeSer = new XYSeries("");
            double[] xValues = mdata[1];
            double[] yValues = mdata[0];

            for (int i = 0; i < xValues.length; i++) {
                activeSer.add(Util.log2Scale(xValues[i]), Util.log2Scale(yValues[i]));
//                activeSer.add(xValues[i],yValues[i]);
            }
            lineChartDataset.addSeries(activeSer);
    
        

        return lineChartDataset;
    }
    
  


}

package no.uib.probe.plots;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author yfa041
 */
public abstract class StackedBarChart extends JFrame implements ChartMouseListener{

    private final Paint[] paints1 = new Paint[]{Color.GREEN.darker().darker(),Color.MAGENTA, Color.ORANGE, Color.RED};
    private final Paint[] paints2 = new Paint[]{Color.GREEN.darker().darker(), Color.RED};

    public JFreeChart getChart() {
        return chart;
    }
    private  JFreeChart chart;

    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     * @param data
     * @param legendItemCollection
     */
    public StackedBarChart(final String title, Map<String, Integer[]> data, LegendItemCollection legendItemCollection) {
        super(title);
        final CategoryDataset dataset = createDataset(data);
        chart = createChart(dataset, title, data, legendItemCollection);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
        chartPanel.addChartMouseListener(StackedBarChart.this);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private CategoryDataset createDataset(Map<String, Integer[]> data) {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        for (String key : data.keySet()) {
            int i = 0;
            Integer[] counts = data.get(key);
            for (int count : counts) {
                result.addValue((double) count, key + " " + i, key);
                i++;
            }
        }
        return result;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset for the chart.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset, String title, Map<String, Integer[]> data, LegendItemCollection lic) {

       chart = ChartFactory.createStackedBarChart(
                title, // chart title
                "Category", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // the plot orientation
                true, // legend
                true, // tooltips
                false // urls
        );

        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        renderer.setItemMargin(0.9);
        int serSize = data.values().iterator().next().length;
        Paint[] paints;
        if (serSize == 2) {
            paints = paints2;
        } else {
            paints = paints1;
        }
        int factor;
        for (int i = 0; i < paints.length; i++) {
            factor = i;
            while (factor < (i + data.size() * serSize)) {
           
                renderer.setSeriesPaint(factor, paints[i]);
                factor = factor + serSize;
            }

        }

//        if (data.size() == 6) {
//            Paint p1 = Color.GREEN.darker().darker();
//            renderer.setSeriesPaint(0, p1);
//            renderer.setSeriesPaint(4, p1);
//            renderer.setSeriesPaint(8, p1);
//            renderer.setSeriesPaint(12, p1);
//            renderer.setSeriesPaint(16, p1);
//            renderer.setSeriesPaint(20, p1);
//
//            Paint p2 = Color.GREEN.brighter().brighter();
//            renderer.setSeriesPaint(1, p2);
//            renderer.setSeriesPaint(5, p2);
//            renderer.setSeriesPaint(9, p2);
//            renderer.setSeriesPaint(13, p2);
//            renderer.setSeriesPaint(17, p2);
//            renderer.setSeriesPaint(21, p2);
//
//            Paint p3 = Color.ORANGE;
//            renderer.setSeriesPaint(2, p3);
//            renderer.setSeriesPaint(6, p3);
//            renderer.setSeriesPaint(10, p3);
//            renderer.setSeriesPaint(14, p3);
//            renderer.setSeriesPaint(18, p3);
//            renderer.setSeriesPaint(22, p3);
//
//            Paint p4 = Color.RED;
//            renderer.setSeriesPaint(3, p4);
//            renderer.setSeriesPaint(7, p4);
//            renderer.setSeriesPaint(11, p4);
//            renderer.setSeriesPaint(15, p4);
//            renderer.setSeriesPaint(19, p4);
//            renderer.setSeriesPaint(23, p4);
//
//        }
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.setRenderer(renderer);
        if (lic != null) {
            plot.setFixedLegendItems(lic);
        }
        plot.setBackgroundPaint(Color.WHITE);
        BarRenderer renderer2 = (BarRenderer) plot.getRenderer();
        renderer2.setBarPainter(new StandardBarPainter());
        renderer2.setMaximumBarWidth(0.1);

        return chart;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {
        Map<String, Integer[]> data = new LinkedHashMap<>();
        data.put("Tag 2", new Integer[]{5, 30, 80});
        data.put("Tag 3", new Integer[]{10, 40, 70});
        data.put("Tag 4", new Integer[]{15, 50, 60});
        data.put("Full Tags", new Integer[]{20, 60, 60});

        final StackedBarChart demo = new StackedBarChart("Stacked Bar Chart Demo 4", data, null){};
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

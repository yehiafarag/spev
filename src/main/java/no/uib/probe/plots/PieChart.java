/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package no.uib.probe.plots;

import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author yfa041
 */
public class PieChart extends JFrame {

    public PieChart(String title) {
        super(title);
        setContentPane(createDemoPanel());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       
    }

  

    public PieChart(String title, Map<String, Integer> data) {
        super(title);
        PieDataset dataset = createDataset(data);
        setContentPane(createPanel(title,dataset));
    }

    private static PieDataset createDataset(Map<String, Integer> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (String key : data.keySet()) {
            dataset.setValue(key, data.get(key));
        }
        return dataset;
    }

    private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("IPhone 5s", 20);
        dataset.setValue("SamSung Grand", 20);
        dataset.setValue("MotoG", 40);
        dataset.setValue("Nokia Lumia", 10);
        return dataset;
    }

    private static JFreeChart createChart(String title,PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                title, // chart title 
                dataset, // data    
                true, // include legend   
                true,
                false);

        return chart;
    }
     public static JPanel createPanel(String title,PieDataset dataset) {
        JFreeChart chart = createChart(title,dataset);
        return new ChartPanel(chart);
    }


    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart("demo ",createDataset());
        return new ChartPanel(chart);
    }

    public static void main(String[] args) {
        PieChart demo = new PieChart("Mobile Sales");
        demo.setSize(560, 367);
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}

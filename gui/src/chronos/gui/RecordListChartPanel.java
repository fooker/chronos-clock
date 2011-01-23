package chronos.gui;

import chronos.Controller;
import chronos.Record;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AreaChartProperties;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.types.ChartType;

public class RecordListChartPanel extends JPanel {

  public static String TITLE = "Movement over Time";

  public static String AXIS_TITLE_X = "Time";

  public static String AXIS_TITLE_Y = "Movement";

  private ChartProperties chartProperties;

  private AxisProperties axisProperties;

  private DataSeries dataSeries;

  public RecordListChartPanel() {
    this.chartProperties = new ChartProperties();

    this.axisProperties = new AxisProperties();
    this.axisProperties.setXAxisLabelsAreVertical(true);
  }

  public void setRecords(List<Record> records) {
    if (records != null) {
      try {
        String[] titles = new String[records.size()];
        for (int i = 0; i < records.size(); i++) {
          titles[i] = Long.toString(records.get(i).getTimestamp());
        }

        DataSeries dataSeries = new DataSeries(titles, AXIS_TITLE_X, AXIS_TITLE_Y, TITLE);

        double[][] data = new double[1][records.size()];
        for (int i = 0; i < records.size(); i++) {
          data[0][i] = records.get(i).getMovement();
        }

        Paint[] paints = new Paint[]{new Color(153, 0, 255, 100)};

        Stroke[] strokes = {LineChartProperties.DEFAULT_LINE_STROKE};
        Shape[] shapes = {PointChartProperties.SHAPE_CIRCLE};
        LineChartProperties areaChartProperties = new LineChartProperties(strokes, shapes);
        
        AxisChartDataSet axisChartDataSet = new AxisChartDataSet(data, null, paints, ChartType.LINE, areaChartProperties);


        dataSeries.addIAxisPlotDataSet(axisChartDataSet);

        this.dataSeries = dataSeries;

      } catch (ChartDataException ex) {
        Controller.getInstance().log(Controller.LogLevel.ERROR, "Exception: %s", ex.getMessage());
        Logger.getLogger(RecordListChartPanel.class.getName()).log(Level.SEVERE, null, ex);
      }

    } else {
      this.dataSeries = null;
    }

    this.repaint();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    if (this.dataSeries != null) {
      try {
        AxisChart chart = new AxisChart(dataSeries, this.chartProperties, this.axisProperties, null, this.getWidth(), this.getHeight());
        chart.setGraphics2D((Graphics2D) g);
        chart.render();

      } catch (Exception ex) {
        Controller.getInstance().log(Controller.LogLevel.ERROR, "Exception: %s", ex.getMessage());
        Logger.getLogger(RecordListChartPanel.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}

package chronos.gui;

import chronos.Record;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RecordListTableModel extends AbstractTableModel {

  public final static String[] COLUMN_NAMES = new String[] {
    "",
    "Time",
    "Movement"
  };

  public final static Class<?>[] COLUMN_CLASSES = new Class<?>[] {
    Integer.class,
    Date.class,
    Integer.class
  };

  private List<Record> records;

  public RecordListTableModel() {
    this(new ArrayList<Record>());
  }

  public RecordListTableModel(List<Record> records) {
    this.records = records;
  }

  public List<Record> getRecords() {
    return this.records;
  }

  public void setRecords(List<Record> records) {
    this.records = records;

    this.fireTableDataChanged();
  }

  public int getRowCount() {
    return this.records.size();
  }

  public int getColumnCount() {
    return COLUMN_NAMES.length;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return COLUMN_NAMES[columnIndex];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return COLUMN_CLASSES[columnIndex];
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return rowIndex;

      case 1:
        return this.records.get(rowIndex).getTimestamp();

      case 2:
        return this.records.get(rowIndex).getMovement();

      default:
        return null;
    }
  }
}

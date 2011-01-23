package chronos.gui;

import chronos.Controller;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class DeviceComboBoxModel extends AbstractListModel implements ComboBoxModel {

  private String selectedIdentifier;

  public DeviceComboBoxModel() {
  }

  public void setSelectedItem(Object anItem) {
    this.selectedIdentifier = (String) anItem;
  }

  public Object getSelectedItem() {
    return this.selectedIdentifier;
  }

  public int getSize() {
    return Controller.getAvailablePortIdentifiers().size();
  }

  public Object getElementAt(int index) {
    return Controller.getAvailablePortIdentifiers().get(index);
  }
}

package chronos.gui;

import gnu.io.CommPortIdentifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class DeviceComboBoxModel extends AbstractListModel implements ComboBoxModel {

  private List<String> identifiers;

  private String selectedIdentifier;

  public DeviceComboBoxModel() {
    this.identifiers = new ArrayList<String>();
    
    Enumeration<CommPortIdentifier> e = CommPortIdentifier.getPortIdentifiers();
    while (e.hasMoreElements()) {
      CommPortIdentifier identifier = e.nextElement();
      if (identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        this.identifiers.add(identifier.getName());
      }
    }
  }

  public void setSelectedItem(Object anItem) {
    this.selectedIdentifier = (String) anItem;
  }

  public Object getSelectedItem() {
    return this.selectedIdentifier;
  }

  public int getSize() {
    return this.identifiers.size();
  }

  public Object getElementAt(int index) {
    return this.identifiers.get(index);
  }
}

package chronos.gui;

import chronos.Controller;

public class MainFrame extends javax.swing.JFrame implements Controller.Listener {

  private StringBuilder log = new StringBuilder();

  public MainFrame() {
    this.initComponents();

    Controller.getInstance().addListener(this);
  }

  public void synced() {
    final Controller controller = Controller.getInstance();

    this.syncLastTextField.setText(controller.getSyncLast().toString());
    this.syncDurationTextField.setText(Long.toString(controller.getSyncDuration()));
    this.syncPkgSendTextField.setText(Long.toString(controller.getSyncPkgSend()));
    this.syncPkgReceivedTextField.setText(Long.toString(controller.getSyncPkgReceived()));

    this.measurementStartTextField.setText(controller.getMeasurementStart().toString());
    this.measurementStopTextField.setText(controller.getMeasurementStop().toString());
    this.measurementDurationTextField.setText(Long.toString(controller.getMeasurementDuration()));
    this.measurementRecordsTextField.setText(Long.toString(controller.getMeasurementRecords()));
    this.measurementGapsTextField.setText(Long.toString(controller.getMeasurementGaps()));

    this.recordListTableModel.setRecords(controller.getRecords());

    this.recordListChartPanel.setRecords(controller.getRecords());
  }

  public void connectionChanged(boolean connected) {
    this.deviceComboBox.setEnabled(!connected);
    this.syncButton.setEnabled(connected);
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    recordListTableModel = new chronos.gui.RecordListTableModel();
    deviceComboBoxModel = new chronos.gui.DeviceComboBoxModel();
    jToolBar1 = new javax.swing.JToolBar();
    deviceComboBox = new javax.swing.JComboBox();
    connectToggleButton = new javax.swing.JToggleButton();
    jSeparator1 = new javax.swing.JToolBar.Separator();
    syncButton = new javax.swing.JButton();
    jSplitPane1 = new javax.swing.JSplitPane();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jPanel4 = new javax.swing.JPanel();
    syncLastTextField = new javax.swing.JTextField();
    jLabel7 = new javax.swing.JLabel();
    syncDurationTextField = new javax.swing.JTextField();
    jLabel8 = new javax.swing.JLabel();
    syncPkgSendTextField = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    syncPkgReceivedTextField = new javax.swing.JTextField();
    jLabel10 = new javax.swing.JLabel();
    jPanel5 = new javax.swing.JPanel();
    measurementStartTextField = new javax.swing.JTextField();
    measurementStopTextField = new javax.swing.JTextField();
    measurementDurationTextField = new javax.swing.JTextField();
    measurementRecordsTextField = new javax.swing.JTextField();
    measurementGapsTextField = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    recordListChartPanel = new chronos.gui.RecordListChartPanel();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jScrollPane1 = new javax.swing.JScrollPane();
    logTextPane = new javax.swing.JTextPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jToolBar1.setFloatable(false);

    deviceComboBox.setEditable(true);
    deviceComboBox.setModel(deviceComboBoxModel);
    jToolBar1.add(deviceComboBox);

    connectToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chronos/gui/icons/network-wired.png"))); // NOI18N
    connectToggleButton.setText("Connect");
    connectToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    connectToggleButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connectToggleButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(connectToggleButton);
    jToolBar1.add(jSeparator1);

    syncButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chronos/gui/icons/view-refresh.png"))); // NOI18N
    syncButton.setText("Sync");
    syncButton.setEnabled(false);
    syncButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    syncButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        syncButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(syncButton);

    getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

    jSplitPane1.setDividerLocation(250);
    jSplitPane1.setDividerSize(5);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    jPanel1.setLayout(new java.awt.GridLayout(1, 0));

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Sync"));

    syncLastTextField.setEditable(false);

    jLabel7.setText("Last Sync:");

    syncDurationTextField.setEditable(false);
    syncDurationTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

    jLabel8.setText("Duration:");

    syncPkgSendTextField.setEditable(false);
    syncPkgSendTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

    jLabel9.setText("Pkg. Send:");

    syncPkgReceivedTextField.setEditable(false);
    syncPkgReceivedTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

    jLabel10.setText("Pkg. Received:");

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel10)
          .addComponent(jLabel9)
          .addComponent(jLabel8)
          .addComponent(jLabel7))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(syncLastTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
          .addComponent(syncDurationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
          .addComponent(syncPkgSendTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
          .addComponent(syncPkgReceivedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(syncLastTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel7))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(syncDurationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel8))
        .addGap(18, 18, 18)
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(syncPkgSendTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel9))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(syncPkgReceivedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel10))
        .addContainerGap(91, Short.MAX_VALUE))
    );

    jPanel1.add(jPanel4);

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Measurement"));

    measurementStartTextField.setEditable(false);

    measurementStopTextField.setEditable(false);

    measurementDurationTextField.setEditable(false);
    measurementDurationTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

    measurementRecordsTextField.setEditable(false);
    measurementRecordsTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

    measurementGapsTextField.setEditable(false);
    measurementGapsTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

    jLabel2.setText("Start:");

    jLabel3.setText("Stop:");

    jLabel4.setText("Duration:");

    jLabel5.setText("Records:");

    jLabel6.setText("Gaps:");

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel6)
          .addComponent(jLabel5)
          .addComponent(jLabel4)
          .addComponent(jLabel3)
          .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(measurementStartTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
          .addComponent(measurementStopTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
          .addComponent(measurementDurationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
          .addComponent(measurementRecordsTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
          .addComponent(measurementGapsTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(measurementStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(measurementStopTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(measurementDurationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4))
        .addGap(18, 18, 18)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(measurementRecordsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(measurementGapsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6))
        .addContainerGap(66, Short.MAX_VALUE))
    );

    jPanel1.add(jPanel5);

    jTabbedPane1.addTab("Statistics", jPanel1);

    jPanel2.setLayout(new java.awt.BorderLayout());

    javax.swing.GroupLayout recordListChartPanelLayout = new javax.swing.GroupLayout(recordListChartPanel);
    recordListChartPanel.setLayout(recordListChartPanelLayout);
    recordListChartPanelLayout.setHorizontalGroup(
      recordListChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 548, Short.MAX_VALUE)
    );
    recordListChartPanelLayout.setVerticalGroup(
      recordListChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 222, Short.MAX_VALUE)
    );

    jPanel2.add(recordListChartPanel, java.awt.BorderLayout.CENTER);

    jTabbedPane1.addTab("Movement", jPanel2);

    jPanel3.setLayout(new java.awt.BorderLayout());

    jTable1.setAutoCreateRowSorter(true);
    jTable1.setModel(recordListTableModel);
    jScrollPane2.setViewportView(jTable1);

    jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

    jTabbedPane1.addTab("Raw", jPanel3);

    jSplitPane1.setLeftComponent(jTabbedPane1);

    logTextPane.setContentType("text/html");
    logTextPane.setEditable(false);
    jScrollPane1.setViewportView(logTextPane);

    jSplitPane1.setRightComponent(jScrollPane1);

    getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void connectToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToggleButtonActionPerformed
    Controller.getInstance().setDevice((String) this.deviceComboBox.getSelectedItem());
    
    Controller.getInstance().setConnected(this.connectToggleButton.isSelected());
    this.connectToggleButton.setSelected(Controller.getInstance().isConnected());
  }//GEN-LAST:event_connectToggleButtonActionPerformed

  private void syncButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncButtonActionPerformed
    Controller.getInstance().sync();
  }//GEN-LAST:event_syncButtonActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToggleButton connectToggleButton;
  private javax.swing.JComboBox deviceComboBox;
  private chronos.gui.DeviceComboBoxModel deviceComboBoxModel;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JToolBar.Separator jSeparator1;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JToolBar jToolBar1;
  private javax.swing.JTextPane logTextPane;
  private javax.swing.JTextField measurementDurationTextField;
  private javax.swing.JTextField measurementGapsTextField;
  private javax.swing.JTextField measurementRecordsTextField;
  private javax.swing.JTextField measurementStartTextField;
  private javax.swing.JTextField measurementStopTextField;
  private chronos.gui.RecordListChartPanel recordListChartPanel;
  private chronos.gui.RecordListTableModel recordListTableModel;
  private javax.swing.JButton syncButton;
  private javax.swing.JTextField syncDurationTextField;
  private javax.swing.JTextField syncLastTextField;
  private javax.swing.JTextField syncPkgReceivedTextField;
  private javax.swing.JTextField syncPkgSendTextField;
  // End of variables declaration//GEN-END:variables

  public void log(Controller.LogLevel level, String message) {

    this.log.append("<font color=\"");
    switch (level) {
      case ERROR:
        this.log.append("#FF0000");

      case WARNING:
        this.log.append("#FF8800");

      case INFO:
        this.log.append("#0000FF");

      case DEBUG:
        this.log.append("#888888");
    }
    this.log.append("\">");

    this.log.append("<b>");
    this.log.append(level.getText());
    this.log.append(":&nbsp;");
    this.log.append("</b>");
    this.log.append("</font>");

    this.log.append(message);
    this.log.append("<br />");

    StringBuilder doc = new StringBuilder();
    doc.append("<html><body><font face=\"monospace\">");
    doc.append(this.log);
    doc.append("</font></body></html>");

    this.logTextPane.setText(doc.toString());
  }
}

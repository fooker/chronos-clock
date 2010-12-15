package chronos;

import chronos.gui.MainFrame;

public class Main {

  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {

      public void run() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
      }
    });
  }
}

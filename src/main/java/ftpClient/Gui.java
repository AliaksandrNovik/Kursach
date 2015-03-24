package ftpClient;

import java.awt.Dimension;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Gui extends JFrame{
	public String[] createList(String currentName){
		
		return
	}
	public void createGUI(){
		JFrame frame = new JFrame("Test frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		
		  JPanel mainPanel = new JPanel();
          mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
 
          JList northList = new JList(data);
          northList.setLayoutOrientation(JList.VERTICAL);
          northList.setVisibleRowCount(0);
 
 
          JScrollPane southScroll = new JScrollPane(southList);
          southScroll.setPreferredSize(new Dimension(100, 100));
	}
	
}

package view;

import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FriendListView extends JFrame{

	
	String username = null;
	JScrollPane jScrollPane = new JScrollPane();
	JPanel jPanel = new JPanel();
	
	public static void main(String [] args) {
		FriendListView friendListView = new FriendListView("user.getRealname()");
    	friendListView.createFrame();
	}
	
	
	public FriendListView(String username) throws HeadlessException {
		super();
		this.username = username;
	}

	public void createFrame() {
		
		jPanel = new JPanel(new GridLayout(10,1,5,5));
		for(int i=0;i<10;i++) {
			ImageIcon imageIcon = new ImageIcon(FriendListView.class.getClassLoader().getResource("src/Image/user1.png"));
			JLabel jLabel = new JLabel("wang",imageIcon,JLabel.LEFT);
			jPanel.add(jLabel);
		}
		jScrollPane =new JScrollPane(jPanel);
		this.add(jScrollPane);
		this.setTitle(this.username+"的好友列表");
		this.setBounds(500,500,300,600);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

package view;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import entity.User;
import serivce.GetFriendByUsernameService;

public class FriendListView extends JFrame{

	
	String username = null;
	JScrollPane jScrollPane = new JScrollPane();
	JPanel jPanel = new JPanel();
		
	
	public FriendListView(String username) throws HeadlessException {
		this.username = username;
	}

	public void createFrame() {
		GetFriendByUsernameService getFriendByUsernameService= new GetFriendByUsernameService();
		List<User> userList= getFriendByUsernameService.getFriendByUsername(this.username);
		if(userList != null) {
			jPanel = new JPanel(new GridLayout(userList.size(),1,5,5));
			for(int i=0;i<userList.size();i++) {
				User user = userList.get(i);
				if(user.getUsername().equals(this.username)) {
					continue;
				}
				ImageIcon imageIcon = new ImageIcon(FriendListView.class.getClassLoader().getResource("src/"+user.getPhoto()));
				JLabel jLabel = new JLabel(user.getUsername(),imageIcon,JLabel.LEFT);
				jPanel.add(jLabel);
			}
			jScrollPane =new JScrollPane(jPanel);
			this.add(jScrollPane);
		}	
		this.setTitle(this.username+"的好友列表");
		this.setBounds(500,500,300,600);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

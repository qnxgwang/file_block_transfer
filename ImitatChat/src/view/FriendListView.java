package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.net.Socket;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class FriendListView extends JFrame{

	
	String username = null;
	JScrollPane jScrollPane = new JScrollPane();
	JPanel jPanel = new JPanel();
	public ChatView tempChatView = null;	
	List<User> userList;
	public JLabel[] jLabel;
	
	public FriendListView(String username,List<User> userList) throws HeadlessException {
		this.username = username;
		this.userList = userList;
	}

	public void flush() {
		
	}
	public void createFrame() {
		/**
		 * 对在线好友进行标记
		 */
		/**
		 * 对好友发来的信息进行标记
		 */
		/**
		 * 将好友发来的信息写入聊天记录
		 */
		if(userList != null) {
			jPanel = new JPanel(new GridLayout(userList.size(),1,5,5));
			jLabel = new JLabel[userList.size()];
			for(int i=0;i<userList.size();i++) {
				User user = userList.get(i);
				if(user.getUsername().equals(this.username)) {
					jLabel[i] = new JLabel();
					continue;
				}				
				ImageIcon imageIcon = new ImageIcon(FriendListView.class.getClassLoader().getResource("src/"+user.getPhoto()));
                jLabel[i] = new JLabel(user.getUsername(),imageIcon,JLabel.LEFT);
				jLabel[i].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2) {
							JLabel label = (JLabel)(e.getSource());
							label.setForeground(Color.black);
							ChatView chatView = new ChatView(username,((JLabel)e.getSource()).getText());
							chatView.createFrame();
							if(tempChatView==null) {
								tempChatView = chatView;
							}
							else {
								tempChatView.dispose();
								tempChatView = chatView;
							}
						}
					}				
				});
				jPanel.add(jLabel[i]);
			}
			jScrollPane =new JScrollPane(jPanel);
			this.add(jScrollPane);
		}	
		this.setTitle(this.username+"的好友列表");
		this.setBounds(500,500,300,600);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				super.windowClosed(e);
				try {
					Socket socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());					
					User user = new User();
					user.setUsername(username);
					Message requestMessage = new Message();
					requestMessage.setMessageType(MessageType.LOGIN_EXIT);
					requestMessage.setUser(user);
					SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				
			}
		});
	}
	
	
}

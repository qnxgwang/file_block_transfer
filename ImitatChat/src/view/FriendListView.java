package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import entity.ChatContent;
import entity.Message;
import entity.User;
import serivce.ImageUtil;
import util.FileUtil;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class FriendListView extends JFrame{

	
	String username = null;
	
	JPanel jPanel = new JPanel();
	JScrollPane jScrollPane = new JScrollPane();
	
	public List<User> userList = null;
	public ChatView tempChatView = null;	
	public JLabel[] jLabel;
	public Map<String, JLabel> jLabelMap;
	
	public FriendListView(String username,List<User> userList) throws HeadlessException {
		this.username = username;
		this.userList = userList;
	}
	/**
	 * 对标签进行设置
	 * @param jLabel
	 * @return
	 */
	public JLabel[] setJLabel(JLabel[] jLabel) {
		for(int i=0;i<userList.size();i++) {
			User user = userList.get(i);	
			ImageIcon imageIcon = null;
			if(user.isOnlineFlag()) {
				imageIcon = new ImageIcon(FriendListView.class.getClassLoader().getResource("src/Image/"+user.getPhoto()));
			}else {
				imageIcon = new ImageIcon(FriendListView.class.getClassLoader().getResource("src/Image/gray"+user.getPhoto()));
			}		
            jLabel[i] = new JLabel(user.getUsername(),imageIcon,JLabel.LEFT);
            if(user.getContentList() != null) {
            	jLabel[i].setForeground(Color.red);
            	for(ChatContent content: user.getContentList()) {
            		try {
						FileUtil.writeFriendMessageToFile(username,content.getTimeStamp(), content.getFrom(), content.getContent());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
            	}         	
            }
			jLabel[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2) {
						JLabel label = (JLabel)(e.getSource());
						label.setForeground(Color.black);
						ChatView chatView = new ChatView(username,((JLabel)e.getSource()).getText());
						chatView.createFrame();
						if(tempChatView == null) {
							tempChatView = chatView;
						}
						else {
							tempChatView.dispose();
							tempChatView = chatView;
						}
					}
				}				
			});
		}
		return jLabel;
	}

	/**
	 * 创建面板
	 */
	public void createFrame() {
		if(userList != null) {
			jPanel = new JPanel(new GridLayout(userList.size(),1,5,5));
			jLabel = new JLabel[userList.size()];		
			jLabelMap = new HashMap<String, JLabel>();
			userList = sort(userList);
			jLabel = setJLabel(jLabel);
			for(int i=0;i<jLabel.length;i++) {
				jPanel.add(jLabel[i]);
				jLabelMap.put(jLabel[i].getText(), jLabel[i]);
			}		
			jScrollPane =new JScrollPane(jPanel);
			this.add(jScrollPane);
		}	
		this.setTitle(this.username+"的好友列表");
		this.setBounds(500,500,300,600);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {//用户退出登录，并告知服务器
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				super.windowClosed(e);
				tempChatView.dispose();
				try {
					Socket socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());					
					User user = new User();
					user.setUsername(username);
					Message requestMessage = new Message();
					requestMessage.setMessageType(MessageType.EXIT);
					requestMessage.setUser(user);
					SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				
			}
		});
	}
	/**
	 * 多关键字排序
	 * @param u1
	 * @param u2
	 * @return
	 */
	
	public boolean cmp(User u1,User u2) {
		
		if(u1.isOnlineFlag() && !u2.isOnlineFlag())return true;
		if(!u1.isOnlineFlag() && u2.isOnlineFlag())return false;
		if(u1.getContentList() == null) return false;
		return true;
		
	}
	/**
	 * 简单排序，复杂度log(n)
	 * @param userList
	 * @return
	 */
	public List<User> sort(List<User> userList){		
		List<User> list = new ArrayList<User>();
		for(int i=0;i<userList.size();i++) {
			int first  = 0;
			if(userList.get(i).isOnlineFlag()) {
				first += 2;//好友处于在线状态
			}
			if(userList.get(i).getContentList() != null) {
				first += 1;//好友存在离线信息
			}
			userList.get(i).setId(first);
		}
		for(int i=3;i>=0;i--) {
			for(int j=0;j<userList.size();j++) {
				if(userList.get(j).getId() == i) {
					list.add(userList.get(j));
				}
			}
		}
		return list;	
	}
}

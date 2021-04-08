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
import java.util.Comparator;
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
import util.FileUtil;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class FriendListView extends JFrame{

	
	private String username = null;
	
	private JPanel jPanel = null;
	
	private JScrollPane jScrollPane = null;
	
	private List<User> userList = null;//好友序列
	
	private ChatView tempChatView = null;//每个用户可以创建一个用于与好友聊天的面板实例	
	
	private JLabel[] jLabel;

	public FriendListView(String username,List<User> userList) throws HeadlessException {
		this.username = username;
		this.userList = userList;
		initPanel(userList);
	}

	public ChatView getTempChatView() {
		return tempChatView;
	}

	public JLabel[] getjLabel() {
		return jLabel;
	}
	
	public List<User> getUserList() {
		return userList;
	}

	public void createFrame() {		
		this.add(jScrollPane);
		this.setTitle(this.username+"的好友列表");
		this.setBounds(500,500,300,600);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new MyWindowAdapter()); //用户退出登录，并告知服务器		
	}
	
	public void initPanel(List<User> userList){
		if(userList != null) {
			jPanel = new JPanel(new GridLayout(userList.size(),1,5,5));
			jLabel = new JLabel[userList.size()];		
			userList = sort(userList);//对好友序列进行排序
			jLabel = setJLabel(jLabel);//初始化标签序列
			for(int i=0; i<jLabel.length; i++) {
				jPanel.add(jLabel[i]);
			}
			jScrollPane =new JScrollPane(jPanel);//可滚动窗口			
		}
	}
	
    private JLabel[] setJLabel(JLabel[] jLabel) {
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
						FileUtil.writeFriendMessageToDB(username,content.getTimeStamp(), content.getMessageFrom(), content.getContent());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
            	}         	
            }
			jLabel[i].addMouseListener(new LabelMouseAdapter());
		}
		return jLabel;
	}
    
    /**
     * 窗口适配器
     * @author 14005
     *
     */
    class MyWindowAdapter extends WindowAdapter{
		@Override
		public void windowClosed(java.awt.event.WindowEvent e) {
			super.windowClosed(e);
			if(tempChatView != null) {
				tempChatView.dispose();
			}		
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
	}
    
    /**
     * 列表适配器
     * @author 14005
     *
     */
    class LabelMouseAdapter extends MouseAdapter{
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
    }
	
	/**
	 * 使用stream进行降序排序
	 * @param userList
	 * @return
	 */
	public List<User> sort(List<User> userList){		
		List<User> list = new ArrayList<User>();
		for(int i=0;i<userList.size();i++) {
			int first  = 0;
			if(userList.get(i).isOnlineFlag()) {
				first += 2;
			}
			if(userList.get(i).getContentList() != null) {
				first += 1;
			}
			userList.get(i).setId(first);
		}
		list.stream().sorted(Comparator.comparing(User::getId).reversed());
		return list;	
	}
}

package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.User;
import handle.DataThread;
import serivce.LoginService;

public class LoginView extends JFrame{
	
	/**
	 * µÇÂ¼´°¿ÚµÄÊôÐÔ
	 */
	private static final long serialVersionUID = 1L;

	protected JLabel photoJLabel = null;
	
	protected JPanel northJPanel = null;
	
	protected JLabel usernameJLabel = null;
	
	protected JTextField usernameJTextField = null;
	
	protected JLabel pwdJLabel = null;
	
	protected JPasswordField pwdJPasswordFiled = null;
	
	protected JPanel centerJPanel =null;
	
	protected JButton loginJButton = null;
	
	protected JButton registerJButton = null;
	
	protected JPanel southJPanel =null;
	
	/**
	 * ³ÌÐòÈë¿Ú
	 * @param args
	 */
	public static void main(String [] args) {
		
		LoginView loginView= new LoginView();
		loginView.createFrame();
		
	}

	/**
	 * ´´½¨µÇÂ¼´°¿Ú
	 */
	public void createFrame() {
		ImageIcon imageIcon = new ImageIcon(LoginView.class.getClassLoader().getResource("src/Image/qq.png"));
		photoJLabel = new JLabel(imageIcon,JLabel.CENTER);
		northJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northJPanel.add(photoJLabel);
		this.add(northJPanel,BorderLayout.NORTH);
		
		usernameJLabel = new JLabel("ÕËºÅ");
		usernameJTextField = new JTextField(25);	
		pwdJLabel = new JLabel("ÃÜÂë");
		pwdJPasswordFiled = new JPasswordField(25);
		centerJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerJPanel.add(usernameJLabel);
		centerJPanel.add(usernameJTextField);
		centerJPanel.add(pwdJLabel);
		centerJPanel.add(pwdJPasswordFiled);
		this.add(centerJPanel,BorderLayout.CENTER);
		
		loginJButton = new JButton("µÇÂ¼");
		loginJButton.addMouseListener(new LoginMouseAdapter());
		registerJButton = new JButton("×¢²á");
		registerJButton.addMouseListener(new RegisterMouseAdapter());

		southJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southJPanel.add(loginJButton);
		southJPanel.add(registerJButton);
		this.add(southJPanel,BorderLayout.SOUTH);
		
		this.setTitle("WeChat");
		this.setBounds(500,500,300,400);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * ×¢²á°´Å¥ÊÊÅäÆ÷
	 * @author 14005
	 *
	 */
	class RegisterMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			LoginView.this.dispose();
        	RegisterView registerView = new RegisterView();
        	registerView.createFrame();
		}
	}
	
	/**
	 * µÇÂ¼°´Å¥ÊÊÅäÆ÷
	 * @author 14005
	 *
	 */
	class LoginMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			User user = new User();
			user.setUsername(usernameJTextField.getText());
			user.setPwd(pwdJPasswordFiled.getText());
			LoginService loginService = new LoginService();
            if(loginService.login(user)) {              	
            	LoginView.this.dispose();                	
            	FriendListView friendListView = new FriendListView(user.getUsername(),loginService.getUserList());
            	friendListView.createFrame();
            	DataThread dataThread = new DataThread(loginService.getDataSocket(),friendListView);
            	dataThread.start();
            	loginService.sendLoginMessageToFriend(usernameJTextField.getText());
            }
            else {
            	JOptionPane.showMessageDialog(LoginView.this, "ÕËºÅ»òÃÜÂë´íÎó");
            }
		}
	}
}

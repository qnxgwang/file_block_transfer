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

import Thread.DataThread;
import entity.User;
import serivce.LoginService;

public class LoginView extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static void main(String [] args) {
		LoginView loginView= new LoginView();
		loginView.createFrame();
	}


	JLabel photoJLabel = null;
	JPanel northJPanel = null;
	
	JLabel usernameJLabel = null;
	JTextField usernameJTextField = null;
	JLabel pwdJLabel = null;
	JPasswordField pwdJPasswordFiled = null;
	JPanel centerJPanel =null;
	
	JButton loginJButton = null;
	JButton registerJButton = null;
	JPanel southJPanel =null;
	
	public void createFrame() {
		ImageIcon imageIcon = new ImageIcon(LoginView.class.getClassLoader().getResource("src/Image/qq.png"));
		photoJLabel = new JLabel(imageIcon,JLabel.CENTER);
		northJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		northJPanel.add(photoJLabel);
		this.add(northJPanel,BorderLayout.NORTH);
		
		usernameJLabel = new JLabel("’À∫≈");
		usernameJTextField = new JTextField(25);	
		pwdJLabel = new JLabel("√‹¬Î");
		pwdJPasswordFiled = new JPasswordField(25);
		centerJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerJPanel.add(usernameJLabel);
		centerJPanel.add(usernameJTextField);
		centerJPanel.add(pwdJLabel);
		centerJPanel.add(pwdJPasswordFiled);
		this.add(centerJPanel,BorderLayout.CENTER);
		
		loginJButton = new JButton("µ«¬º");
		loginJButton.addMouseListener(new MouseAdapter() {
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
                }
                else {
                	JOptionPane.showMessageDialog(LoginView.this, "’À∫≈ªÚ√‹¬Î¥ÌŒÛ");
                }
			}
			
		});
		registerJButton = new JButton("◊¢≤·");
		registerJButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginView.this.dispose();
            	RegisterView registerView = new RegisterView();
            	registerView.createFrame();
			}
			
		});
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
}

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.User;
import serivce.RegisterService;

public class RegisterView extends JFrame{
	
	/**
	 * 注册窗口的属性
	 */
	private JLabel usernameJlabel = null;
	
	private JTextField usernameJTextField = null;
	
	private JLabel realnameJlabel = null;
	
	private JTextField realnameJTextField = null;
	
	private JLabel pwdJlabel = null;
	
	private JPasswordField pwdJPasswordField = null;
	
	private JLabel confirmJlabel = null;
	
	private JPasswordField confirmJPasswordField = null;
	
	private JLabel photoJlabel = null;
	
	private JComboBox<String> photoJComboBox = null;
	
	private JPanel centerJPanel = null;
	
	private JButton submitJButton = null;
	
	private JButton resetJButton = null;
	
	private JButton backJButton = null;
	
	private JPanel southJPanel = null;
	
	/**
	 * 创建注册窗口
	 */
	public void createFrame() {
		usernameJlabel = new JLabel("用户账号");
		usernameJTextField = new JTextField(20);
		realnameJlabel = new JLabel("用户姓名");
		realnameJTextField = new JTextField(20);
		photoJlabel = new JLabel("用户头像");
        photoJComboBox=new JComboBox<String>();    
        photoJComboBox.addItem("--请选择--");    
        photoJComboBox.addItem("user1.png");
        photoJComboBox.addItem("user2.png");
        photoJComboBox.addItem("user3.png");
        photoJComboBox.setPreferredSize(new Dimension(200,20));
		pwdJlabel = new JLabel("用户密码");
		pwdJPasswordField = new JPasswordField(20);
		confirmJlabel = new JLabel("确认密码");
		confirmJPasswordField = new JPasswordField(20);
		centerJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerJPanel.add(usernameJlabel);
		centerJPanel.add(usernameJTextField);
		centerJPanel.add(realnameJlabel);
		centerJPanel.add(realnameJTextField);
		centerJPanel.add(photoJlabel);
		centerJPanel.add(photoJComboBox);
		centerJPanel.add(pwdJlabel);
		centerJPanel.add(pwdJPasswordField);
		centerJPanel.add(confirmJlabel);
		centerJPanel.add(confirmJPasswordField);
		this.add(centerJPanel,BorderLayout.CENTER);
			
		submitJButton = new JButton("注册");
		submitJButton.addMouseListener(new SubmitMouseAdapter()); 

		resetJButton = new JButton("重置密码");
		resetJButton.addMouseListener(new ResetMouseAdapter());
		
		backJButton = new JButton("返回");
		backJButton.addMouseListener(new BackMouseAdapter());
	
		southJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southJPanel.add(submitJButton);		
		southJPanel.add(backJButton);	
		southJPanel.add(resetJButton);
		this.add(southJPanel,BorderLayout.SOUTH);
		
		this.setTitle("用户注册");
		this.setBounds(500,500,300,300);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 按钮适配器
	 * @author 14005
	 *
	 */
	class ResetMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			pwdJPasswordField.setText("");
			confirmJPasswordField.setText("");
		}
	}
	
	/**
	 * 按钮适配器
	 * @author 14005
	 *
	 */
	class BackMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			RegisterView.this.dispose();
            LoginView back = new LoginView();
            back.createFrame();
		}
	}
	
	/**
	 * 按钮适配器
	 * @author 14005
	 *
	 */
	class SubmitMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			String pwdText = pwdJPasswordField.getText();
			String confirmText = confirmJPasswordField.getText();
			System.out.println(pwdText);
			if(pwdText.equals(confirmText)) {
				User user = new User();
				user.setUsername(usernameJTextField.getText());
				user.setRealname(realnameJTextField.getText());
				user.setPhoto(photoJComboBox.getSelectedItem().toString());
				user.setPwd(pwdJPasswordField.getText());
				RegisterService registerService = new RegisterService();
				boolean registerflag = registerService.register(user);
				if(registerflag) {
					JOptionPane.showMessageDialog(RegisterView.this, "注册成功");
				}
				else {
					JOptionPane.showMessageDialog(RegisterView.this, "用户名已经被注册，请更换用户名");
				}
			}
			else {
				JOptionPane.showMessageDialog(RegisterView.this, "两次输入密码不一致");
			}
		}
	}
	
}

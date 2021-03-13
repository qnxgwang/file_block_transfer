package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import serivce.SendService;
import util.FileUtil;


public class ChatView extends JFrame{
	
	private String myUsername = null;
	public String friendUsername = null;
	public JTextPane recordPanel;
	
	private ImageIcon headim_image = new ImageIcon("cuit1.jpg");
	private ImageIcon camera_image = new ImageIcon("camera.png");
	private ImageIcon camera2_image = new ImageIcon("camera2.png");
	private ImageIcon type_image = new ImageIcon("type.png");
	private ImageIcon submit_image = new ImageIcon("submit.png");

	
	public ChatView(String myUsername,String friendUsername) {
		this.myUsername = myUsername;
		this.friendUsername = friendUsername;
	}
	public static void main(String [] args) {
		ChatView v = new ChatView(null,null);
				v.createFrame();
	}
	public void createFrame() {
		this.setTitle("Chat-Room:"+" "+this.myUsername+" "+"to"+" "+this.friendUsername);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setSize((int)(getWindowWidth()*0.5), (int)(getWindowWidth()*0.4));
		this.setLocationRelativeTo(null);	
		
		JLabel label =new JLabel(headim_image);
        label.setBounds(0, 0,(int)(this.getWidth()*0.2),(int)(this.getHeight()*0.25));
        this.add(label);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds((int)(this.getWidth()*0.2),(int)(this.getHeight()*0.8),(int)(this.getWidth()*0.8),(int)(this.getHeight()*0.2));
        this.add(scrollPane_1);                
        JTextArea textArea = new JTextArea();
        textArea.setBorder(null);
        textArea.setLineWrap(true);
        scrollPane_1.setViewportView(textArea);
		
        JScrollPane scrollPane_2 = new JScrollPane();	
		scrollPane_2.setBounds((int)(this.getWidth()*0.2),0,(int)(this.getWidth()*0.8),(int)(this.getHeight()*0.7));
		this.add(scrollPane_2);
		recordPanel = new JTextPane();
		recordPanel.setBackground(new Color(202, 235, 216));
		recordPanel.setEditable(false);	
		scrollPane_2.setViewportView(recordPanel);
		FileUtil.readMessageFromFile(recordPanel,myUsername,friendUsername);
		
        JButton send = new JButton(submit_image);
		send.setBounds((int)(this.getWidth()*0.8),(int)(this.getHeight()*0.7),(int)(this.getWidth()*0.15),(int)(this.getHeight()*0.1));
        send.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SendService sendService = new SendService();
				sendService.sendMessage(myUsername, friendUsername, textArea.getText());
				Document docs = recordPanel.getDocument();//获得文本对象
			    try {
			    	AttributeSet attributeSet=new javax.swing.text.SimpleAttributeSet();
			        docs.insertString(docs.getLength(),new Date().toString()+" MINE "+textArea.getText()+'\n',attributeSet);//对文本进行追加			    
			        FileUtil.writeMyMessageToFile(myUsername, new Date().toString(), friendUsername, textArea.getText());
			    } catch (Exception e1) {
			        e1.printStackTrace();
			    }			    
			}       	
        });
		this.add(send);
			
		this.setVisible(true);
	}
	/**
	 * 返回显示器宽度
	 * @return
	 */
	public int getWindowWidth() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		return width;		
	}
	/**
	 * 返回显示器高度
	 * @return
	 */
	public int getWindowHeight() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)screensize.getHeight();
		return height;
	}
	
	
}

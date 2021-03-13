package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;



public class ServerUI {

	private ImageIcon headim_image = new ImageIcon("cuit1.jpg");
	private ImageIcon camera_image = new ImageIcon("camera.png");
	private ImageIcon camera2_image = new ImageIcon("camera2.png");
	private ImageIcon type_image = new ImageIcon("type.png");
	private ImageIcon submit_image = new ImageIcon("submit.png");
	
	public static void main(String[] args) throws IOException {
		new ServerUI().windowOpen();		
	}
	
	public void windowOpen() throws IOException {
		JFrame window = new JFrame("we-chat");
		window.setDefaultCloseOperation(3);
		window.setResizable(false);
		window.setLayout(null);
		window.setSize(1000, 800);
		window.setLocationRelativeTo(null);
				
		//输入框
		JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(200,600,800,200);
        window.add(scrollPane_1);                
        JTextArea textArea = new JTextArea();
        textArea.setBorder(null);
        textArea.setLineWrap(true);
        scrollPane_1.setViewportView(textArea);
	    		
        //头像
        
        
        //IP地址库
		JPanel listpanel = new JPanel();
		listpanel.setBackground(new Color(137, 207, 240));
		listpanel.setBounds(0, 200, 200, 1000);
		listpanel.setLayout(null);
		JTextField field = new JTextField();
		field.setBounds(0, 0, 100, 50);
		listpanel.add(field);	
		window.add(listpanel);	
				
		//聊天记录			
		
		//相机
		JButton camera = new JButton(camera_image);
		camera.setBounds(200,550,50,50);
		camera.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
								
				camera.setEnabled(false);
				JFrame camera_window = new JFrame("video");			
				camera_window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				camera_window.setResizable(false);
				camera_window.setLayout(null);
				camera_window.setSize(400, 800);
				camera_window.setLocation(window.getX()+1000, window.getY());
				camera_window.setVisible(true);
	            
				JPanel video1_panel = new JPanel();
				video1_panel.setBackground(new Color(137, 207, 240));
				video1_panel.setBounds(0,0,400,400);
				JLabel la = new JLabel(camera2_image);
				video1_panel.add(la);
				camera_window.add(video1_panel);
				
				JPanel video2_panel = new JPanel();
				video2_panel.setBackground(new Color(137, 207, 240));
				video2_panel.setBounds(0,400,400,400);
				camera_window.add(video2_panel);
				
				/*Webcam webcam = Webcam.getDefault();
				webcam.setViewSize(WebcamResolution.VGA.getSize());
				webcam.open();
				WebcamPanel webcampanel = new WebcamPanel(webcam);
				video2_panel.add(webcampanel);
				exitListener wl = new exitListener(camera,webcam);
				camera_window.addWindowListener(wl);*/
			}
			
		});
		window.add(camera);
		
		JButton text_color_button = new JButton();
		text_color_button.setBackground(Color.black);
		text_color_button.setBounds(800,550,50,50);
		text_color_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                JFrame color_choose_jf=new JFrame("Font-color");
                color_choose_jf.setDefaultCloseOperation(2);
                color_choose_jf.setSize(100,140);
                
                JButton jb=(JButton)e.getSource();
                Point p=jb.getLocationOnScreen();
                double x=p.getX();
                double y=p.getY();
                color_choose_jf.setLocation((int)(x),(int)(y)-140);
                color_choose_jf.setLayout(new FlowLayout(FlowLayout.CENTER));
                color_choose_jf.setResizable(false);
                Color[] colors=new Color[24];
                for(int i=0;i<=23;i++) {
                	colors[i]=new Color(1);
                }
                colors[0]=Color.black;
                colors[1]=Color.blue;
                colors[2]=Color.cyan;
                colors[3]=Color.DARK_GRAY;
                colors[4]=Color.gray;
                colors[5]=Color.green;
                colors[6]=Color.LIGHT_GRAY;
                colors[7]=Color.magenta;
                colors[8]=Color.orange;
                colors[9]=Color.pink;
                colors[10]=Color.red;
                colors[11]=Color.white;
                colors[12]=Color.yellow;
                JButton[] color_button=new JButton[24];
                for(int i=0;i<=23;i++) {
                	color_button[i]=new JButton();
                	color_button[i].addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
						     Color color=((JButton)e.getSource()).getBackground();
						     text_color_button.setBackground(color);
						}
                		
                	});
                	color_button[i].setBackground(colors[i]);
                	color_button[i].setPreferredSize(new Dimension(20,20));
                	color_choose_jf.add(color_button[i]);
                }
                color_choose_jf.setVisible(true);         
			}		
		});
		window.add(text_color_button);		
		JButton font_button = new JButton(type_image);
		font_button.setBounds(850,550,50,50);
		window.add(font_button);
		
		window.setVisible(true);
	}
}

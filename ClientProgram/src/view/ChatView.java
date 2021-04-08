package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

import serivce.FileService;
import serivce.SendService;
import util.FileUtil;


public class ChatView extends JFrame{
	
	private String myUsername = null;
	
	private String friendUsername = null;
	
	private JScrollPane filePane = null;
	
	private JPanel filePanel = null;
	
	private JLabel[] fileLabel = null;

	private JTextPane recordPanel;
	
	private JTextArea textArea = null;
	
	private boolean closed = false;
	
	private ImageIcon headim_image = new ImageIcon("cuit1.jpg");
	
	private ImageIcon submit_image = new ImageIcon("submit.png");
	
	public ChatView(String myUsername,String friendUsername) {
		this.myUsername = myUsername;
		this.friendUsername = friendUsername;
	}
	
    public String getFriendUsername() {
		return friendUsername;
	}

	public JTextPane getRecordPanel() {
		return recordPanel;
	}
	
	public boolean isClosed() {
		return closed;
	}

	public void initFilePane() {
    	filePanel = new JPanel(new GridLayout(10,1,5,5));//默认10个文件长度 		
    	List<String> fileList = null;
		try {
			fileList = FileUtil.readFileName(myUsername, friendUsername);		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	fileLabel = new JLabel[fileList.size()];
    	for(int i=0; i<fileLabel.length; i++) {
    		fileLabel[i] = new JLabel(fileList.get(i));
    		fileLabel[i].addMouseListener(new MyFileMouseAdapter()); 
    		filePanel.add(fileLabel[i]);
    	}
    	filePane =new JScrollPane(filePanel);//可滚动窗口	
    }
	
	/**
	 * 创建窗口
	 */
	public void createFrame() {
		this.setTitle("Chat-Room:"+" "+this.myUsername+" "+"to"+" "+this.friendUsername);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setSize((int)(getWindowWidth()*0.5), (int)(getWindowWidth()*0.4));
		this.setLocationRelativeTo(null);	
		
		initFilePane();
		filePane.setBounds(0,(int)(this.getHeight()*0.25),(int)(this.getWidth()*0.2),(int)(this.getHeight()*0.75));
		this.add(filePane);
		
		JLabel label =new JLabel(headim_image);
        label.setBounds(0, 0,(int)(this.getWidth()*0.2),(int)(this.getHeight()*0.25));
        this.add(label);
        
        JScrollPane editJScrollPane = new JScrollPane();
        editJScrollPane.setBounds((int)(this.getWidth()*0.2),(int)(this.getHeight()*0.8),(int)(this.getWidth()*0.8),(int)(this.getHeight()*0.2));
        this.add(editJScrollPane);                
        textArea = new JTextArea();
        textArea.setBorder(null);
        textArea.setLineWrap(true);
        editJScrollPane.setViewportView(textArea);
        textArea.setTransferHandler(new MyTransferHandler());
        
        JScrollPane recordJScrollPane = new JScrollPane();	
        recordJScrollPane.setBounds((int)(this.getWidth()*0.2),0,(int)(this.getWidth()*0.8),(int)(this.getHeight()*0.7));
		this.add(recordJScrollPane);
		recordPanel = new JTextPane();
		recordPanel.setBackground(new Color(202, 235, 216));
		recordPanel.setEditable(false);	
		recordJScrollPane.setViewportView(recordPanel);
		FileUtil.readMessageFromFile(recordPanel,myUsername,friendUsername);
		
        JButton send = new JButton(submit_image);
		send.setBounds((int)(this.getWidth()*0.8),(int)(this.getHeight()*0.7),(int)(this.getWidth()*0.15),(int)(this.getHeight()*0.1));
        send.addMouseListener(new MyButtonMouseAdapter()); 
        this.add(send);		
        
        this.addWindowListener(new MyWindowAdapter());		
		this.setVisible(true);
	}
	
	/**
	 * 文件下载适配器
	 * @author 14005
	 *
	 */
	class MyFileMouseAdapter extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount()==2) {
				JLabel label = (JLabel)(e.getSource());
				String filename = label.getText();
				FileService.requestFile(myUsername, friendUsername, filename);
			}
		}		
	}
	
	/**
	 * 发送按钮适配器
	 * @author 14005
	 *
	 */
	class MyButtonMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			SendService.sendMessage(myUsername, friendUsername, textArea.getText());
			Document docs = recordPanel.getDocument();//获得文本对象
		    try {
		    	AttributeSet attributeSet=new javax.swing.text.SimpleAttributeSet();
		        docs.insertString(docs.getLength(),new Date().toString()+" MINE "+textArea.getText()+'\n',attributeSet);//对文本进行追加			    
		        FileUtil.writeMyMessageToDB(myUsername, new Date().toString(), friendUsername, textArea.getText());
		    } catch (Exception e1) {
		        e1.printStackTrace();
		    }			    
		}       
	}
	
	/**
	 * 窗口关闭适配器
	 * @author 14005
	 *
	 */
	class MyWindowAdapter extends WindowAdapter{
		@Override
		public void windowClosed(java.awt.event.WindowEvent e) {
			super.windowClosed(e);
			closed = true;//JFrame关闭后，设置closed关键字为true，用于FriendListView进行判断
		}
	}
	
	/**
	 * 拖动上传框适配器
	 * @author 14005
	 *
	 */
	class MyTransferHandler extends TransferHandler{
		private static final long serialVersionUID = 1L;
        @Override
        public boolean importData(JComponent comp, Transferable t) {
            try {
                Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                String filepath = o.toString();
                if (filepath.startsWith("[")) {
                    filepath = filepath.substring(1);
                }
                if (filepath.endsWith("]")) {
                    filepath = filepath.substring(0, filepath.length() - 1);
                }
                File file = new File(filepath);
                if(file.length()==0)return true;
                FileService.sendMessage(myUsername, friendUsername, file.getName(), filepath);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }	
        @Override
        public boolean canImport(JComponent comp, DataFlavor[] flavors) {
            for (int i = 0; i < flavors.length; i++) {
                if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                    return true;
                }
            }
            return false;
        }
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

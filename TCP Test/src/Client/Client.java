package Client;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextMeasurer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;

class Client extends JFrame implements ActionListener {
	JTextField txtMessage;
	JTextArea textPane; //��ʾ
	JButton btnSend;
	JButton btnNewButton;
	private JTextField txtServerIpAddress;
	private JTextField txtPort;
	private JLabel label_1;
	private JTextField textFieldName;
	
	//����ȫ�ֱ���
	private static String ClientName;//�ͻ�������
	InetAddress IP = null; //������IP
	int port;//�������˿�
	private static boolean isSendMsg = false; //�жϿͻ����Ƿ���������
	Socket Send;
	public Client() throws Exception{
		getContentPane().setLayout(null);
		
		//this.getContentPane().setBackground(Color.white); //����JFrame������ɫ
		
		this.setTitle("����ͻ���");
		this.setSize(720, 550);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if(JOptionPane.showConfirmDialog(null, "<html><font size = 3>ȷ���˳���</html>","ϵͳ��ʾ",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE) == 0) {
							System.exit(0);
					}else {
						return;
					}
				}
			});
		
		
		txtMessage = new JTextField();
		txtMessage.setText("Message");
		txtMessage.setBounds(144, 22, 353, 35);
		getContentPane().add(txtMessage);
		txtMessage.setColumns(10);
		
		textPane = new JTextArea();
		textPane.setLineWrap(true);
		textPane.setBounds(35, 177, 636, 319);
		getContentPane().add(textPane);
		
//		JScrollPane AddDownMenu = new JScrollPane();
//		AddDownMenu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //����ˮƽ�������Զ�����
//		AddDownMenu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); //���ô�ֱ�������Զ�����
//		AddDownMenu.setBounds(36, 152, 636, 250);
//		AddDownMenu.setVisible(true);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(518, 21, 153, 37);
		btnSend.setEnabled(false);
		btnSend.setBackground(Color.white);
		getContentPane().add(btnSend);
		
		txtServerIpAddress = new JTextField();
		txtServerIpAddress.setText("localhost");
		txtServerIpAddress.setBounds(144, 73, 153, 35);
		getContentPane().add(txtServerIpAddress);
		txtServerIpAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("7788");
		txtPort.setBounds(431, 78, 66, 35);
		getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		btnNewButton = new JButton("Connect");
		btnNewButton.setBounds(518, 126, 153, 37);
		btnNewButton.setBackground(Color.white);
		getContentPane().add(btnNewButton);
		
		JLabel lblServerPort = new JLabel("Server Port��");
		lblServerPort.setFont(new Font("����", Font.PLAIN, 17));
		lblServerPort.setBounds(307, 72, 116, 36);
		getContentPane().add(lblServerPort);
		
		JLabel lblServerIp = new JLabel("Server IP\uFF1A");
		lblServerIp.setFont(new Font("����", Font.PLAIN, 17));
		lblServerIp.setBounds(35, 71, 98, 36);
		getContentPane().add(lblServerIp);
		
		JLabel label = new JLabel("����:");
		label.setFont(new Font("����", Font.PLAIN, 17));
		label.setBounds(49, 19, 98, 36);
		getContentPane().add(label);
		
		label_1 = new JLabel("�û���:");
		label_1.setFont(new Font("����", Font.PLAIN, 17));
		label_1.setBounds(49, 129, 98, 36);
		getContentPane().add(label_1);
		
		textFieldName = new JTextField();
		textFieldName.setText("");
		textFieldName.setColumns(10);
		textFieldName.setBounds(144, 129, 353, 35);
		getContentPane().add(textFieldName);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 702, 401);
		
		
//		getContentPane().add(panel);
		btnNewButton.addActionListener(this);
		btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				String StringMessage = txtMessage.getText();
				
				// TODO Auto-generated method stub
				try {
					OutputStream Msg = Send.getOutputStream();//����������
					String SendMessage = ClientName+":"+StringMessage; //�ڷ��͵���Ϣͷ�����Ͽͻ����û���
					Msg.write(SendMessage.getBytes()); //���������������
					isSendMsg = true; //�����ж��Ƿ����Լ�������һ����Ϣ
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		this.setVisible(true);
		
	}
	
	
	public static void main(String[] args) throws SocketException  {
		
		try {
			Client client = new Client();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnNewButton) {
					if(textFieldName.getText().trim().equals("")) {
						textPane.append("�������û�����\n");
					}else {
						this.setTitle("�ͻ��ˣ�"+textFieldName.getText());
						//���ó�ʼ������򲻿ɱ༭
						textFieldName.setEnabled(false);
						txtPort.setEnabled(false);
						txtServerIpAddress.setEnabled(false);
						
						ClientName = textFieldName.getText(); //����ͻ������õ��û���
						try {
							//��ȡ������IP��ַ
							IP = InetAddress.getByName(txtServerIpAddress.getText());
							//��ȡ�������˿�
							port = Integer.valueOf(txtPort.getText());
							
							Send = new Socket(IP, port); //�����������������
							textPane.append(" �ѻ�ȡIP ��Port ������������ӳɹ���\n");
							btnSend.setEnabled(true);//��������ɹ����Ӻ����Send��ť
							btnNewButton.setEnabled(false); //ʹConnect��ť�����ٴε���
						}catch(UnknownHostException e1) {
							textPane.append("��������ʧ�ܣ����������Ƿ�������\n");
							System.out.println(e1.getMessage());
							textPane.append("\n"+e1.getMessage());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							textPane.append("��������ʧ�ܣ����������Ƿ�������");
							System.out.println(e1.getMessage());
							textPane.append("\n"+e1.getMessage());
						} 
						
						new Thread(new Runnable() {
							/*
							 * (non-Javadoc)
							 * @see java.lang.Runnable#run()
							 * �����߳����ڽ�����Ϣ
							 */
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								
										try {
											System.out.println("���ڵȴ����շ�����������Ϣ!");
											
											InputStream GetMsg = Send.getInputStream();
											byte[] buf =new byte[4096];
											int len = GetMsg.read(buf);
										
											while(len != -1) {
												
												if(isSendMsg) {
													textPane.append("\n "+"����" + txtMessage.getText());
													isSendMsg = false;
												}else {
													textPane.append("\n "+new String(buf,0,len));
												}
												
												
												len = GetMsg.read(buf);
												
											}
											
										GetMsg.close();//�ر���
										}catch(IOException e) {
											e.getStackTrace();
										}
							}
					}).start();
					}
			}
		}
	}




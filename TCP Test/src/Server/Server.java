package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class Socks{
	/*
	 * ������������
	 * @lixuehui
	 */
	private volatile static String MainMsg;//�����û��ĵ�ǰ������Ϣ������ת�����������û�
	private volatile static boolean isChange; //�����ж�������Ϣ�Ƿ����
	private volatile static int ThreadMax = 0; //���ط����߳���
	private volatile static int NowThread = 0 ;//Ŀǰ�ѿ����߳���
	private volatile static int NowSendPort;//��ǰMainMsg�洢��Ϣ�ķ��Ͷ˿�
	public Socks() throws Exception{
		ServerSocket DS;
		DS= new ServerSocket(7788);//����һ���˿�Ϊ7788 ��Socket �Ķ���
		
		//ʹ����ѭ�����տͻ��˷��͵�����
		while(true) {
			//����DS ��accept()�ķ�������������
			System.out.println("�����������У����ڵȴ�����");
			Socket Server = DS.accept(); //�ȴ�����
			ThreadMax+=1;
			
			String Message = null; //�����û����͵���Ϣ��ʱ�洢���������̵߳���
			Thread thread = new Thread(()->{ 
			//�˴��¿��̴߳�����ͻ��˵���������
				try {
					int port = Server.getPort();//��ȡ�ͻ��˵����Ӷ˿�
					
					while(true) {
						System.out.println("��˿ںţ�"+port+"���û��������ӳɹ���");
						
						InputStream GetData = Server.getInputStream();//��ȡ�ͻ��˷��͵�����
						byte[] GetMsg= new byte[4096];
						
						if(NowThread < ThreadMax) {
							Thread ReSend = new Thread(()->{
								int size = GetMsg.length-1;
								//�¿��̷߳��ؿͻ�������
								NowThread +=1;
									while(true) {	
										if(isChange) {
											try {
												System.out.println("���������߳�");
												isChange = false;
												OutputStream Data = Server.getOutputStream();
												//String SendPort = "�˿�"+String.valueOf(NowSendPort)+":";//���û�����Ϣ��Ŀ��ͻ��˶˿�
												//String PortMsg = SendPort + MainMsg ;
												String PortMsg = "[�˿�"+port+"] "+MainMsg; //������Ϣ���Ͽͻ��˶˿ں�
												Data.write(PortMsg.getBytes());
												
												System.out.println("���ͳɹ�");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											//��ʼ���������ݵ�Byte����
											while(size != -1) { 
												GetMsg[size] = (byte)0;
												size--;
											}
											size = GetMsg.length-1;
											System.out.println("Ŀǰ�������û���"+ThreadMax+" �����߳�����"+NowThread);
										}
										
									}
									
							});
						
							ReSend.start(); //ִ���߳�
							//ReSend.sleep(10000000);
							System.out.println("Ŀǰ�������û���"+ThreadMax+" �����߳�����"+NowThread);
						}else {
							System.out.println("Ŀǰ�������û���"+ThreadMax+" �����߳�����"+NowThread+"�Ѵ��������߳�����");
						}
						
						
						int len = GetData.read(GetMsg);//�����л�ȡ���ݲ������GetMsg �����У��������ݳ��ȣ�
						while(len != -1) {
							System.out.println("���յ�"+port+"�˿ڷ��͵ģ�"+new String(GetMsg,0,len));
							NowSendPort = port;//�洢��ǰ�̵߳Ŀͻ��˶˿�
							MainMsg = new String(GetMsg,0,len); //�����յ���Ϣ�����ڹ���������
							isChange = true;//����״̬
							
						
							len = GetData.read(GetMsg);
						}
						
						
					}
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
					System.out.println("�Ͽ����ӣ�");
					try {
						Server.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ThreadMax -=1;
					
				}
				
		});
			
			thread.start();//ִ���̣߳�
	}
	
	}
}



public class Server {
	public static void main(String[] args) {
		try {
			Socks so = new Socks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

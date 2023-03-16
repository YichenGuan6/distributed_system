package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class createwhiteboard {
	private ArrayList<Socket>sockets=new ArrayList<>();
	private ArrayList<DataInputStream>inputs=new ArrayList<>();
	private ArrayList<DataOutputStream>outputs=new ArrayList<>();
	private static String username = "manager";
	private static String password = "12345";
	public static  String state = "";
	public static void main(String[]arguments) throws IOException{
		
		String first_create = JOptionPane.showInputDialog("type manager username and password the structure is username@password");
		String[] check_inf = first_create.split("@");
		if(username.equals(check_inf[0]) && password.equals(check_inf[1]))
		{
			state = "open";
			createwhiteboard sev=new createwhiteboard();
			sev.create();
			
		}
		else
		{
			JOptionPane.showInputDialog("wrong wrong see u!");
			System.exit(0);
		}
		
	}
	
	public void create() throws IOException
	{
		ServerSocket whiteboard_server=new ServerSocket(6666);
		
		System.out.println("server is open");
		while("open".equals(state)){
			
			System.out.println("new  mebber!!!!");
			Socket whiteboard_client = whiteboard_server.accept();
			//#################change##################################
			DataInputStream input = new DataInputStream(whiteboard_client.getInputStream());
			DataOutputStream output = new DataOutputStream(whiteboard_client.getOutputStream());
			
			String receive_request = input.readUTF();
			String[] check_request = receive_request.split("@");
			System.out.println("check input before if loop"+receive_request);
			
//			String new_user = JOptionPane.showInputDialog("new user"+check_request[1]+" wants to connect");
//			
			if("manager".equals(check_request[1]))
			{
				output.writeUTF("request@server@yes");
				output.flush();
				sockets.add(whiteboard_client);
				inputs.add(input);
				outputs.add(output);
				System.out.println("manager id ;"+(sockets.size())+" is connected!");
				System.out.println("get manager "+(sockets.size())+"'s IOflow");
				System.out.println("current output list has "+outputs.size());
				System.out.println("current output scoket##### has "+sockets.size());
				MessageChannel channel= new MessageChannel(sockets.size()-1, input, outputs);
				channel.start();
			}
			if("manager".equals(check_request[1])==false &&sockets.size()==0)
			{
				output.writeUTF("request@server@contact");
				whiteboard_client.close();
				input.close();
				output.close();	
			}
			if("manager".equals(check_request[1])==false && sockets.size()>0 )
			{
				
				outputs.get(0).writeUTF(receive_request);
				System.out.println("send request from server in create function ");
				String c =inputs.get(0).readUTF();
				System.out.println("check c"+c);
				String[] c_list = c.split("@");
				output.writeUTF(c);
				System.out.println("The respond from manager : "+ c);
				if("yes".equals(c_list[2]))
				{
					System.out.println("get client "+(sockets.size())+"'s IOflow");
					System.out.println("current output list has "+outputs.size());
					sockets.add(whiteboard_client);
					inputs.add(input);
					outputs.add(output);
					MessageChannel channel= new MessageChannel(sockets.size()-1, input, outputs);
					channel.start();
				}
				else
				{
					//output.writeUTF("no");
					whiteboard_client.close();
					input.close();
					output.close();	
				}
				
			}
		}
	}
	
	class MessageChannel extends Thread
	{
		private DataInputStream  input;
		private ArrayList<DataOutputStream> l_outputs;
		private int socketNo=0;
		
		public MessageChannel(int socketNo1,DataInputStream a_input,ArrayList<DataOutputStream>l_output)
		{
			this.input=a_input;
			this.l_outputs=l_output;
			this.socketNo=socketNo1;
			System.out.println("this is client "+socketNo);
		}
		
		public void run()
		{
			while(true)
			{
				try {
					if(input.available() > 0) 
					{
					String rec_data ="";
					try {
						System.out.println("receive message from user id: "+socketNo);
						rec_data = input.readUTF();
						System.out.println("server message channel function "+rec_data);
						send(rec_data);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void send(String sever_message)
		{
			try {
			
			for(int i = 0;i<l_outputs.size();i++)
			{
				if(i==socketNo)
				{
					continue;
				}
				else
				{
				System.out.println("check in send function"+sever_message);
				l_outputs.get(i).writeUTF(sever_message);
				}
			}
			}catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	

}

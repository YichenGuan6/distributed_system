package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class joinwhiteboard {
	
	private String username ="";
	private static JPanel draw_board;
	private JTextArea text_out;
	private JTextArea text_in;
	private String tool = "line";
	private String color = "black";
	public static Graphics g;
	private int x1,y1, x2,y2;
	DataInputStream input;
	DataOutputStream output;
	
	//private Socket socket;
	
	
	
	private HashMap <String,Color> colormap = new HashMap<>();
	
	public static void main(String[]arguments) throws IOException
	{
		
		joinwhiteboard abc = new joinwhiteboard();
		abc.start();
		System.out.println("---check the situation-");

	}
	
	public void start() throws IOException
	{
		
        String user_name = JOptionPane.showInputDialog("the sever ip and port are  setting default just type u name below");
        
        
        if("manager".equals(user_name))
        {
        	Socket new_socket=new Socket("localhost",6666);
        	username = "manager";
        	System.out.println(username+" is connecnt");
    		input = new DataInputStream(new_socket.getInputStream());
    		output = new DataOutputStream(new_socket.getOutputStream());
    		String user_request = "request@"+username;
    		output.writeUTF(user_request);
    		output.flush();
    		String respond = input.readUTF();
    		String[] check_res = respond.split("@");
    		
			if("yes".equals(check_res[2]))
			{
	    		receive_information rec = new receive_information();
	    		rec.start();
	    		manager_board();
	    		System.out.println("open manager gui");  
			}
	
        }
        //user login
        else
        {
        	
        	Socket new_socket=new Socket("localhost",6666);
        	username = user_name;
        	System.out.println(username+" try to connecnt");
    		input = new DataInputStream(new_socket.getInputStream());
    		output = new DataOutputStream(new_socket.getOutputStream());
    		System.out.println(username+" try to connecnt2 ");
    		String user_request = "request@"+username;
    		output.writeUTF(user_request);
    		output.flush();
    		System.out.println(username+" try to connecnt3 ");
    		String respond = input.readUTF();
    		System.out.println(respond.length());
    		String[] check_res = respond.split("@");
    		System.out.println(username+" is connecnt");
    		
			if ("no".equals(check_res[2]))
			{
				JOptionPane.showInputDialog("Admin do not allow u to use white board");
				new_socket.close();
				input.close();
				output.close();
				System.exit(0);
			}
			if("contact".equals(check_res[2]))
			{
				JOptionPane.showInputDialog(" the manager is not here try again later");
				new_socket.close();
				input.close();
				output.close();
				System.exit(0);
			}
			if("yes".equals(check_res[2]))
			{
	    		receive_information rec = new receive_information();
	    		rec.start();
	    		client_board();
	    		System.out.println("open manager gui");  
			}
			
			else
			{
				JOptionPane.showInputDialog("some thing wrong");
				new_socket.close();
				input.close();
				output.close();
				System.exit(0);
			} 
        	
        }
		
		
	}
	
	private void client_board()
	{
		//add color
		Color navy = new Color(0,0,128);
		Color teal = new Color(0,128,128);
		Color purple = new Color(128,0,128);
		Color lime = new Color(46,139,87);
		Color maroon = new Color(128,0,0);
		Color megenta = new Color(255,0,255);
		Color silver = new Color(192,192,192);
		Color olive = new Color(128,128,0);
		
		
		colormap.put("red", Color.red);
		colormap.put("yellow", Color.yellow);
		colormap.put("blue", Color.blue);
		colormap.put("green", Color.green);
		colormap.put("black", Color.black);
		colormap.put("white", Color.white);
		colormap.put("cyan", Color.cyan);
		colormap.put("gray", Color.gray);
		colormap.put("navy", navy);
		colormap.put("teal", teal);
		colormap.put("purple", purple);
		colormap.put("lime", lime);
		colormap.put("maroon", maroon);
		colormap.put("megenta", megenta);
		colormap.put("silver", silver);
		colormap.put("olive", olive);
		JFrame white_board=new JFrame();
		white_board.setTitle("CLIENT-----Whiteboard "+username);
		white_board.setSize(600,480);
		white_board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		white_board.setLocationRelativeTo(null);
		white_board.setLayout(new BorderLayout());
		
		//tool bar gui!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		JPanel tool_bar=new JPanel();
		tool_bar.setLayout(new FlowLayout(FlowLayout.LEFT));
		tool_bar.setBackground(Color.RED);
		
		JPanel tool = new JPanel();
		tool.setBackground(Color.WHITE);
		tool.setPreferredSize(new Dimension(80,450));
		//add tool
		String [] tool_name = {"line","circle","oval","rectangle","pencile","text"};
		JButton[]tool_list=new JButton[tool_name.length];
		for(int i=0;i<tool_name.length;i++){
			tool_list[i]=new JButton(tool_name[i]);
			tool_list[i].setPreferredSize(new Dimension(65,30));
			tool.add(tool_list[i]);
			}
		Color[]colors={Color.red,Color.yellow,Color.blue,Color.green,Color.black,Color.white,Color.cyan,Color.gray,navy,
				teal,purple,lime,maroon,megenta,silver,olive};
		String [] color_name = {"red","yellow","blue","green","black","white","cyan","gray","navy","teal",
				"purple","lime","maroon","megenta","silver","olive"};
		JButton[]color_list=new JButton[color_name.length];
		for(int i=0;i<color_name.length;i++){
			color_list[i]=new JButton();
			color_list[i].setActionCommand(color_name[i]);
			color_list[i].setBackground(colors[i]);
			tool.add(color_list[i]);
		}
		
		tool_bar.add(tool);
		
		
		//drawing area
		//JPanel draw_panel=new JPanel();
		//draw_panel.setBackground(Color.LIGHT_GRAY);
		draw_board = new JPanel();
		draw_board.setBackground(Color.WHITE);
		draw_board.setPreferredSize(new Dimension(320,450));
		//draw_panel.add(draw_board);
		
		
		//message area
		
		JPanel message_panel=new JPanel();
		message_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		message_panel.setBackground(Color.GREEN);
		
		
		JPanel message=new JPanel();
		message.setBackground(Color.WHITE);
		message.setPreferredSize(new Dimension(150,450));
		
		JLabel message_label=new JLabel("message---output");
		message.add(message_label);
		text_out = new JTextArea(10,12);
		text_out.setLineWrap(true);
		text_out.setWrapStyleWord(true);
		text_out.append("test");
		text_out.setEditable(false);
		JScrollPane jsp1=new JScrollPane(text_out,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		message.add(jsp1);
		
		
		JLabel message_label2=new JLabel("message---input");
		message.add(message_label2);
		text_in = new JTextArea(10,12);
		text_in.setLineWrap(true);
		text_in.setWrapStyleWord(true);
		JScrollPane jsp2=new JScrollPane(text_in,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		message.add(jsp2);
		
		JButton send_button=new JButton("send");
		message.add(send_button);
		message_panel.add(message);
		
		white_board.add(draw_board,BorderLayout.CENTER);
		white_board.add(message_panel,BorderLayout.EAST);
		white_board.add(tool_bar,BorderLayout.WEST);
		white_board.setVisible(true);
		
		
		
		//listener
		Listener lis=new Listener(text_out,text_in);
		send_button.addActionListener(lis);
		for(int i=0;i<tool_name.length;i++){
			tool_list[i].addActionListener(lis);
		}
		for(int i=0;i<color_name.length;i++){
			color_list[i].addActionListener(lis);
		}
		
		draw_board.addMouseListener(lis);
		draw_board.addMouseMotionListener(lis);
		
		g=draw_board.getGraphics();
		System.out.println("---finish gui-");

		
	}
	private void manager_board()
	{
		//add color
		Color navy = new Color(0,0,128);
		Color teal = new Color(0,128,128);
		Color purple = new Color(128,0,128);
		Color lime = new Color(46,139,87);
		Color maroon = new Color(128,0,0);
		Color megenta = new Color(255,0,255);
		Color silver = new Color(192,192,192);
		Color olive = new Color(128,128,0);
		
		
		colormap.put("red", Color.red);
		colormap.put("yellow", Color.yellow);
		colormap.put("blue", Color.blue);
		colormap.put("green", Color.green);
		colormap.put("black", Color.black);
		colormap.put("white", Color.white);
		colormap.put("cyan", Color.cyan);
		colormap.put("gray", Color.gray);
		colormap.put("navy", navy);
		colormap.put("teal", teal);
		colormap.put("purple", purple);
		colormap.put("lime", lime);
		colormap.put("maroon", maroon);
		colormap.put("megenta", megenta);
		colormap.put("silver", silver);
		colormap.put("olive", olive);
		JFrame white_board=new JFrame();
		white_board.setTitle("CLIENT-----Whiteboard "+username);
		white_board.setSize(600,480);
		white_board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		white_board.setLocationRelativeTo(null);
		white_board.setLayout(new BorderLayout());
		
		//tool bar gui!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		JPanel tool_bar=new JPanel();
		tool_bar.setLayout(new FlowLayout(FlowLayout.LEFT));
		tool_bar.setBackground(Color.RED);
		
		JPanel tool = new JPanel();
		tool.setBackground(Color.WHITE);
		tool.setPreferredSize(new Dimension(80,450));
		//add tool
		String [] tool_name = {"line","circle","oval","rectangle","pencile","text","save","close","kickout"};
		JButton[]tool_list=new JButton[tool_name.length];
		for(int i=0;i<tool_name.length;i++){
			tool_list[i]=new JButton(tool_name[i]);
			tool_list[i].setPreferredSize(new Dimension(65,30));
			tool.add(tool_list[i]);
			}
		Color[]colors={Color.red,Color.yellow,Color.blue,Color.green,Color.black,Color.white,Color.cyan,Color.gray,navy,
				teal,purple,lime,maroon,megenta,silver,olive};
		String [] color_name = {"red","yellow","blue","green","black","white","cyan","gray","navy","teal",
				"purple","lime","maroon","megenta","silver","olive"};
		JButton[]color_list=new JButton[color_name.length];
		for(int i=0;i<color_name.length;i++){
			color_list[i]=new JButton();
			color_list[i].setActionCommand(color_name[i]);
			color_list[i].setBackground(colors[i]);
			tool.add(color_list[i]);
		}
		
		tool_bar.add(tool);
		
		
		//drawing area
		//JPanel draw_panel=new JPanel();
		//draw_panel.setBackground(Color.LIGHT_GRAY);
		draw_board = new JPanel();
		draw_board.setBackground(Color.WHITE);
		draw_board.setPreferredSize(new Dimension(320,450));
		//draw_panel.add(draw_board);
		
		
		//message area
		
		JPanel message_panel=new JPanel();
		message_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		message_panel.setBackground(Color.GREEN);
		
		
		JPanel message=new JPanel();
		message.setBackground(Color.WHITE);
		message.setPreferredSize(new Dimension(150,450));
		
		JLabel message_label=new JLabel("message---output");
		message.add(message_label);
		text_out = new JTextArea(10,12);
		text_out.setLineWrap(true);
		text_out.setWrapStyleWord(true);
		text_out.append("test");
		text_out.setEditable(false);
		JScrollPane jsp1=new JScrollPane(text_out,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		message.add(jsp1);
		
		
		JLabel message_label2=new JLabel("message---input");
		message.add(message_label2);
		text_in = new JTextArea(10,12);
		text_in.setLineWrap(true);
		text_in.setWrapStyleWord(true);
		JScrollPane jsp2=new JScrollPane(text_in,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		message.add(jsp2);
		
		JButton send_button=new JButton("send");
		message.add(send_button);
		message_panel.add(message);
		
		white_board.add(draw_board,BorderLayout.CENTER);
		white_board.add(message_panel,BorderLayout.EAST);
		white_board.add(tool_bar,BorderLayout.WEST);
		white_board.setVisible(true);
		
		
		
		//listener
		Listener lis=new Listener(text_out,text_in);
		send_button.addActionListener(lis);
		for(int i=0;i<tool_name.length;i++){
			tool_list[i].addActionListener(lis);
		}
		for(int i=0;i<color_name.length;i++){
			color_list[i].addActionListener(lis);
		}
		
		draw_board.addMouseListener(lis);
		draw_board.addMouseMotionListener(lis);
		
		g=draw_board.getGraphics();
		System.out.println("---finish gui-");

		
	}

		
	
	class Listener implements ActionListener,MouseListener,MouseMotionListener
	{
		private JTextArea j_up_message;
		private JTextArea j_down_message;
		
		public Listener(JTextArea jta1,JTextArea jta2){
			this.j_up_message=jta1;
			this.j_down_message=jta2;
		}
		
		public void actionPerformed(ActionEvent e) {
			String tool_name=e.getActionCommand();
			if("line".equals(tool_name))
			{
				tool = tool_name;	
				System.out.println("current tool "+tool);
			}
			if("circle".equals(tool_name))
			{
				tool = tool_name;	
				System.out.println("current tool "+tool);
			}
			if("oval".equals(tool_name))
			{
				tool = tool_name;	
				System.out.println("current tool "+tool);
			}
			if("rectangle".equals(tool_name))
			{
				tool = tool_name;	
				System.out.println("current tool "+tool);
			}
			if("text".equals(tool_name))
			{
				tool = tool_name;	
				System.out.println("current tool "+tool_name);
			}
			if("pencile".equals(tool_name))
			{
				tool = tool_name;	
				System.out.println("current tool "+tool_name);
			}
			if("red".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current tool"+tool_name);
			}
			if("yellow".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("blue".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("green".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current tool"+tool_name);
			}
			if("black".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current tool"+tool_name);
			}
			if("white".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("cyan".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current tool"+tool_name);
			}
			if("gray".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current tool"+tool_name);
			}
			if("navy".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("teal".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("purple".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("lime".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			
			if("maroon".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("megenta".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("silver".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			if("olive".equals(tool_name))
			{
				color = tool_name;
				System.out.println("current color"+tool_name);
			}
			
			if("send".equals(tool_name))
			{
				String message_send = j_down_message.getText();
				String full_message = "message@"+username+"@"+message_send;
				
				j_up_message.append("local message:"+message_send);
				j_up_message.setCaretPosition(j_up_message.getText().length());
				try {
					output.writeUTF(full_message);
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				j_down_message.setText(" ");
				
				System.out.println("send button click"+tool_name + message_send);
			}
			if("save".equals(tool_name))
			{
				BufferedImage bufferedImage = new BufferedImage(draw_board.getWidth(), draw_board.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = bufferedImage.createGraphics();
				draw_board.paintAll(g2d);
				try {
					if(ImageIO.write(bufferedImage, "png", new File("./output_image.png")))
					{
						System.out.println("-- saved");
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
			}
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			g.setColor(colormap.get(color));
			if ("pencile".equals(tool)) 
			{
				System.out.println("current tool"+tool);
				x2 = x1;
				y2 = y1;
				x1 = e.getX();
				y1 = e.getY();
				g.setColor(colormap.get(color));
				g.drawLine(x1,y1,x2,y2);
				String send_data_pencile ="shapeinf@"+username+"@"+"pencile@"+x1+"@"+y1+"@"+x2+"@"+y2+"@"+color ;
				System.out.println(send_data_pencile);
				//sendinformation(send_data_pencile);
				try {
					output.writeUTF(send_data_pencile);
					output.flush();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			x1 = e.getX();
			y1 = e.getY();
			System.out.println("x is "+x1+" Y is "+ y1);
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			x2 = e.getX();
			y2 = e.getY();
			switch(tool){
			case"line" :
				g.setColor(colormap.get(color));
				g.drawLine(x1,y1,x2,y2);
				
				String send_data_line ="shapeinf@"+username+"@"+"line@"+x1+"@"+y1+"@"+x2+"@"+y2+"@"+color ;
				System.out.println("line is drawing x is "+x1+" Y is "+ y1);
				System.out.println("********************* "+send_data_line);
				try {
					output.writeUTF(send_data_line);
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
			case"circle":
				g.setColor(colormap.get(color));
				g.drawOval(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x2-x1),Math.abs(x2-x1));
				int cx =Math.min(x1, x2);
				int cb =Math.min(y1, y2);
				int cr =Math.abs(x2-x1);
				String send_data_circle ="shapeinf@"+username+"@"+"circle@"+cx+"@"+cb+"@"+cr+"@"+cr+"@"+color;
				System.out.println("circle is drawing x is "+cx+" Y is "+cb+"radius"+cr + "color"+color);
				System.out.println(send_data_circle);
				try {
					output.writeUTF(send_data_circle);
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
			case"oval":
				g.setColor(colormap.get(color));
				g.drawOval(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x2-x1),Math.abs(y2-y1));
				int ox =Math.min(x1, x2);
				int ob =Math.min(y1, y2);
				int or1 =Math.abs(x2-x1);
				int or2 =Math.abs(y2-y1);
				String send_data_oval ="shapeinf@"+username+"@"+"oval@"+ox+"@"+ob+"@"+or1+"@"+or2+"@"+color;
				System.out.println("oval is drawing x is "+x1+" Y is "+ y1);
				System.out.println(send_data_oval);
				try {
					output.writeUTF(send_data_oval);
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
			case"rectangle":
				g.setColor(colormap.get(color));
				g.drawRect(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x2-x1),Math.abs(y2-y1));
				int rx =Math.min(x1, x2);
				int rb =Math.min(y1, y2);
				int rr1 =Math.abs(x2-x1);
				int rr2 =Math.abs(y2-y1);
				String send_data_rectangle ="shapeinf@"+username+"@"+"rectangle@"+rx+"@"+rb+"@"+rr1+"@"+rr2+"@"+color;
				System.out.println("rectangle is drawing x is "+x1+" Y is "+ y1);
				System.out.println(send_data_rectangle);
				//sendinformation(send_data_rectangle);
				try {
					output.writeUTF(send_data_rectangle);
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
			case"text":
				String a =JOptionPane.showInputDialog("input");
				if(a != null)
				{
					
					g.drawString(a,x1,y1);
					String send_data_text ="textdata@"+username+"@"+a+"@"+x1+"@"+y1+"@"+color;
					System.out.println("send text data"+send_data_text);
					try {
						output.writeUTF(send_data_text);
						output.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					a = "";
				}
				else 
				{
					System.out.println("no input");
				}
				break;
				
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
//		public void sendinformation(String message)
//		{ 
//			try {
//				output.writeUTF(message);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		
//		}	
	}
	
	class receive_information extends Thread
	{
		public void run(){
			
			while(true)
			{
				try {
					if(input.available() > 0) 
					{
						String rec_message = "";
						try {
							rec_message = input.readUTF();
							System.out.println("in the receive_information function "+rec_message);
							read_inf(rec_message);
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
		
		private void read_inf(String data)
		{
			String type ="";
			String sender ="";
			String shape = "";
			String[] operation = data.split("@");
			System.out.println("in the read_inf function the receive data"+ data);
			type = operation[0];
			sender = operation[1];
			if  ("shapeinf".equals(type))
			{
				shape = operation[2];
				int x = Integer.parseInt(operation[3]);
				int y = Integer.parseInt(operation[4]);
				int xx = Integer.parseInt(operation[5]);
				int yy = Integer.parseInt(operation[6]);
				String rec_col = operation[7];
				switch(shape)
				{
				case"line" :
					g.setColor(colormap.get(rec_col));
					g.drawLine(x,y,xx,yy);
					System.out.println("in the read_inf function the receive data line");
					break;
					
				case"circle":
					g.setColor(colormap.get(rec_col));
					g.drawOval(x,y,xx,yy);
					System.out.println("in the read_inf function the receive data cirle");
					break;
				case"oval":
					g.setColor(colormap.get(rec_col));
					g.drawOval(x,y,xx,yy);
					System.out.println("in the read_inf function the receive data oval");
					break;
					
				case"rectangle":
					g.setColor(colormap.get(rec_col));
					g.drawRect(x,y,xx,yy);
					System.out.println("in the read_inf function the receive data rec");
					break;
					
				case"pencile":
					g.setColor(colormap.get(rec_col));
					g.drawLine(x,y,xx,yy);
					System.out.println("in the read_inf function the receive data pencile");
					break;
			}
			}
			if("textdata".equals(type))
			{
				int text_x = Integer.parseInt(operation[3]);
				int text_y = Integer.parseInt(operation[4]);
				String text_inf = operation[2];
			    String text_col = operation[5];
			    g.setColor(colormap.get(text_col));
			    g.drawString(text_inf,text_x,text_y);
			    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");			
			}
			if("message".equals(type))
			{
				String rec_message = operation[2];
				text_out.append("from user "+ sender + ": "+rec_message+'\n');
				text_out.setCaretPosition(text_out.getText().length());
				
				System.out.println("in the read_inf function the receive data message");
			}
			if("request".equals(type) && username.equals("manager"))
			{
				String conf = JOptionPane.showInputDialog("user "+ operation[1]+"wants to connect!");
				try {
					String respond = "request@"+username+"@"+conf;
					output.writeUTF(respond);
					System.out.println("read_ inf request check");
					output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
}

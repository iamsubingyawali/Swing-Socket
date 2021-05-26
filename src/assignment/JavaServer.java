package assignment;

//importing the required libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 * Program:		Java Server
 * Filename:	JavaServer.java
 * @author 		Subin Gyawali
 * Course:		BEng(Hons) Computing (Computer Networks Engineering)
 * Module:		CSY2026 - Modern Networks 
 * Tutor:		Khagendra Shah
 * @version:	2.5
 * Date:		15-Dec-2019
 */

public class JavaServer extends JFrame implements ActionListener,KeyListener{
	
	// Defining Port Address Field to enter Port number
	protected JTextField jTFieldPortNum;
	// Defining Combo box to choose protocol for communication i.e. TCP or UDP
	private JComboBox<String> jCBoxProtocol;
	// Defining button to open the specified port/open the connection
	private JButton jButtonOpen;
	// Defining button to end the connection/close the port
	private JButton jButtonClose;
	// Defining text area to type the message to be sent to the server
	protected JTextArea jTAreaMessage;
	// Defining send button to send the message
	private JButton jButtonSend;
	// Defining JPanel to indicate the status of the connection 
	// (Green for connected and red for disconnected)
	private JPanel jPanelStatus;
	// Defining text area to display the chat messages
	protected JTextArea jTAreaChat;
	// Defining a flag variable to start and stop the connection with the help of while loop
	protected boolean flag;
	// Defining thread to run the communication code in a separate thread
	protected static Thread serverThread;
	// Declaring an object of class TCPServer
	protected static TCPServer tcpServer;
	//Declaring an object of class UDPServer
	protected static UDPServer udpServer;
	
	// main METHOD TO START PROGRAM EXECUTION STARTS
	public static void main(String[] args) {
		// Creating an object of JavaServer class
		JavaServer serverGui = new JavaServer();
		// Calling the prepareGUI() method of class JavaServer
		serverGui.prepareGUI();
		// Setting the swing window with components visible
		serverGui.setVisible(true);
		// Initializing objects of the class TCPServer
		tcpServer = new TCPServer(serverGui);
		// Initializing objects of the class UDPserver 
		udpServer = new UDPServer(serverGui);
	}	
	// main METHOD ENDS

	
	// METHOD prepareGUI() TO DESIGN GUI STARTS
	public void prepareGUI(){
		// Setting the specifications for the JFrame GUI window
		setSize(600, 900);
		setLocation(1020, 70);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Server");
		setLayout(null);
		
		// Creating a Container object
		Container serverContainer = getContentPane();
		
		// Setting the title icon of the JFrame window
		ImageIcon imageIconTitle = new ImageIcon("./Images/server_icon.png");
		setIconImage(imageIconTitle.getImage());
		
		// Defining specifications for jPanelStatus to indicate connection status
		jPanelStatus = new JPanel();
		jPanelStatus.setBorder(null);
		jPanelStatus.setBackground(Color.RED);
	
		// Creating a jPanel to keep fields and button for communication
		JPanel jPanelConnection = new JPanel();
		// Defining specifications for jPanelConnection
		jPanelConnection.setBackground(new Color(245,85,0));
		jPanelConnection.setLayout(new FlowLayout(FlowLayout.CENTER,82,4));
		jPanelConnection.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.WHITE));
		
		// Creating a jPanel to keep fields (Port address field) for communication
		JPanel jPanelConnectionFields = new JPanel();
		// Defining specifications for jPnaelConnectionFields
		jPanelConnectionFields.setBackground(null);
		jPanelConnectionFields.setLayout(new FlowLayout(FlowLayout.CENTER,30,0));
		
		// Creating a jPanel to keep buttons (Open and Close buttons) for communication
		JPanel jPanelConnectionButtons = new JPanel();
		// Defining specifications for jPnaelConnectionButtons
		jPanelConnectionButtons.setBackground(null);
		jPanelConnectionButtons.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		
		// Creating a jPanel to show the chat messages
		JPanel jPanelChat = new JPanel();
		// Defining specifications for jPnaelChat
		jPanelChat.setBackground(Color.WHITE);
		jPanelChat.setLayout(new BorderLayout());
		jPanelChat.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(245,85,0)));
		
		// Creating a jPanel to keep field for typing message and send button
		JPanel jPanelSend = new JPanel();
		// Defining specifications for jPanelSend
		jPanelSend.setBackground(new Color(238,238,238));
		jPanelSend.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
		jPanelSend.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(245,85,0)));
		
		// Adding all the defined panels to the container
		serverContainer.add(jPanelStatus);
		serverContainer.add(jPanelConnection);
		serverContainer.add(jPanelChat);
		serverContainer.add(jPanelSend);
		
		// Adding panel for connection fields and buttons to the main connection panel
		jPanelConnection.add(jPanelConnectionFields);
		jPanelConnection.add(jPanelConnectionButtons);
		
		// Setting the position of the panels on the window
		jPanelStatus.setBounds(0,0,600,3);
		jPanelConnection.setBounds(0, 3, 600, 45);
		jPanelChat.setBounds(0,48,600,747);
		jPanelSend.setBounds(0,795,600,70);
		
		
		// CONFIGURATIONS FOR CONNECTION PANEL STARTS		
		
		// Defining combo box drop down items
		String[] comboBoxItems= {"TCP","UDP"};
		
		// Creating Connection Fields and Connection Buttons
		jTFieldPortNum = new JTextField("8080");
		jButtonOpen = new JButton("Open");
		jButtonClose = new JButton("Close");
		// Creating combo box with protocol items in it
		jCBoxProtocol = new JComboBox<>(comboBoxItems);

		// Adding all the fields (Port Field),buttons and combo box to the fields panel
		jPanelConnectionFields.add(jTFieldPortNum);
		jPanelConnectionFields.add(jCBoxProtocol);
		jPanelConnectionButtons.add(jButtonOpen);
		jPanelConnectionButtons.add(jButtonClose);

		// Setting size of the different components
		jTFieldPortNum.setPreferredSize(new Dimension(75,35));
		jCBoxProtocol.setPreferredSize(new Dimension(75,35));
		jButtonOpen.setPreferredSize(new Dimension(90,35));
		jButtonClose.setPreferredSize(new Dimension(90,35));

		// Setting borders to null for the Port text field
		jTFieldPortNum.setBorder(null);

		// Customizing fonts for different components 
		jTFieldPortNum.setFont(jTFieldPortNum.getFont().deriveFont(16.0f));
		jCBoxProtocol.setFont(jCBoxProtocol.getFont().deriveFont(14.0f));
		jButtonOpen.setFont(jButtonOpen.getFont().deriveFont(15.0f));
		jButtonClose.setFont(jButtonClose.getFont().deriveFont(15.0f));

		// Setting alignment for the port text field
		jTFieldPortNum.setHorizontalAlignment(JTextField.CENTER);
		
		// Setting specifications for combo box drop down
		jCBoxProtocol.setBackground(Color.WHITE);
		jCBoxProtocol.setFocusable(false);		
		jCBoxProtocol.setBorder(null);

		// Setting borders to null for the connection buttons
		jButtonOpen.setBorder(null);
		jButtonClose.setBorder(null);

		// Customizing button styles and configurations of connection buttons
		jButtonOpen.setBackground(new Color(0, 100, 0));
		jButtonOpen.setForeground(Color.WHITE);
		jButtonClose.setBackground(new Color(210, 5, 1));
		jButtonClose.setForeground(Color.WHITE);
		jButtonOpen.setFocusPainted(false);
		jButtonClose.setFocusPainted(false);
		jButtonClose.setEnabled(false);
		// CONFIGURATIONS FOR CONNECTION PANEL ENDS

		
		// CONFIGURATIONS FOR CHAT PANEL STARTS
		
		// Setting specifications for chat text area
		jTAreaChat = new JTextArea();
		jTAreaChat.setEditable(false);
		jTAreaChat.setMargin(new Insets(10,10,10,10));
		jTAreaChat.setFont(jTAreaChat.getFont().deriveFont(19.0f));
		jTAreaChat.setForeground(Color.BLACK);
		jTAreaChat.setLineWrap(true);
		jTAreaChat.setWrapStyleWord(true);
		
		// Creating and adding scroll pane to the chat text area 
		// so that the scroll bar appears when there is no space for extra text
		JScrollPane jScrollTextChat = new JScrollPane(jTAreaChat);
		jScrollTextChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollTextChat.setBorder(null);
		jPanelChat.add(jScrollTextChat);
		// CONFIGURATIONS FOR CHAT PANEL ENDS

		
		// CONFIGURATIONS FOR MESSAGE PANEL STARTS
		
		// Setting specifications for text area to type message	
		jTAreaMessage = new JTextArea();
		jTAreaMessage.setMargin(new Insets(5, 0, 0, 0));
		jTAreaMessage.setFont(jTAreaMessage.getFont().deriveFont(18.0f));
		jTAreaMessage.setForeground(Color.BLACK);
		jTAreaMessage.setLineWrap(true);
		jTAreaMessage.setWrapStyleWord(true);
		jTAreaMessage.setBackground(new Color(238,238,238));
		jTAreaMessage.setEditable(false);
		jTAreaMessage.addKeyListener(this);
		
		// Creating and adding scroll pane to the message text area 
		// so that the scroll bar appears when there is no space for extra text
		JScrollPane jScrollTextMsg = new JScrollPane(jTAreaMessage);
		jScrollTextMsg.setPreferredSize(new Dimension(500,60));
		jScrollTextMsg.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollTextMsg.setBorder(null);
		jScrollTextMsg.setBackground(new Color(238,238,238));
		
		// Creating send image icon for send button
		ImageIcon imageIconSend = new ImageIcon("./Images/send.png");
		
		// Setting specifications for send button
		jButtonSend = new JButton(imageIconSend);
		jButtonSend.setFocusPainted(false);
		jButtonSend.setBorder(null);
		jButtonSend.setPreferredSize(new Dimension(75,60));
		jButtonSend.setBackground(new Color(238,238,238));
		jButtonSend.setEnabled(false);
		
		// Adding send button and message text area to the send panel at bottom
		jPanelSend.add(jScrollTextMsg);
		jPanelSend.add(jButtonSend);
		
		// Adding action listeners to the buttons
		jButtonOpen.addActionListener(this);
		jButtonClose.addActionListener(this);
		jButtonSend.addActionListener(this);
	}
	// CONFIGURATIONS FOR MESSAGE PANEL ENDS
	
	// METHOD prepareGUI() ENDS

	
	
	
	
	// Defining actions of the buttons
	@Override
	public void actionPerformed(ActionEvent action) {
		
		// Defining actions when Close button is clicked 
		if (action.getSource().equals(jButtonClose)){
			jPanelStatus.setBackground(Color.RED);
			jButtonOpen.setEnabled(true);
			jTAreaMessage.setText(" ");
			jButtonClose.setEnabled(false);
			jTAreaChat.setText(" ");
			jButtonSend.setEnabled(false);
			jTAreaMessage.setEditable(false);
			jTFieldPortNum.setEditable(true);
			jCBoxProtocol.setEnabled(true);
			flag = true;
		}
		
		// Defining actions when Open button is clicked 
		if (action.getSource().equals(jButtonOpen)){
			flag = false;
			jPanelStatus.setBackground(Color.GREEN);
			jButtonOpen.setEnabled(false);
			jTAreaMessage.setEditable(true);
			jButtonSend.setEnabled(true);
			jButtonClose.setEnabled(true);
			jTFieldPortNum.setEditable(false);
			jCBoxProtocol.setEnabled(false);
			
			// Getting selected item from the combo box drop down
			String protocol = jCBoxProtocol.getSelectedItem().toString();
			
			// Running respective threads on the basis of protocol selected
			if (protocol.equals("TCP")){
				serverThread = new Thread(tcpServer);
			}
			
			else{
				serverThread = new Thread(udpServer);
			}
			JavaServer.serverThread.start();
		}
		
		// Adding actions for send button
		if (action.getSource().equals(jButtonSend)){
			// Starting synchronized block to call notifyAll method by the thread to 
			// make the thread wake up from the wait state called inside the run method of the thread
			synchronized (JavaServer.serverThread) {
				JavaServer.serverThread.notifyAll();
			}
		}
		
	}
	
	
	
	
	
	
	// Listening to key Events
	@Override
	public void keyPressed(KeyEvent e) {
		// Unused method
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Getting and storing key code of the released key
		int keyCode = e.getKeyCode();
		
		// Setting actions to be performed when enter button is pressed and released
		if (keyCode == KeyEvent.VK_ENTER){
			// Starting synchronized block to call notifyAll method by the thread to 
			// make the thread wake up from the wait state called inside the run method of the thread
			synchronized (JavaServer.serverThread) {
				JavaServer.serverThread.notifyAll();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Unused Method
	}
}





//Class to create TCP connection and running separate thread for the communication
class TCPServer implements Runnable{
	// Declaring a new object of the main class JavaClient
	JavaServer javaServer;
	
	//Default Constructor
	public TCPServer(){
		
	}
	//Parameterized Constructor
	public TCPServer(JavaServer javaServer){
		this.javaServer = javaServer;
	}
	
	@Override
	public void run() {
		javaServer.jTAreaChat.setText("Attempted TCP Connection...\n\n");
		try{	
			// Creating and opening a socket by the server with a specified port for communication
			ServerSocket sSocket = new ServerSocket(Integer.parseInt(javaServer.jTFieldPortNum.getText()));
			// Defining defining the socket through which communication occurs
			Socket clientSocket = null;
			// Defining InputStreamReader object to read the input stream
			InputStreamReader reader = null;
			// Defining BufferReader object to read the data from buffer
			BufferedReader buffer = null;
			// Defining PrintWriter object to write/send the data through the socket
			PrintWriter writer = null;
			
			// Starting while loop to continuously send and receive the data
			// (Continuous Connection)
			while(!javaServer.flag){
				//(RECEIVING THE DATA)
				// Accepting connections on the specified port
				clientSocket = sSocket.accept();
				// Creating InputStreamReader object to get the input stream
				reader = new InputStreamReader(clientSocket.getInputStream());
				// Creating BufferReader object to read the data from the input stream object
				buffer = new BufferedReader(reader);
				// Defining String object to store the message sent by the client
				String message;
				
				// Initializing file output stream object with path to export the chat message to a file
				FileOutputStream fileOut = new FileOutputStream("tcpChat.txt");
				// Initializing BufferOutputStream object to write data to the output stream
				BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut);
			
				// Checking whether the message received is null
				if ((message = buffer.readLine()) != null){
					// Appending the message to the text area chat
					javaServer.jTAreaChat.setText((javaServer.jTAreaChat.getText()+ "\n" +message).trim());
					
					// Extracting bytes from the message writing to the file
					bufferOut.write(javaServer.jTAreaChat.getText().getBytes());	
					bufferOut.flush();
					fileOut.close();
				}
				
				//(SENDING THE DATA)
				// Starting synchronized block to call the wait method by the thread
				// so that the thread pauses until it is notified to resume
				synchronized (JavaServer.serverThread) {
					JavaServer.serverThread.wait();
				}
		
				// Creating Output Stream Object to send a output stream
				OutputStream output = clientSocket.getOutputStream();
				// Creating writer object to write the data/message to the socket
				writer = new PrintWriter(output,true);
				// Defining String object to store the message got from message text area field
				String myMessage = javaServer.jTAreaMessage.getText();
				// Writing the data to be sent to the client
				writer.println("Server: "+ myMessage);
				// Appending messages to the chat text area
				javaServer.jTAreaChat.setText((javaServer.jTAreaChat.getText()+ "\n" +"Me: "+myMessage).trim());
				// Setting the value in message text area to null after it has been sent
				javaServer.jTAreaMessage.setText(null);
				
			}
			// Closing all the used resources
			writer.close();
			reader.close();
			buffer.close();
			clientSocket.close();
			sSocket.close();
		}
		// Catching the possible exceptions with an error dialogue
		catch(Exception e){
			int error = JOptionPane.showConfirmDialog(null, "A Connection Error Has Occurred.",
					"Connection Failed", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			// Exiting the application after the user has clicked OK or exited the Option Pane
			if (error == 0 || error == -1){
				System.exit(0);
			}
		}
	}
}




//Class to create UDP connection and running separate thread for the communication
class UDPServer implements Runnable{
	// Creating new object of the main class JavaClient
	JavaServer javaServer;
	
	//Default Constructor
	public UDPServer() {
		
	}
	
	//Parameterized Constructor
	public UDPServer(JavaServer javaServer){
		this.javaServer = javaServer;
	}
	
	@Override
	public void run() {
		javaServer.jTAreaChat.setText("Attempted UDP Connection...\n\n");
		try{	
			// Creating DataGram Socket object to create a communication Socket
			DatagramSocket dSocket = new DatagramSocket(Integer.parseInt(javaServer.jTFieldPortNum.getText()));
			
			// Starting while loop to continuously send and receive the data
			// (Continuous Connection)
			while(!javaServer.flag){
				
				//(RECEIVING THE DATA)
				// Creating buffer of size 1024 bytes to store the message in the form of bytes 
				byte[] buffer = new byte[1024];
				// Creating a DatagramPacket object to receive the data bytes
				DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
				// Receiving the packet from the socket
				dSocket.receive(packet);
				// Defining a String object to store the message as String
				String message;
				
				// Initializing file output stream object with path to export the chat message to a file
				FileOutputStream fileOut = new FileOutputStream("udpChat.txt");
				// Initializing BufferOutputStream object to write data to the output stream
				BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut);
				
				// Getting the data bytes, converting to the string data and storing as a String
				// Checking whether the message received is null
				if ((message = new String(packet.getData())) != null){
					// Appending the received message to the chat text area
					javaServer.jTAreaChat.setText((javaServer.jTAreaChat.getText()+ "\n" +"Client: "+message).trim());
					
					// Extracting bytes from the message and writing to the file
					bufferOut.write(javaServer.jTAreaChat.getText().getBytes());	
					bufferOut.flush();
					fileOut.close();
				}
				
				
				//(SENDING THE DATA)
				// Starting synchronized block to call the wait method by the thread
				// so that the thread pauses until it is notified to resume
				synchronized (JavaServer.serverThread) {
					JavaServer.serverThread.wait();
				}
				
				// Getting IP address from the connected socket
				InetAddress ipAddress = packet.getAddress();
				// Getting port number of the destination
				int port = packet.getPort();
				
				// Creating an String object to get and store the message typed 
				String myMessage = javaServer.jTAreaMessage.getText();
				// Getting bytes from the String message
				buffer = myMessage.getBytes();
				// Creating a combined Packet from the message, IP address and port to be sent over the socket
				DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,ipAddress,port);
				// Appending the message to the chat text area
				javaServer.jTAreaChat.setText((javaServer.jTAreaChat.getText()+ "\n" +"Me: "+myMessage).trim());
				// Sending the Packet through the socket
				dSocket.send(sendPacket);
				// Setting the value in message text area to null after it has been sent						
				javaServer.jTAreaMessage.setText(null);
				
			}
			// Closing the DataGram Socket
			dSocket.close();
		}
		// Catching the possible exceptions with an error message dialogue
		catch(Exception e){
			int error = JOptionPane.showConfirmDialog(null, "A Connection Error Has Occurred.",
					"Connection Failed", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			// Exiting the application after the user has clicked OK or exited the Option Pane
			if (error == 0 || error == -1){
				System.exit(0);
			}
		}
	}
}


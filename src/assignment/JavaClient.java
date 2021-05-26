package assignment;

// importing the required libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 * Program:		Java Client
 * Filename:	JavaClient.java
 * @author 		Subin Gyawali
 * Course:		BEng(Hons) Computing (Computer Networks Engineering)
 * Module:		CSY2026 - Modern Networks 
 * Tutor:		Khagendra Shah
 * @version:	2.8
 * Date:		15-Dec-2019
 */

public class JavaClient extends JFrame implements ActionListener,KeyListener{
	
	// Defining IP Address Field to enter IP Address
	protected JTextField jTFieldIpAddress;
	// Defining Port Address Field to enter Port number
	protected JTextField jTFieldPortNum;
	// Defining Combo box to choose protocol for communication i.e. TCP or UDP
	private JComboBox<String> jCBoxProtocol;
	// Defining button to start the connection with the server
	private JButton jButtonConnect;
	// Defining button to end the connection with the server
	private JButton jButtonEnd;
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
	protected static Thread clientThread;
	// Declaring an object of class TCPClient
	protected static TCPClient tcpClient;
	//Declaring an object of class UDPClient
	protected static UDPClient udpClient;
	
	// main METHOD TO START PROGRAM EXECUTION STARTS
	public static void main(String[] args) {
		// Creating an object of JavaClient class
		JavaClient clientGui = new JavaClient();
		// Calling the prepareGUI() method of class JavaClient
		clientGui.prepareGUI();
		// Setting the swing window with components visible
		clientGui.setVisible(true);
		// Initializing objects of the class TCPClient
		tcpClient = new TCPClient(clientGui);
		// Initializing objects of the class UDPClient 
		udpClient = new UDPClient(clientGui);

	}
	// main METHOD ENDS
	
	
	// METHOD prepareGUI() TO DESIGN GUI STARTS
	public void prepareGUI(){
		
		// Setting the specifications for the JFrame GUI window
		setSize(600, 900);
		setLocation(400, 70);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Client");
		setLayout(null);
		
		// Creating a Container object
		Container clientContainer = getContentPane();
		
		// Setting the title icon of the JFrame window
		ImageIcon imageIconTitle = new ImageIcon("./Images/client_icon.png");
		setIconImage(imageIconTitle.getImage());
		
		// Defining specifications for jPanelStatus to indicate connection status
		jPanelStatus = new JPanel();
		jPanelStatus.setBorder(null);
		jPanelStatus.setBackground(Color.RED);
		
		// Creating a jPanel to keep fields and button for communication
		JPanel jPanelConnection = new JPanel();
		// Defining specifications for jPanelConnection
		jPanelConnection.setBackground(new Color(0,100,255));
		jPanelConnection.setLayout(new FlowLayout(FlowLayout.CENTER,24,4));
		jPanelConnection.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.WHITE));
		
		// Creating a jPanel to keep fields (IP and Port address fields) for communication
		JPanel jPanelConnectionFields = new JPanel();
		// Defining specifications for jPnaelConnectionFields
		jPanelConnectionFields.setBackground(null);
		jPanelConnectionFields.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
		
		// Creating a jPanel to keep buttons (Connect and End buttons) for communication
		JPanel jPanelConnectionButtons = new JPanel();
		// Defining specifications for jPnaelConnectionButtons
		jPanelConnectionButtons.setBackground(null);
		jPanelConnectionButtons.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		
		// Creating a jPanel to show the chat messages
		JPanel jPanelChat = new JPanel();
		// Defining specifications for jPnaelChat
		jPanelChat.setBackground(Color.WHITE);
		jPanelChat.setLayout(new BorderLayout());
		jPanelChat.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0,100,255)));
		
		// Creating a jPanel to keep field for typing message and send button
		JPanel jPanelSend = new JPanel();
		// Defining specifications for jPanelSend
		jPanelSend.setBackground(new Color(238,238,238));
		jPanelSend.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
		jPanelSend.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0,100,255)));
		
		// Adding all the defined panels to the container
		clientContainer.add(jPanelStatus);
		clientContainer.add(jPanelConnection);
		clientContainer.add(jPanelChat);
		clientContainer.add(jPanelSend);
		
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
		jTFieldIpAddress = new JTextField("127.0.0.1");
		jTFieldPortNum = new JTextField("8080");
		jButtonConnect = new JButton("Connect");
		jButtonEnd = new JButton("End");
		
		// Defining JLabel to insert colon in between IP address and port number
		JLabel JLabelColon = new JLabel(":");
		// Creating combo box with protocol items in it
		jCBoxProtocol = new JComboBox<>(comboBoxItems);
		
		// Adding all the fields (IP and Port Fields),colon and combo box to the fields panel
		jPanelConnectionFields.add(jTFieldIpAddress);
		jPanelConnectionFields.add(JLabelColon);
		jPanelConnectionFields.add(jTFieldPortNum);
		jPanelConnectionFields.add(jCBoxProtocol);
		
		// Adding connection buttons ()
		jPanelConnectionButtons.add(jButtonConnect);
		jPanelConnectionButtons.add(jButtonEnd);
		
		// Setting size of the different components
		jTFieldIpAddress.setPreferredSize(new Dimension(150,35));
		jTFieldPortNum.setPreferredSize(new Dimension(75,35));
		jCBoxProtocol.setPreferredSize(new Dimension(75,35));
		jButtonConnect.setPreferredSize(new Dimension(90,35));
		jButtonEnd.setPreferredSize(new Dimension(90,35));
		
		// Setting borders to null for the text fields for IP and Port
		jTFieldIpAddress.setBorder(null);
		jTFieldPortNum.setBorder(null);
		
		// Customizing fonts for different components 
		JLabelColon.setFont(JLabelColon.getFont().deriveFont(25.0f));
		jTFieldIpAddress.setFont(jTFieldIpAddress.getFont().deriveFont(16.0f));
		jTFieldPortNum.setFont(jTFieldPortNum.getFont().deriveFont(16.0f));
		jCBoxProtocol.setFont(jCBoxProtocol.getFont().deriveFont(14.0f));
		jButtonConnect.setFont(jButtonConnect.getFont().deriveFont(15.0f));
		jButtonEnd.setFont(jButtonEnd.getFont().deriveFont(15.0f));
		
		// Setting alignment for the text fields
		jTFieldIpAddress.setHorizontalAlignment(JTextField.CENTER);
		jTFieldPortNum.setHorizontalAlignment(JTextField.CENTER);
		
		// Setting specifications for combo box drop down
		jCBoxProtocol.setBackground(Color.WHITE);
		jCBoxProtocol.setFocusable(false);
		jCBoxProtocol.setBorder(null);
		
		// Setting borders to null for the connection buttons
		jButtonConnect.setBorder(null);
		jButtonEnd.setBorder(null);
		
		// Customizing button styles and configurations of connection buttons
		jButtonConnect.setBackground(new Color(0, 100, 0));
		jButtonConnect.setForeground(Color.WHITE);
		jButtonEnd.setBackground(new Color(210, 5, 1));
		jButtonEnd.setForeground(Color.WHITE);
		jButtonConnect.setFocusPainted(false);
		jButtonEnd.setFocusPainted(false);
		jButtonEnd.setEnabled(false);
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
		JScrollPane jScrollText = new JScrollPane(jTAreaMessage);
		jScrollText.setPreferredSize(new Dimension(500,60));
		jScrollText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollText.setBorder(null);
		
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
		jPanelSend.add(jScrollText);
		jPanelSend.add(jButtonSend);
		
		// Adding action listeners to the buttons
		jButtonConnect.addActionListener(this);
		jButtonEnd.addActionListener(this);
		jButtonSend.addActionListener(this);	
	}
	// CONFIGURATIONS FOR MESSAGE PANEL ENDS

	// METHOD prepareGUI() ENDS
	
	
	
	
	// Defining actions of the buttons
	@Override
	public void actionPerformed(ActionEvent action) {
		
		// Defining actions when End button is clicked 
		if (action.getSource().equals(jButtonEnd)){
			jPanelStatus.setBackground(Color.RED);
			jButtonConnect.setEnabled(true);
			jTAreaMessage.setText(" ");
			jButtonEnd.setEnabled(false);
			jTAreaChat.setText(" ");
			jButtonSend.setEnabled(false);
			jTAreaMessage.setEditable(false);
			jTFieldIpAddress.setEditable(true);
			jTFieldPortNum.setEditable(true);
			jCBoxProtocol.setEnabled(true);
			flag = true;
		}
		
		// Defining actions when Connect button is clicked 
		if (action.getSource().equals(jButtonConnect)){
			flag = false;
			jPanelStatus.setBackground(Color.GREEN);
			jButtonConnect.setEnabled(false);
			jTAreaMessage.setEditable(true);
			jButtonSend.setEnabled(true);
			jButtonEnd.setEnabled(true);
			jTFieldIpAddress.setEditable(false);
			jTFieldPortNum.setEditable(false);
			jCBoxProtocol.setEnabled(false);
			
			// Getting selected item from the combo box drop down
			String protocol = jCBoxProtocol.getSelectedItem().toString();
			
			// Running respective threads on the basis of protocol selected
			if (protocol.equals("TCP")){
				clientThread = new Thread(tcpClient);
			}
			
			else{
				clientThread = new Thread(udpClient);
			}
			JavaClient.clientThread.start();
		}
		
		// Adding actions for send button
		if (action.getSource().equals(jButtonSend)){
			// Starting synchronized block to call notifyAll method by the thread to 
			// make the thread wake up from the wait state called inside the run method of the thread
			synchronized (JavaClient.clientThread) {
				JavaClient.clientThread.notifyAll();
			}
		}
	}
	
	
	
	// Listening to key Events
	@Override
	public void keyPressed(KeyEvent arg0) {
		// Unused method
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Getting and storing key code of the released key
		int keyCode = arg0.getKeyCode();
		
		// Setting actions to be performed when enter button is pressed and released
		if (keyCode == KeyEvent.VK_ENTER){
			// Starting synchronized block to call notifyAll method by the thread to 
			// make the thread wake up from the wait state called inside the run method of the thread
			synchronized (JavaClient.clientThread) {
				JavaClient.clientThread.notifyAll();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Unused Method
	}
}






//Class to create TCP connection and running separate thread for the communication
class TCPClient implements Runnable{
	// Declaring a new object of the main class JavaClient
	JavaClient javaClient;
	
	//Default Constructor
	public TCPClient() {
		
	}
	
	//Parameterized Constructor
	public TCPClient(JavaClient javaClient){
		this.javaClient = javaClient;
	}
	
	@Override
	public void run() {
		
		javaClient.jTAreaChat.setText("Attempted TCP Connection...\n\n");
		try{
			// Defining InputStreamReader object to read the input stream
			InputStreamReader reader = null;
			// Defining BufferReader object to read the data from buffer
			BufferedReader buffer = null;
			// Defining PrintWriter object to write/send the data through the socket
			PrintWriter writer = null;	
			// Defining defining the socket through which communication occurs
			Socket clientSocket = null;
			
			// Starting while loop to continuously send and receive the data
			// (Continuous Connection)
			while (!javaClient.flag){
				//(SENDING THE DATA)
				// Starting synchronized block to call the wait method by the thread
				// so that the thread pauses until it is notified to resume
				synchronized (JavaClient.clientThread) {
					JavaClient.clientThread.wait();
				}
				
				// Creating Socket object with IP Address and Port Number
				clientSocket = new Socket(javaClient.jTFieldIpAddress.getText(), Integer.parseInt(javaClient.jTFieldPortNum.getText()));
				// Creating Output Stream Object to send a output stream
				OutputStream output = clientSocket.getOutputStream();
				// Creating writer object to write the data/message to the socket
				writer = new PrintWriter(output,true);
				// Defining String object to store the message got from message text area field
				String myMessage = javaClient.jTAreaMessage.getText();
				// Writing the data to be sent to the server
				writer.println("Client: "+myMessage);
				// Appending messages to the chat text area
				javaClient.jTAreaChat.setText((javaClient.jTAreaChat.getText()+ "\n" +"Me: "+myMessage).trim());
				// Setting the value in message text area to null after it has been sent
				javaClient.jTAreaMessage.setText(null);
			
				
				//(RECEIVING THE DATA)
				// Creating InputStreamReader object to get the input stream
				reader = new InputStreamReader(clientSocket.getInputStream());
				// Creating BufferReader object to read the data from the input stream object
				buffer = new BufferedReader(reader);
				// Defining String object to store the message sent by the server
				String message;
				
				// Checking whether the message received is null
				if ((message = buffer.readLine()) != null){
					// Appending the message to the text area chat
					javaClient.jTAreaChat.setText((javaClient.jTAreaChat.getText()+ "\n" +message).trim());
				}
			}
			
			// Closing all the used resources
			writer.close();
			reader.close();
			buffer.close();
			clientSocket.close();
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






// Class to create UDP connection and running separate thread for the communication
class UDPClient implements Runnable{
	// Declaring a new object of the main class JavaClient
	JavaClient javaClient;
	
	//Default Constructor
	public UDPClient() {
		
	}
	
	//Parameterized Constructor
	public UDPClient(JavaClient javaClient){
		this.javaClient = javaClient;
	}
	
	@Override
	public void run() {
		
		javaClient.jTAreaChat.setText("Attempted UDP Connection...\n\n");
		try{
			// Creating DataGram Socket object to create a communication Socket
			DatagramSocket dSocket = new DatagramSocket();
			
			// Starting while loop to continuously send and receive the data
			// (Continuous Connection)
			while (!javaClient.flag){
				//(SENDING THE DATA)
				// Starting synchronized block to call the wait method by the thread
				// so that the thread pauses until it is notified to resume
				synchronized (JavaClient.clientThread) {
					JavaClient.clientThread.wait();
				}
				
				// Creating an String object to get and store the message typed 
				String myMessage = javaClient.jTAreaMessage.getText();
				// Creating buffer of size 1024 bytes to store the message in the form of bytes 
				byte[] buffer = new byte[1024];
				// Getting bytes from the String message
				buffer = myMessage.getBytes();
				
				// Getting IP Address from the text field and Creating InetAddress object
				InetAddress ipAddress = InetAddress.getByName(javaClient.jTFieldIpAddress.getText());
				// Getting port number from port field and storing in an integer variable
				int port = Integer.parseInt(javaClient.jTFieldPortNum.getText());
				
				// Creating a combined Packet from the message, IP address and port to be sent over the socket
				DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,ipAddress,port);
				// Appending the message to the chat text area
				javaClient.jTAreaChat.setText((javaClient.jTAreaChat.getText()+ "\n" +"Me: "+myMessage).trim());
				// Sending the Packet through the socket
				dSocket.send(sendPacket);
				// Setting the value in message text area to null after it has been sent
				javaClient.jTAreaMessage.setText(null);
				
				
				//(RECEIVING THE DATA)
				// Creating a DatagramPacket object to receive the data bytes
				DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length);

				// Receiving the packet from the socket
				dSocket.receive(receivePacket);
				// Defining a String object to store the message as String
				String message;
			
				// Getting the data bytes, converting to the string data and storing as a String
				// Checking whether the message received is null
				if ((message = new String(receivePacket.getData())) != null){
					// Appending the received message to the chat text area
					javaClient.jTAreaChat.setText((javaClient.jTAreaChat.getText()+ "\n" +"Server: "+message).trim());
				}
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

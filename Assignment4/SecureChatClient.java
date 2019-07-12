/* CS 1501
   Primitive chat client.
   This client connects to a server so that messages can be typed and forwarded
   to all other clients.  Try it out in conjunction with ImprovedChatServer.java.
   You will need to modify / update this program to incorporate the secure elements
   as specified in the Assignment sheet.  Note that the PORT used below is not the
   one required in the assignment -- for your SecureChatClient be sure to
   change the port that so that it matches the port specified for the secure
   server.
*/
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    BufferedReader myReader;
    PrintWriter myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
    Socket connection;
    BigInteger serverE, serverN;
    String cipherType;
    BigInteger cipherKey, encryptedKey;
    SymCipher cipher;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;

    public SecureChatClient ()
    {
        try {

            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
            InetAddress addr =
                    InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);   // Connect to server with new socket

            outStream = new ObjectOutputStream(connection.getOutputStream());
            outStream.flush();

            inStream = new ObjectInputStream(connection.getInputStream());

            serverE = (BigInteger) inStream.readObject();
            System.out.println("The server's E: " + serverE);
            serverN = (BigInteger) inStream.readObject();
            System.out.println("The server's N: " + serverN);
            cipherType = (String) inStream.readObject();
            System.out.println("The server's preferred encryption type: " + cipherType);

            if (cipherType.equals("Add")){
                cipher = new Add128();
            }
            else if (cipherType.equals("Sub")){
                cipher = new Substitute();
            }

            cipherKey = new BigInteger(1, cipher.getKey());
            System.out.println("Symmetric Key: " + cipherKey);
            encryptedKey = cipherKey.modPow(serverE, serverN); // RSA encrypted version of the key for the cipher
            outStream.writeObject(encryptedKey);
            outStream.flush();


//            myReader =
//                    new BufferedReader(
//                            new InputStreamReader(inStream));   // Get Reader and Writer
//
//            myWriter =
//                    new PrintWriter(
//                            new BufferedWriter(
//                                    new OutputStreamWriter(outStream)), true);


            // Send name to Server.  Server will need
            // this to announce sign-on and sign-off
            // of clients
            outStream.writeObject(cipher.encode(myName));
            outStream.flush();


            this.setTitle(myName);      // Set title to identify chatter

            Box b = Box.createHorizontalBox();  // Set up graphical environment for
            outputArea = new JTextArea(8, 30);  // user
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");

            inputField = new JTextField("");  // This is where user will type input
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  // Thread is to receive strings
            outputThread.start();                    // from Server

            addWindowListener(
                    new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        {
                            try {
                                outStream.writeObject(cipher.encode("CLIENT CLOSING"));
                                outStream.flush();
                                //myWriter.println("CLIENT CLOSING");
                                System.exit(0);
                            }
                            catch (Exception e2){
                                System.out.println(e2 + ": error");
                            }
                        }
                    }
            );

            setSize(500, 200);
            setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
        }
    }

    public void run()
    {
        while (true)
        {
            try {
                byte[] bytesReceived = (byte[])inStream.readObject();
                System.out.println("Receiving Message:");
                System.out.println("Bytes Received: ");
                for(byte b : bytesReceived){
                    System.out.print(b + " ");
                }
                System.out.println();
                String currMsg = cipher.decode(bytesReceived);
                System.out.println("Decrypted Bytes: ");
                for(byte b : currMsg.getBytes()){
                    System.out.print(b + " ");
                }
                System.out.println();
                System.out.println("Original Message: " + currMsg);
                System.out.println();

                outputArea.append(currMsg+"\n");
            }
            catch (Exception e)
            {
                System.out.println(e +  ", closing client!");
                break;
            }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        try {
            String currMsg = e.getActionCommand();      // Get input value
            String msgToSend = myName + ":" + currMsg;

            //The following lines of code are what is being sent to the server
            System.out.println("Sending Message:");
            System.out.println("Original String: " + msgToSend);
            System.out.println("Array of bytes: ");
            for(byte b : msgToSend.getBytes()){
                System.out.print(b + " ");
            }
            System.out.println();
            System.out.println("Encoded Array of bytes: ");
            for (byte b : cipher.encode(msgToSend)){
                System.out.print(b + " ");
            }
            System.out.println();
            System.out.println();
            inputField.setText("");
            outStream.writeObject(cipher.encode(msgToSend));
            outStream.flush();
            //myWriter.println(myName + ":" + currMsg);   // Add name and send it
        }
        catch (Exception e2){
            System.out.println(e + ", error");
        }
    }                                               // to Server

    public static void main(String [] args)
    {
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}


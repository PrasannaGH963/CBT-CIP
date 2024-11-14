package Java_Int;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.awt.Font;
import javax.swing.border.EmptyBorder;

public class ChatClient {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private JTextArea messageArea;
    private JTextField messageInput;
    private String username;
    private JButton sendButton;

    public ChatClient(String username) {
        this.username = username;
        initializeChatInterface();
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);  // port number same as server
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            new Thread(new IncomingMessageHandler()).start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error connecting to the server");
        }
    }

    private void initializeChatInterface() {
        JFrame frame = new JFrame("Chat - " + username);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        messageInput = new JTextField();
        messageInput.setFont(new Font("Arial", Font.PLAIN, 14));
        messageInput.addActionListener(new SendMessageListener());
        
        sendButton = new JButton("Send");
        sendButton.addActionListener(new SendMessageListener());

        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            writer.println(username + ": " + message);
            messageInput.setText("");
            messageInput.requestFocus();
        }
    }

    private class SendMessageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendMessage(messageInput.getText());
        }
    }

    private class IncomingMessageHandler implements Runnable {
        public void run() {
            try {
                String incomingMessage;
                while ((incomingMessage = reader.readLine()) != null) {
                    messageArea.append(incomingMessage + "\n");
                }
            } catch (IOException ex) {
                System.out.println("Error reading messages from server: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username != null && !username.isEmpty()) {
            new ChatClient(username);
        }
    }
}
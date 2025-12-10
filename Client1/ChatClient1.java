import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient1 extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter writer;

    public ChatClient1(String clientName) {
        setTitle(clientName);
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scroll = new JScrollPane(chatArea);
        add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        connectToServer();
        setVisible(true);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 5000);
            writer = new PrintWriter(socket.getOutputStream(), true);

            // receive messages
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                    );
                    String msg;
                    while ((msg = reader.readLine()) != null) {
                        chatArea.append(msg + "\n");
                    }
                } catch (Exception ignored) {}
            }).start();

        } catch (Exception e) {
            chatArea.append("Unable to connect to server.\n");
        }
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            writer.println(getTitle() + ": " + msg);
            inputField.setText("");
        }
    }

    public static void main(String[] args) {
        new ChatClient1("Client 1");
    }
}

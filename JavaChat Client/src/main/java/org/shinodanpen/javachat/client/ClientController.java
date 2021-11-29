package org.shinodanpen.javachat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.shinodanpen.javachat.common.Packet;
import org.shinodanpen.javachat.common.PacketType;
import org.shinodanpen.javachat.common.packets.ClosePacket;

import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class ClientController{

    private ClientThread clientThread;

    @FXML
    private TextArea messageArea;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField portField;
    @FXML
    private TextField addressField;
    @FXML
    private RadioButton connectButton;
    @FXML
    private RadioButton disconnectButton;
    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> messageListView;
    @FXML
    private Alert alert;

    public ClientController(){
        this.clientThread = new ClientThread(this);
    }


    @FXML
    public void initialize() {}

    public void addMessageToList(Packet packet) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = packet.getDateTime().format(formatter);


        String message = packet.getName() + packet.getMessage() + " [" + formattedDateTime + "]";
        System.out.println("Receiving packet with type: " + packet.getType() + " from " + packet.getName());
        Platform.runLater(() -> {

            if(packet.getType() == PacketType.CLOSE){
                ClosePacket packet1 = (ClosePacket) packet;
                if(packet1.isUsernameRefuse())
                try {
                    alertUsernameInUse();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                } else {

                    messageListView.getItems().add(message);
                    return;
                }
            } else {
                messageListView.getItems().add(message);
            }
        });
    }

    @FXML
    private void onConnectButtonClick() throws IOException {
        usernameField.setDisable(true);
        portField.setDisable(true);
        addressField.setDisable(true);
        connectButton.setDisable(true);
        connectButton.setSelected(true);


        if(usernameField.getText().isEmpty()) {
            usernameField.setText("user");
        }
        if(portField.getText().isEmpty()) {
            portField.setText("49654");
        }


        disconnectButton.setDisable(false);
        disconnectButton.setSelected(false);
        messageArea.setDisable(false);
        sendButton.setDisable(false);
        messageListView.setDisable(false);





        clientThread = new ClientThread(this);
        clientThread.startConnection(usernameField.getText(),
                Integer.parseInt(portField.getText()), addressField.getText());
        clientThread.start();

    }

    @FXML
    private void onSendButtonClick(){
        clientThread.sendMessage(messageArea.getText());
        messageArea.setText("");
    }

    @FXML
    public void onDisconnectButtonClick() {
        try {
            usernameField.setDisable(false);
            portField.setDisable(false);
            addressField.setDisable(false);
            connectButton.setDisable(false);
            connectButton.setSelected(false);


            disconnectButton.setDisable(true);
            disconnectButton.setSelected(true);
            messageArea.setDisable(true);
            sendButton.setDisable(true);

            messageListView.setDisable(true);


            clientThread.closeConnection("Quit from GUI");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void alertUsernameInUse() throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                alert = new Alert(Alert.AlertType.ERROR, "Username already in use. Try again with a different one.", ButtonType.OK);
                alert.show();
                onDisconnectButtonClick();
            }
        });
    }
}

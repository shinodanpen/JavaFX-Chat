module JavaChat.Client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports org.shinodanpen.javachat.client;
    opens org.shinodanpen.javachat.client to javafx.fxml;
}
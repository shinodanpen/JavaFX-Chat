package org.shinodanpen.javachat.client;

/*import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;*/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/client_panel.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("client - JavaChat");

        primaryStage.getIcons().add(new Image("https://i.imgur.com/9AQt0kx.png"));

        primaryStage.setScene(scene);
        primaryStage.show();

        ClientController controller = loader.getController();

        primaryStage.setOnCloseRequest(e -> {
            // Executed on window termination from X button
            try {
                controller.onDisconnectButtonClick();
                this.stop();
            } catch (Exception ex) {
                System.out.println("Application closed.");
            }
            System.exit(0);
        });
    }
}


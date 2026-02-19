package adolf;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final AdolfBot bot = new AdolfBot();

    @Override
    public void start(Stage stage) {
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        TextField inputField = new TextField();
        inputField.setPromptText("Type a command (e.g. list, todo ..., bye)");

        Button sendButton = new Button("Send");

        Runnable send = () -> {
            String userText = inputField.getText();
            if (userText == null || userText.trim().isEmpty()) return;

            chatArea.appendText("You: " + userText + "\n");

            String response = bot.getResponse(userText);
            chatArea.appendText("Adolf: " + response + "\n\n");

            inputField.clear();

            if (bot.isExitCommand(userText)) {
                stage.close();
            }
        };

        sendButton.setOnAction(e -> send.run());
        inputField.setOnAction(e -> send.run()); // press Enter to send

        HBox inputBox = new HBox(8, inputField, sendButton);
        VBox root = new VBox(10, scrollPane, inputBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Adolf");
        stage.setScene(scene);
        stage.show();

        chatArea.appendText("Adolf: Hello! I'm Adolf.\n");
        chatArea.appendText("Adolf: What can I do for you?\n\n");
    }
}
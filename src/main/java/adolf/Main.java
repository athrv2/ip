package adolf;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX UI for interacting with the Adolf bot.
 * Uses an asymmetric conversation layout (user vs bot styled differently)
 * and highlights error messages.
 */
public class Main extends Application {

    private static final double MIN_WIDTH = 400;
    private static final double MIN_HEIGHT = 300;
    private static final String USER_BUBBLE_STYLE =
            "-fx-background-color: #d4e8f7; -fx-background-radius: 12; "
                    + "-fx-padding: 8 12; -fx-font-size: 14px;";
    private static final String BOT_BUBBLE_STYLE =
            "-fx-background-color: #f0f0f0; -fx-background-radius: 12; "
                    + "-fx-padding: 8 12; -fx-font-size: 14px;";
    private static final String ERROR_BUBBLE_STYLE =
            "-fx-background-color: #ffe0e0; -fx-background-radius: 12; "
                    + "-fx-border-color: #c44; -fx-border-radius: 10; -fx-border-width: 1; "
                    + "-fx-padding: 8 12; -fx-font-size: 14px;";

    private final AdolfBot bot = new AdolfBot();
    private VBox chatContent;
    private ScrollPane scrollPane;

    @Override
    public void start(Stage stage) {
        chatContent = new VBox(10);
        chatContent.setPadding(new Insets(10));
        chatContent.setFillWidth(true);

        scrollPane = new ScrollPane(chatContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #fafafa;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        TextField inputField = new TextField();
        inputField.setPromptText("Type a command (e.g. list, todo ..., bye)");
        inputField.setStyle("-fx-font-size: 14px;");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-font-size: 14px;");

        Runnable send = () -> {
            String userText = inputField.getText();
            if (userText == null || userText.trim().isEmpty()) {
                return;
            }

            addMessage(userText, true);
            String response = bot.getResponse(userText);
            boolean isError = response.startsWith("Oops —") || response.startsWith("OOPS!!!");
            addMessage(response, false, isError);
            inputField.clear();

            if (bot.isExitCommand(userText)) {
                stage.close();
            }
        };

        sendButton.setOnAction(e -> send.run());
        inputField.setOnAction(e -> send.run());

        HBox inputBox = new HBox(8, inputField, sendButton);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.setPadding(new Insets(8, 0, 0, 0));

        VBox root = new VBox(10, scrollPane, inputBox);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #e8e2d8;");

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Adolf — Your task buddy");
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setResizable(true);
        stage.show();

        addMessage("Hey! I'm Adolf, your task buddy.\nWhat would you like to do?", false, false);
    }

    private void addMessage(String text, boolean fromUser) {
        addMessage(text, fromUser, false);
    }

    private void addMessage(String text, boolean fromUser, boolean isError) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(400);

        HBox bubble = new HBox(label);

        if (fromUser) {
            bubble.setStyle(USER_BUBBLE_STYLE);
            bubble.setAlignment(Pos.CENTER_RIGHT);
            HBox row = new HBox(bubble);
            row.setAlignment(Pos.CENTER_RIGHT);
            chatContent.getChildren().add(row);
        } else {
            bubble.setStyle(isError ? ERROR_BUBBLE_STYLE : BOT_BUBBLE_STYLE);
            bubble.setAlignment(Pos.CENTER_LEFT);
            HBox row = new HBox(bubble);
            row.setAlignment(Pos.CENTER_LEFT);
            chatContent.getChildren().add(row);
        }

        scrollPane.setVvalue(1.0);
    }
}

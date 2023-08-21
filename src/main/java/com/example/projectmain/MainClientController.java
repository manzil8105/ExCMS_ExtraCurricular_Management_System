package com.example.projectmain;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainClientController implements Initializable {
    @FXML
    private Button sent;
    @FXML
    private TextField textField;
    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;
    private VBox vBoxText;
    private Text text;
    private TextFlow textFlow;
    private VBox lineMargin;
    private double x;
    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client(new Socket("localhost", 1234));
            System.out.println("Connected to Server");
        } catch (IOException e) {
            e.printStackTrace();
        }

        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollPane.setVvalue((double) newValue);
            }
        });

        client.receiveMassageFromServer(vBox);
    }

    @FXML
    void sentAction(ActionEvent event) {
        String massageToServer = textField.getText();

        if (!Objects.equals(massageToServer, "")) {
            text = new Text(massageToServer);
            text.setFont(Font.font(18));
            text.setStyle("-fx-fill: white;");
            x = text.getLayoutBounds().getWidth();

            textFlow = new TextFlow(text);
            textFlow.setPadding(new Insets(6, 10, 6, 10));
            textFlow.setStyle("-fx-background-color: #1b1bfa;" + "-fx-background-radius: 15");
            textFlow.setMaxWidth(x + 20);

            vBoxText = new VBox();
            vBoxText.setAlignment(Pos.TOP_RIGHT);
            vBoxText.getChildren().add(textFlow);

            lineMargin = new VBox();
            lineMargin.setPadding(new Insets(5, 0, 0, 5));

            vBox.getChildren().add(vBoxText);
            vBox.getChildren().add(lineMargin);

            textField.clear();

            client.sentMassageToServer(massageToServer);
        }
    }

    public void serverText(String massageFromClient, VBox vBox) {
        text = new Text(massageFromClient);
        text.setFont(Font.font(18));
        text.setStyle("-fx-fill: #000000;");
        x = text.getLayoutBounds().getWidth();

        textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(6, 10, 6, 10));
        textFlow.setStyle("-fx-background-color: #b9b8b8;" + "-fx-background-radius: 15");
        textFlow.setMaxWidth(x + 20);

        vBoxText = new VBox();
        vBoxText.setAlignment(Pos.TOP_LEFT);
        vBoxText.getChildren().add(textFlow);

        lineMargin = new VBox();
        lineMargin.setPadding(new Insets(5, 0, 0, 5));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(vBoxText);
                vBox.getChildren().add(lineMargin);
            }
        });
    }
}
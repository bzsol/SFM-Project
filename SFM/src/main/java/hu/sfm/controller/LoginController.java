package hu.sfm.controller;

import hu.sfm.entity.User;
import hu.sfm.main.Main;
import hu.sfm.utils.Encryption;
import hu.sfm.utils.JPAUserDAO;
import hu.sfm.utils.UserDAO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    @FXML
    private Button loginBtn;

    @FXML
    private Label loginTextLabel;

    @FXML
    private Label loginPwLabel;

    @FXML
    private PasswordField logInputPassw;

    @FXML
    private TextField logInputUname;

    @FXML
    private VBox loginErrorMsgVbox;

    /** Designt megvalósító eventek **/
    @FXML
    private void onMouseEnteredLogin(MouseEvent event) {
        loginBtn.setStyle("-fx-background-color: #2199dd");
        loginBtn.setTextFill(Paint.valueOf("white"));
    }

    @FXML
    private void onMouseExitedLogin(MouseEvent event) {
        loginBtn.setStyle("-fx-background-color: white");
        loginBtn.setTextFill(Paint.valueOf("#2199dd"));
    }

    @FXML
    private void onMouseEnteredText(MouseEvent event) {
        loginTextLabel.setTextFill(Paint.valueOf("white"));
    }

    @FXML
    private void onMouseExitedText(MouseEvent event) {
        loginTextLabel.setTextFill(Paint.valueOf("#2199dd"));
    }

    @FXML
    private void onMouseEnteredPw(MouseEvent event) {
        loginPwLabel.setTextFill(Paint.valueOf("white"));
    }

    @FXML
    private void onMouseExitedPw(MouseEvent event) {
        loginPwLabel.setTextFill(Paint.valueOf("#2199dd"));
    }
    /** Designt megvalósító eventek **/

    // Hyperlink a regisztrációs GUI-ra
    @FXML
    private void onActionRegistrationLink(ActionEvent event) throws IOException {
        Main.setRoot("/fxml/registrationpanel");
    }

    @FXML
    void onLogIn(ActionEvent event) throws IOException {
        for (var timer : Timers) {
            timer.stop();
        }

        String userName = logInputUname.getText();
        String passWord = logInputPassw.getText();
        boolean validate = false;
        try {
            UserDAO uDAO = new JPAUserDAO();
            if(userName.equals("")||passWord.equals("")){
                if (loginErrorMsgVbox.getChildren().size() > 0) {
                    loginErrorMsgVbox.getChildren().remove(0);
                }
                Label label = Main.createErrorLabel("Sikertelen bejelentkezés!", "- Nem maradhat üresen mező!");
                loginErrorMsgVbox.getChildren().add(label);
                TimeUntilDisappear();
                logInputPassw.setText("");
                logInputUname.setText("");
            }else {

                for (User u : uDAO.getUser()) {
                    if (u.getUsername().equals(userName) && Encryption.visszafejt(u.getPassword()).equals(passWord)) {

                        validate = true;
                        Main.actUser=u;
                        break;
                    }
                }
                if (validate) {
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.setHeight(936);
                    stage.setWidth(1616);
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    Main.setRoot("/fxml/dashboard");

                } else {
                    if (loginErrorMsgVbox.getChildren().size() > 0) {
                        loginErrorMsgVbox.getChildren().remove(0);
                    }
                    Label label = Main.createErrorLabel("Sikertelen bejelentkezés!", "- Hibás felhasználónevet, vagy jelszót adott meg!");
                    loginErrorMsgVbox.getChildren().add(label);
                    TimeUntilDisappear();
                    logInputPassw.setText("");
                    logInputUname.setText("");
                }
            }
            uDAO.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Timeline> Timers = new ArrayList<>();

    private void TimeUntilDisappear() {
        Timeline disappear = new Timeline(
                new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (loginErrorMsgVbox.getChildren().size() > 0) {
                            loginErrorMsgVbox.getChildren().remove(0);
                        }
                    }
                })
        );
        disappear.setCycleCount(Timeline.INDEFINITE);
        disappear.play();
        Timers.add(disappear);
    }

}

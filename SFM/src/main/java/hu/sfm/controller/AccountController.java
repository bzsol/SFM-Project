package hu.sfm.controller;

import hu.sfm.entity.Permission;
import hu.sfm.entity.User;
import hu.sfm.main.Main;
import hu.sfm.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AccountController {
    @FXML
    private ChoiceBox<String> accountChoiceBox;

    @FXML
    private TextField vezNev;

    @FXML
    private TextField kerNev;

    @FXML
    private TextField userName;

    @FXML
    private TextField email;

    @FXML
    private PasswordField  newPass;

    @FXML
    private PasswordField newPassCheck;

    @FXML
    private ChoiceBox<Permission> permissionChoiceBox;

    @FXML
    private TextField addDate;

    @FXML
    private TextField salary;

    @FXML
    private TextField regDate;

    @FXML
    private TextField birthDate;

    private String initVeznev;

    private String initKernev;

    private String initEmail;

    private String initBirthDate;

    @FXML
    private void initialize() {
        UserDAO userDAO = new JPAUserDAO();
        for (User u : userDAO.getUser()){
            accountChoiceBox.getItems().add(u.getUsername());
        }

        permissionChoiceBox.getItems().addAll(Permission.values());

        loader(Main.actUser.getUsername());

        newPass.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color: #2199dd;-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
        newPass.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color: #2199dd;-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");


        if (!Main.actUser.getPerm().equals(Permission.ADMIN))
        {
            accountChoiceBox.setVisible(false);
            permissionChoiceBox.setDisable(true);
            salary.setDisable(true);
        }
        accountChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loader(newValue));
    }

    @FXML
    private void onActionChangePass(ActionEvent event) {

        if(!newPass.getText().isEmpty() || !newPassCheck.getText().isEmpty()){

            if(newPass.getText().equals(newPassCheck.getText()) && UserPassChecker.passCheck(newPassCheck.getText())){
                UserDAO userDAO = new JPAUserDAO();
                for (User u : userDAO.getUser()){
                    if(u.getUsername().equals(accountChoiceBox.getValue())){
                        u.setPassword(Encryption.titkosit(newPass.getText()));
                        u.setAddDate(LocalDate.now());
                        userDAO.saveUser(u);
                        PopupHandler.alertMsg = "Jelszó sikeresen megváltoztatva!";
                        PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
                        newPass.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:#2199dd;-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
                        newPassCheck.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:#2199dd;-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
                        newPass.clear();
                        newPassCheck.clear();
                    }
                }
            } else if (UserPassChecker.passCheck(newPassCheck.getText())) {
                PopupHandler.alertMsg = "A jelszavak nem egyeznek!";
                PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
                newPass.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:rgb(220, 40, 40);-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
                newPassCheck.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:rgb(220, 40, 40);-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
            } else {
                PopupHandler.alertMsg = "A megadott jelszó nem felel meg a kritériumoknak!";
                PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
                newPass.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:rgb(220, 40, 40);-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
                newPassCheck.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:rgb(220, 40, 40);-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
            }
        } else {
            PopupHandler.alertMsg = "A jelszó mezők nem maradhatnak üresen!";
            PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
            newPass.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:rgb(220, 40, 40);-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
            newPassCheck.setStyle("-fx-background-color: transparent;-fx-background-insets:0;-fx-border-width: 0px 0px 2px 0px;-fx-border-color:rgb(220, 40, 40);-fx-text-fill: white;-fx-padding:0;-fx-alignment: BASELINE_RIGHT;");
        }

    }

    @FXML
    private void onActionDeleteAccount (ActionEvent event) {

        if(!Main.actUser.getPerm().equals(Permission.ADMIN))
        {
            PopupHandler.alertMsg= "Nincs jogosultságod a felhasználói fiók törlésére!";
            PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
        }
        else
        {
            UserDAO userDAO = new JPAUserDAO();

            for (User u : userDAO.getUser())
            {
                if (u.getUsername().equals(userName.getText()) && !u.getPerm().equals(Permission.ADMIN))
                {
                    String name = u.getUsername();
                    userDAO.deleteUser(u);
                    PopupHandler.alertMsg = "A(z) " + name + " felhasználó sikeresen törölve a rendszerből!";
                    PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
                    accountChoiceBox.getItems().remove(u.getUsername());
                } else if (u.getUsername().equals(userName.getText())) {
                    PopupHandler.alertMsg = "Adminisztrátori jogosultsággal rendelkező felhasználó nem törölhető!";
                    PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
                }
            }
            loader(Main.actUser.getUsername());
        }



    }


    @FXML
    private void onActionSaveAccount (ActionEvent event) {
        UserDAO userDAO = new JPAUserDAO();
        boolean hozza_adjam=true;
        boolean is_admin = false;

        for (User u : userDAO.getUser())
        {

            if(u.getUsername().equals(accountChoiceBox.getValue()))
            {
                if (UserPassChecker.NameCheck(vezNev.getText())) {
                    if(Main.actUser.getVezetekNev() == null || !u.getVezetekNev().equals(vezNev.getText())){
                        vezNev.setStyle("-fx-border-color: #2199dd; -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                        u.setVezetekNev(vezNev.getText());
                    }
                } else if (!initVeznev.equals(vezNev.getText())) {
                    hozza_adjam = false;
                    vezNev.setStyle("-fx-border-color:  rgb(220, 40, 40); -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                }

                if (UserPassChecker.NameCheck((kerNev.getText()))) {
                    if(Main.actUser.getKeresztNev() == null || !u.getKeresztNev().equals(kerNev.getText())){
                        kerNev.setStyle("-fx-border-color: #2199dd; -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                        u.setKeresztNev(kerNev.getText());
                    }
                } else if (!initKernev.equals(kerNev.getText())) {
                    hozza_adjam = false;
                    kerNev.setStyle("-fx-border-color:  rgb(220, 40, 40); -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                }

                if (UserPassChecker.EmailChecker(email.getText())) {
                    if(Main.actUser.getEmail() == null || !u.getEmail().equals(email.getText())){
                        email.setStyle("-fx-alignment:  BASELINE_RIGHT;-fx-border-color: #2199dd; -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                        u.setEmail(email.getText());
                    }
                } else if (!initEmail.equals(email.getText())) {
                    hozza_adjam = false;
                    email.setStyle("-fx-border-color:  rgb(220, 40, 40); -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                }

                if (UserPassChecker.dateCheck(birthDate.getText())) {
                    if(Main.actUser.getBirthDate() == null || !String.valueOf(u.getBirthDate()).equals(birthDate.getText())){
                        birthDate.setStyle("-fx-opacity:1;-fx-alignment:  BASELINE_RIGHT;-fx-border-color: #2199dd; -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                        u.setBirthDate(LocalDate.parse(birthDate.getText()));
                    }
                } else if (!initBirthDate.equals(birthDate.getText())) {
                    hozza_adjam = false;
                    birthDate.setStyle("-fx-border-color:  rgb(220, 40, 40); -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                }

                if (u.getPerm() != permissionChoiceBox.getValue() && u.getPerm() != Permission.ADMIN) {
                    u.setPerm(permissionChoiceBox.getValue());
                } else if (u.getPerm() == Permission.ADMIN) {
                    is_admin = true;
                }

                if (UserPassChecker.currencyCheck(salary.getText())) {
                    if (Integer.parseInt(CurrencyManager.removeTextFieldPattern(salary.getText())) != u.getSallary()) {
                        u.setSallary(Integer.parseInt(CurrencyManager.removeTextFieldPattern(salary.getText())));
                    }
                } else {
                    hozza_adjam = false;
                    salary.setStyle("-fx-border-color:  rgb(220, 40, 40); -fx-border-width:  0px 0px 2px 0px;-fx-background-color:  transparent;-fx-text-fill: white;-fx-padding: 0");
                }
                u.setAddDate(LocalDate.now());
                userDAO.updateUser(u);
                if (!hozza_adjam){
                    PopupHandler.alertMsg = "A megadott adat(ok) nem felel(nek) meg a szükséges kritériumoknak!";
                    if (is_admin) {
                        PopupHandler.alertMsg += "\nBiztonsági okokból adminisztrátor nem változtathatja a saját jogosultságát!";
                    }
                    PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
                }


            }


        }


    }


    private void loader(String actuallyUser){

        UserDAO userDAO = new JPAUserDAO();

        accountChoiceBox.setValue(actuallyUser);
        for (User u : userDAO.getUser() ){
            if(u.getUsername().equals(actuallyUser)){
                if (u.getAddDate() == null) {
                    addDate.setText("<Empty>");
                } else {
                    addDate.setText(String.valueOf(u.getAddDate()));
                }

                if (u.getVezetekNev() == null) {
                    vezNev.clear();
                    vezNev.setPromptText("<Empty>");
                } else {
                    vezNev.setText(u.getVezetekNev());
                }

                if (u.getKeresztNev() == null) {
                    kerNev.clear();
                    kerNev.setPromptText("<Empty>");
                } else {
                    kerNev.setText(u.getKeresztNev());
                }

                if (u.getEmail() == null) {
                    email.clear();
                    email.setPromptText("<Empty>");
                } else {
                    email.setText(u.getEmail());
                }

                userName.setText(u.getUsername());

                if (u.getBirthDate() == null) {
                    birthDate.clear();
                    birthDate.setPromptText("YYYY-MM-DD");
                } else {
                    birthDate.setText(String.valueOf(u.getBirthDate()));
                }

                initVeznev = vezNev.getText();
                initKernev = kerNev.getText();
                initEmail = email.getText();
                initBirthDate = birthDate.getText();

                salary.setText(u.getSallary() == 0 ? "0 Ft":CurrencyManager.createPattern(String.valueOf(u.getSallary())));
                regDate.setText(String.valueOf(u.getregDate()));
                addDate.setText(u.getAddDate() == null ?String.valueOf(LocalDate.now()): String.valueOf(u.getAddDate()));
                switch (u.getPerm()){
                    case ADMIN:
                        permissionChoiceBox.setValue(Permission.ADMIN);
                        break;
                    case ALKALMAZOTT:
                        permissionChoiceBox.setValue(Permission.ALKALMAZOTT);
                        break;
                    case DEFAULT:
                        permissionChoiceBox.setValue(Permission.DEFAULT);
                }
            }
        }

    }
}

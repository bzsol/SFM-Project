package hu.sfm.controller;

import hu.sfm.entity.Bevetel;
import hu.sfm.entity.Product;
import hu.sfm.main.Main;
import hu.sfm.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class CartController {
    @FXML
    private Button completeBtn;

    @FXML
    private Button continueBtn;

    @FXML
    private TextField totalPrice;

    @FXML
    private VBox cartVbox;

    @FXML
    private void initialize() {
        totalPrice.setText("0 Ft");

        setupCart();
    }

    @FXML
    private void onActionPurchaseComplete(ActionEvent event) {
        ProductDAO productDAO = new JPAProductDAO();
        BevetelDAO bevetelDAO=new JPABevetelDAO();

        for(Bevetel b : bevetelDAO.getBevetelek()){
            if(b.getKasszaZaras()==null){
                b.setOsszeg(b.getOsszeg()+Integer.parseInt(CurrencyManager.removeTextFieldPattern(totalPrice.getText())));
                bevetelDAO.updateBevetel(b);
            }
        }
        for(var ac : Main.actualCart.entrySet()){
            for(Product p : productDAO.getProducts() ){
                if(p.getName().equals(ac.getKey())){
                    p.setQuantity(p.getQuantity()-ac.getValue());
                    productDAO.updateProduct(p);
                }
            }
        }

        onActionPurchaseDecline(event);
        Stage stage = (Stage) completeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onActionPurchaseContinue(ActionEvent event) {
        Stage stage = (Stage) continueBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onActionPurchaseDecline(ActionEvent event) {
        if (Main.actualCart.size() > 0) {
            PopupHandler.alertMsg = "Biztos vagy benne, hogy az összes elemet el szeretnéd távolítani a kosárból?";
            PopupHandler.showAlert(PopupHandler.Type.ALERT);
            if (PopupHandler.resultType == PopupHandler.Result.ACCEPTED) {
                Main.actualCart.clear();
                cartVbox.getChildren().clear();
                emptyCartSetup();
            }
        } else {
            PopupHandler.alertMsg = "Egyetlen termék sincs a kosárban, amit el lehetne távolítani!";
            PopupHandler.showAlert(PopupHandler.Type.NOTIFICATION);
        }
    }

    private void setupCart() {
        if (Main.actualCart.size() == 0) {
            emptyCartSetup();
        } else {

            int sorszam = 0;
            String itemprice = "0";
            ProductDAO productDAO = new JPAProductDAO();
            int vegosszeg = 0;

            for (Map.Entry<String, Integer> product : Main.actualCart.entrySet()) {

                for (Product p : productDAO.getProducts()) {
                    if (p.getName().equals(product.getKey())) {
                        itemprice = String.valueOf(p.getPrice());

                    }
                }
                vegosszeg += Integer.parseInt(itemprice) * product.getValue();

                HBox productLine = new HBox();
                productLine.setId(product.getKey());
                productLine.setMinHeight(50);
                productLine.setPrefWidth(800);
                if (sorszam % 2 == 0) {
                    productLine.setStyle("-fx-background-color: rgba(86, 86, 86, .8)");
                } else {
                    productLine.setStyle("-fx-background-color: rgba(132, 132, 132, .8)");
                }
                Label l1 = new Label(product.getValue() + "x");
                l1.setStyle("-fx-min-width: 100px; -fx-font-family: Segoe UI; -fx-text-fill: white; -fx-font-size: 16px; -fx-alignment: BASELINE_CENTER; -fx-label-padding: 14px");
                Label l2 = new Label(product.getKey());
                l2.setStyle("-fx-min-width: 155px; -fx-font-family: Segoe UI; -fx-text-fill: white; -fx-font-size: 16px; -fx-alignment: BASELINE_CENTER; -fx-label-padding: 14px");
                Label l3 = new Label(CurrencyManager.createPattern(String.valueOf(Integer.parseInt(itemprice) * product.getValue())));
                l3.setStyle("-fx-min-width: 310px; -fx-font-family: Segoe UI; -fx-text-fill: white; -fx-font-size: 16px; -fx-alignment: BASELINE_CENTER; -fx-label-padding: 14px");
                Button removeBtn = new Button("Eltávolítás");
                removeBtn.setId(product.getKey());
                removeBtn.setOnAction(event ->
                {
                    Main.actualCart.remove(((Button) event.getSource()).getId());
                    cartVbox.getChildren().clear();
                    setupCart();
                });
                productLine.getChildren().addAll(l1, l2, l3, removeBtn);
                productLine.setMargin(removeBtn, new Insets(10, 0, 0, 95));
                cartVbox.getChildren().add(productLine);
                sorszam++;
            }
            totalPrice.setText(CurrencyManager.createPattern(String.valueOf(vegosszeg)));
        }
    }

    private void emptyCartSetup() {
        Label zeroItemLabel = new Label("Jelenleg egyetlen termék sincs a kosárban");
        zeroItemLabel.setStyle("-fx-font-family: Segoe UI; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-label-padding: 125px 0px 0px 202.5px");
        cartVbox.getChildren().add(zeroItemLabel);
        totalPrice.setText(CurrencyManager.createPattern("0"));
    }
}

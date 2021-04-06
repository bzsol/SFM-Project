package hu.sfm.main;

import hu.sfm.controller.DashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.h2.tools.Server;

import javax.tools.Tool;
import java.io.IOException;




public class Main extends Application {
        private static Scene scene;
        public static int sorszam;
        public static Pane actualPane;
        public static Pane mainBuyMenuPane;
        public static boolean hasGroup = false;

        @Override
        public void start(Stage stage) throws Exception {
            new Server().runTool("-tcp", "-web", "-ifNotExists");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));

            stage.setOnCloseRequest(e->{
                e.consume();
                System.out.println("The application closed, the database is closed too!");
                System.exit(0);

            });



            scene = new Scene(root);
            stage.setTitle("Vezérlőpult");
            stage.setScene(scene);
            stage.show();
        }

       public static void setRoot(String fxml) throws IOException {
            scene.setRoot(loadFXML(fxml));
            Stage stage = (Stage) scene.getWindow();
            if (fxml.equals("loginpanel")) {
                stage.setTitle("Login");
            } else if (fxml.equals("registrationpanel")) {
                stage.setTitle("Registration");
            }
        }

        private static Parent loadFXML(String fxml) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        }

        public static Label createErrorLabel(String title, String tooltipMessage) {
            Label label = new Label(title);
            label.setStyle("-fx-border-width: 2px; -fx-border-color: red; -fx-border-radius: 50%; -fx-font-family: Segoe UI;" +
                    " -fx-font-size: 14px; -fx-text-fill: white; -fx-alignment: center; -fx-padding: 0; -fx-font-family: Segoe UI;");
            label.setMinWidth(287);
            Tooltip tooltip = new Tooltip(tooltipMessage);
            tooltip.setStyle("-fx-text-fill: red; -fx-background-color: white; -fx-font-size: 12px");
            tooltip.setShowDelay(Duration.seconds(0.1));
            tooltip.setShowDuration(Duration.seconds(30));
            label.setTooltip(tooltip);
            return label;
        }

        public static void main(String[] args) {
            launch();
        }

    }

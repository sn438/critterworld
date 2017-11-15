package gui;

import java.net.URL;
import java.util.Optional;

import javax.swing.text.html.Option;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
	private static int sessionId;
	//private static LoginInfo loginInfo;
	
	public static void main(String[] args) {
		//sessionId = Integer.parseInt(args[0]);
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		/*
		login();
		//LoginInfo loginInfo = login();
		System.out.println(loginInfo);
		if (loginInfo != null) {
			System.out.println(loginInfo.readPassword + " " + loginInfo.writePassword + " " + 
		loginInfo.adminPassword);
		}
		*/
		stage.setMinHeight(800);
		stage.setMinWidth(1000);

		try {
			URL r = getClass().getResource("gui.fxml");
			if (r == null)
				throw new Exception("No FXML resource found.");
			Scene scene = new Scene(FXMLLoader.load(r));
			stage.setTitle("CRITTERWORLD!");
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
			
		} catch (Exception e) {
			
		}
	}

	public static int getSessionId() {
		return sessionId;
	}
	/*
	private void login() {
		LoginInfo login = null;
		Dialog<LoginInfo> dialog = new Dialog<>();
        dialog.setTitle("Login Info");
        dialog.setHeaderText("Please Enter In The Passwords You Have Access To");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField readPasswordTextField = new TextField("Read Password");
        TextField writePasswordTextField = new TextField("Write Password");
        TextField adminPasswordTextField = new TextField("Admin Password");
        dialogPane.setContent(new VBox(8, readPasswordTextField, writePasswordTextField, adminPasswordTextField));
        Platform.runLater(readPasswordTextField::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
           
        	if (button == ButtonType.OK) {
                return new LoginInfo(readPasswordTextField.getText(),
                		writePasswordTextField.getText(), adminPasswordTextField.getText());
            }
            return null;
        });
        
        Optional<LoginInfo> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((LoginInfo results) -> {
           loginInfo = new LoginInfo(results.readPassword, results.writePassword, results.adminPassword);
        });
	}
	
	class LoginInfo {

        String readPassword;
        String writePassword;
        String adminPassword;

        private LoginInfo(String readPassword, String writePassword, String adminPassword) {
            this.readPassword = readPassword;
            this.writePassword = writePassword;
            this.adminPassword = adminPassword;
        }
    }
    */
}

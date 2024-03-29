/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.Messenger;
import edu.duke.ece651.risc.shared.Utils.InitialUnitNumHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.net.URL;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import edu.duke.ece651.risc.controller.LoginController;

public class Client extends Application{
//public class Client{
	public static Messenger messenger;
	public static PrintStream out;
	public static BufferedReader bufferedReader;
	public static Player current_player;

	@Override
	public void start(Stage stage) throws IOException, ClassNotFoundException {
		out = System.out;
		messenger = new Messenger("127.0.0.1", 12345);
		Reader inputSource1 = new InputStreamReader(System.in);
		bufferedReader = new BufferedReader(inputSource1);
        messenger.send("");
        current_player = new Player("", messenger, bufferedReader, out,
				InitialUnitNumHelper.initialUnitNum.get("soldier"));

		URL xmlResource = getClass().getResource("/ui/login.fxml");		
		FXMLLoader loader = new FXMLLoader(xmlResource);

		HashMap<Class<?>,Object> controllers = new HashMap<>();
		controllers.put(LoginController.class, new LoginController(current_player));
		loader.setControllerFactory((c) -> {
			return controllers.get(c);
		});
	
		AnchorPane gp = loader.load();

		Scene scene = new Scene(gp, 1280, 800);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		launch(args);
		// out = System.out;
		// if (args.length != 1) {
		// 	out.println("There should be 1 arguments, which is your username");
		// 	return;
		// }
		// String username = args[0];
		// out.println("Waiting to connect to the server");
		// messenger = new Messenger("127.0.0.1", 12345);
		// out.println("Connected to the server and send your name to server");
		// messenger.send(username);
		// Reader inputSource1 = new InputStreamReader(System.in);
		// bufferedReader = new BufferedReader(inputSource1);
		// current_player = new Player(username, messenger, bufferedReader, out,
		// 		InitialUnitNumHelper.initialUnitNum.get("soldier"));
				
		// current_player.doLoginSignup();
		// current_player.doMenuPhase();
	}
}

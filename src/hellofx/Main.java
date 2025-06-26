package hellofx;

import hellofx.Pages.HomePage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       
        primaryStage.setTitle("DSA Visualizer");
        Scene scene = new Scene(HomePage.getView(primaryStage),1000,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

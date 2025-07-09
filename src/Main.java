
import Pages.HomePage;
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
        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        Scene scene = new Scene(HomePage.getView(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


import Pages.*;
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
        primaryStage.getIcons().add(new javafx.scene.image.Image(Main.class.getResourceAsStream("/Images/" + "icon" + ".png")));
        primaryStage.setMaximized(true);
        Scene scene = new Scene(HomePageUp.getView(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

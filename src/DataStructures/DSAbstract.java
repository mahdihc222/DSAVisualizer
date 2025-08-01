package DataStructures;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import Pages.VisualPage;
import javafx.scene.Node;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

abstract public class DSAbstract<T> {
    protected List<Node> Controls = new ArrayList<>();
    protected List<T> dataNodes = new ArrayList<>();
    protected int startingX, startingY;

    abstract protected void showCode();
    abstract protected void initializeControls();

    abstract protected void addNode(int val);

    abstract protected void removeLastNode();


    protected void clear(){
        dataNodes.clear();
        VisualPage.getAnimationPane().getChildren().clear();

    }

    final protected TextArea getCodeTextArea(String title){
        String code = "Code not found";
        try{
            InputStream in = DSAbstract.class.getResourceAsStream("/code/" + title + ".txt");
            code = new String(in.readAllBytes(), StandardCharsets.UTF_8);


        }
        catch(Exception e){
            e.printStackTrace();
        }
        TextArea codeBox = new TextArea(code);
        codeBox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8;-fx-background-radius: 8;");
        codeBox.setFont(Font.font("Consolas", 14));
        codeBox.setEditable(false);
        codeBox.setWrapText(true);
        return codeBox;
    }

}

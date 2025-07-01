package Helpers;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CircularNode extends Group {
    private Circle boundary;
    private Text text;

    public CircularNode(int elem,int centerX, int centerY, int ind){
        
        
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font("Arial",12));
        text.setX(centerX - text.getLayoutBounds().getWidth()/2);
        text.setY(centerY + text.getLayoutBounds().getHeight()/4);

        boundary = new Circle(centerX, centerY, String.valueOf(elem).length()/2 +10);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);

        this.getChildren().addAll(boundary,text);
        
    }

    public int getElement(){ return Integer.parseInt(text.getText());}
    public void setElement(Integer x){
        text.setText(x.toString());
    }
}

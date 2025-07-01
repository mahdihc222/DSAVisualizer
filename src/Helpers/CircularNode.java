package Helpers;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


//#dont use, DEPRICATED
public class CircularNode extends Group {
    private Circle boundary;
    private Text text;

    public CircularNode(int elem,int centerX, int centerY){
        

        boundary = new Circle(centerX, centerY, String.valueOf(elem).length()/2 +10);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);

        text = new Text(String.valueOf(elem));
        text.setFont(Font.font("Arial",12));
        updateTextPosition();

        this.getChildren().addAll(boundary,text);
        
    }

    public void setLocation(int x, int y){
        boundary.setCenterX(x);
        boundary.setCenterY(y);
        updateTextPosition();
    }

    public void exchangeValueWith(CircularNode other){
        String temp = this.text.getText();
        this.text.setText(other.text.getText());
        other.text.setText(temp);
    }

    private void updateTextPosition(){
        text.setX(boundary.getCenterX() - text.getLayoutBounds().getWidth()/2);
        text.setY(boundary.getCenterY() + text.getLayoutBounds().getHeight()/4);
    }

    public boolean isLessThan(CircularNode other){
        return this.getElement()<other.getElement();
    }

    public int getX(){ return (int)boundary.getCenterX();}
    public int getY(){ return (int)boundary.getCenterY();}
    private int getElement(){ return Integer.parseInt(text.getText());}
    // public void setElement(Integer x){
    //     text.setText(x.toString());
    // }
}

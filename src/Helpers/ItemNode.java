package Helpers;

import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

//ItemNode class that will hold and display data members
//Dont use other nodes, use this node class and specify type in constructor
public class ItemNode extends Group {
    private Shape boundary;
    private Text text; 
    private Text index = null;
    private ItemNode prev = null;
    @SuppressWarnings("unused") // redundant warning, needed later
    private boolean isRectangle = true;
    private int elem;
    private Line connectLine = null;
    static final int NODEHEIGHT = 40;
    static final int NODEWIDTH = 40;
    static final int NODERADIUS = 20;
    private final int TEXTSIZE = 12; // final int to hold textsize
    private final String FONTNAME = "Arial"; // final string to hold fontname

    public ItemNode(String s, int nodeX, int nodeY) {
        text = new Text(s); text.setFont(Font.font(FONTNAME, FontWeight.BOLD, TEXTSIZE));
        boundary = new Rectangle(nodeX, nodeY, NODEWIDTH, NODEHEIGHT);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        updateTextPosition();

        this.getChildren().addAll(boundary, text);
    }

    public ItemNode(String s, int nodeX, int nodeY, int ind) {
        text = new Text(s); text.setFont(Font.font(FONTNAME, FontWeight.BOLD, TEXTSIZE));
        boundary = new Rectangle(nodeX, nodeY, NODEWIDTH, NODEHEIGHT);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        updateTextPosition();

        index = new Text(String.valueOf(ind));
        index.setX(nodeX + boundary.getLayoutBounds().getWidth() / 2 - index.getLayoutBounds().getWidth());
        index.setY(getY() + NODEHEIGHT + 15);
        index.setFill(Color.GRAY);
        index.setFont(Font.font(FONTNAME, TEXTSIZE));

        this.getChildren().addAll(boundary, text, index);
    }

    // constructor to create rectangular nodes with index
    public ItemNode(int elem, int nodeX, int nodeY, int ind) {
        this.elem = elem;
        boundary = new Rectangle(nodeX, nodeY, NODEHEIGHT, NODEWIDTH);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        text = new Text(String.valueOf(elem)); 
        text.setFont(Font.font(FONTNAME, FontWeight.BOLD, TEXTSIZE));
        updateTextPosition();

        index = new Text(String.valueOf(ind));
        index.setX(nodeX + boundary.getLayoutBounds().getWidth() / 2 - index.getLayoutBounds().getWidth());
        index.setY(getY() + NODEHEIGHT + 15);
        index.setFill(Color.GRAY);
        index.setFont(Font.font(FONTNAME, TEXTSIZE));

        this.getChildren().addAll(boundary, text, index);

    }

    // constructor to create rectangular nodes without index
    public ItemNode(int elem, int nodeX, int nodeY) {
        this.elem = elem;
        boundary = new Rectangle(nodeX, nodeY, NODEWIDTH, NODEHEIGHT);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        text = new Text(String.valueOf(elem)); 
        text.setFont(Font.font(FONTNAME, FontWeight.BOLD, TEXTSIZE));
        updateTextPosition();

        this.getChildren().addAll(boundary, text);
    }


    // this constructor is used to construct circular nodes, which always needs prev
    // link(except root)
    public ItemNode(int elem, int centerX, int centerY, boolean isRectangle, ItemNode prev) {
        this.elem = elem;
        this.isRectangle = isRectangle;
        this.prev = prev;
        boundary = new Circle(centerX, centerY, NODERADIUS);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);

        text = new Text(String.valueOf(elem)); 
        text.setFont(Font.font(FONTNAME, FontWeight.BOLD, TEXTSIZE));

        updateTextPosition();

        if (this.prev != null) {
            connectLine = getConnectingLine();
            this.getChildren().add(connectLine);
        }

        this.getChildren().addAll(boundary, text);
    }

    public ItemNode(int elem, int centerX, int centerY, boolean isRectangle) {
        this.elem = elem;
        this.isRectangle = isRectangle;
        boundary = new Circle(centerX, centerY, NODERADIUS);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font(FONTNAME, FontWeight.BOLD, TEXTSIZE));
        updateTextPosition();
        this.getChildren().addAll(boundary, text);
    }

    // this updateTextPosition places text at proper place
    private void updateTextPosition() {
        if (boundary instanceof Circle) {
            text.setX(getX() - text.getLayoutBounds().getWidth() / 2);
            text.setY(getY() + text.getLayoutBounds().getHeight() / 4);
        } else {
            text.setX(getX() + NODEWIDTH / 2 - text.getLayoutBounds().getWidth() / 2);
            text.setY(getY() + NODEHEIGHT / 2 + text.getLayoutBounds().getHeight() / 4);
        }
    }

    private void updateIndexPosition() {
        if (index != null) {
            index.setX(((Rectangle) boundary).getX() + boundary.getLayoutBounds().getWidth() / 2
                    - index.getLayoutBounds().getWidth());
            index.setY(getY() + NODEHEIGHT + 15);
        }
    }

    public double getX() {
        if (boundary instanceof Circle)
            return ((Circle) boundary).getCenterX();
        if (boundary instanceof Rectangle)
            return ((Rectangle) boundary).getX();
        return -1;
    }

    public double getY() {
        if (boundary instanceof Circle)
            return ((Circle) boundary).getCenterY();
        if (boundary instanceof Rectangle)
            return ((Rectangle) boundary).getY();
        return -1;
    }

    // function to compare if current Node is less than the passed node
    public boolean isLessThan(ItemNode other) {
        return this.getElement() < other.getElement();
    }

    // function to set or change the location of a node
    // not tested yet
    public void setLocation(double x, double y) {
        if (boundary instanceof Circle) {
            Circle boun = (Circle) boundary;
            boun.setCenterX(x);
            boun.setCenterY(y);
        } else {
            ((Rectangle) boundary).setX(x);
            ((Rectangle) boundary).setY(y);
        }
        updateTextPosition();
        updateIndexPosition();
    }

    // function to exchange only value of two nodes
    // nodes will remain in place
    public void exchangeValueWith(ItemNode other) {
        //String temp = this.text.getText();
        int tempint = this.getElement();
       // this.text.setText(other.text.getText());
        this.setElement(other.getElement());
        //other.text.setText(temp);
        other.setElement(tempint);
        this.updateTextPosition();
        other.updateTextPosition();
    }

    public void setIndex(int ind) {
        index.setText(String.valueOf(ind));
    }

    public void flash(Color flashColor) {
        boundary.setFill(flashColor);
        PauseTransition pause = new PauseTransition(Duration.seconds(1.0)); // green for 1s
        pause.setOnFinished(e -> boundary.setFill(Color.WHITE));
        pause.play();
    }

    public void flashText(Color flashColor) {
        text.setFill(flashColor);
        PauseTransition pause = new PauseTransition(Duration.seconds(2.0)); // green for 1s
        pause.setOnFinished(e -> text.setFill(Color.BLACK));
        pause.play();
    }

    public void setTextColor(Color newColor) {
        text.setFill(newColor);
    }

    public void setNodeColor(Color color) {
        boundary.setFill(color);
    }

    // Returns a Line connecting this node to its previous node.

    private Line getConnectingLine() {
        if (this.prev == null)
            return null;

        if (boundary instanceof Circle && prev.boundary instanceof Circle) {
            // Calculate direction vector
            double x1 = prev.getX();
            double y1 = prev.getY();
            double x2 = getX();
            double y2 = getY();
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double r = NODERADIUS;

            if (dist != 0) {
                double startX = x1 + dx * r / dist;
                double startY = y1 + dy * r / dist;
                double endX = x2 - dx * r / dist;
                double endY = y2 - dy * r / dist;
                return new Line(startX, startY, endX, endY);
            } else {
                return new Line(x1, y1, x2, y2);
            }
        } else if (boundary instanceof Rectangle && prev.boundary instanceof Rectangle) {
            // Connect midpoints of right side of prev to left side of current
            Rectangle prevRect = (Rectangle) prev.boundary;
            Rectangle curRect = (Rectangle) boundary;
            double prevMidY = prevRect.getY() + prevRect.getHeight() / 2;
            double curMidY = curRect.getY() + curRect.getHeight() / 2;
            double prevRightX = prevRect.getX() + prevRect.getWidth();
            double curLeftX = curRect.getX();
            return new Line(prevRightX, prevMidY, curLeftX, curMidY);
        } else {
            // Fallback: connect centers
            return new Line(prev.getX(), prev.getY(), getX(), getY());
        }
    }

    public static int getNodeRadius() {
        return NODERADIUS;
    }

    public static int getNodeHeight() {
        return NODEHEIGHT;
    }

    // public int hashCode() {
    //     int x = Integer.parseInt(text.getText());
    //     return Objects.hash(x);
    // }

    public void highlight() {

        if(boundary instanceof Circle){
            boundary.setStrokeWidth(6);
        }
        else boundary.setStrokeWidth(3);
    }

    public void unHighlight() {
        boundary.setStrokeWidth(1);
    }

    public void setElement(int a) {
        elem = a;
        text.setText(String.valueOf(elem));
        updateTextPosition();
    }

    public void setText(String s) {
        text.setText(s);
        updateTextPosition();
    }

    public int getElement() {
        return elem;
    }

    public Line getLine(){
        return connectLine;
    }

    public void setLine(Line l){
        connectLine = l;
    }



    public void setNodeLayout(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

}

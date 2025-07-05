package DataStructures;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;

abstract public class DSAbstract<T> {
    protected List<Node> Controls = new ArrayList<>();
    protected List<T> dataNodes = new ArrayList<>();
    protected int startingX, startingY;
    abstract public String getCode();
    //abstract public List<T> getAnimationNodes();
    //abstract public List<Node> getControlNodes();
    abstract protected void initializeControls();
    abstract protected void addNode(int val);
    abstract protected void removeLastNode();
}

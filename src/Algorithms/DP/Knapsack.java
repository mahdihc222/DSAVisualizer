package Algorithms.DP;

import java.util.ArrayList;
import java.util.List;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.scene.control.Tab;

public class Knapsack extends DSAbstract<ItemNode> {
    List<Integer> weights = new ArrayList<>();
    List<ItemNode> weightNodes = new  ArrayList<>();
    List<Integer> costs = new ArrayList<>();
    List<ItemNode> costNodes = new ArrayList<>();

    public Knapsack() {
        initializeControls();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        showCode();
    }

    
 
    @Override
    protected void addNode(int val) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void initializeControls() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void showCode() {
        // TODO Auto-generated method stub
        Tab problemTab = new Tab("Problem", getCodeTextArea("Problems/Knapsack"));
        Tab codeTab = new Tab("Code", getCodeTextArea("Knapsack"));
        VisualPage.getCodePane().getTabs().addAll(problemTab, codeTab);
        VisualPage.getCodePane().getTabs().forEach(tab -> tab.setClosable(false));
    }
    

}
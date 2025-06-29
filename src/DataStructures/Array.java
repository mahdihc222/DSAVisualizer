package DataStructures;

import java.util.ArrayList;
import java.util.List;

import Helpers.ListNode;

public class Array {
    private List<ListNode> Nodes = new ArrayList<>();
    private int startingX, startingY;

    Array(int x, int y) {
        startingX = x;
        startingY = y;
    }

    public Array(int x, int y, List<Integer> ls) {
        startingX = x;
        startingY = y;
        for (Integer curr : ls) {
            Nodes.add(new ListNode(curr, startingX + 20 * Nodes.size(), startingY, Nodes.size()));
        }
    }

    public List<ListNode> getVisibleArray() {
        return Nodes;
    }

    public void insert(int elem) {
        Nodes.add(new ListNode(elem, startingX + 20 * Nodes.size(), startingY, Nodes.size()));
    }

    public void removeLast() {
        Nodes.removeLast();
    }

    // public void remove(int index){
    // Nodes.remove(index);
    // }

}

package de.techfak.gse.template.parsingUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Tree node.
 *
 * @param <T> the type parameter
 */
public class TreeNode<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private ArrayList<TreeNode<T>> children;

    /**
     * Instantiates a new Tree node.
     *
     * @param data the data
     */
    public TreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Gets children.
     *
     * @return the children
     */
    public List<TreeNode<T>> getChildren() {
        return children;
    }

    /**
     * Add child.
     *
     * @param child the child
     */
    public void addChild(TreeNode<T> child) {
        children.add(child);
    }

    /**
     * Print tree.
     */
    public void printTree() {
        printTree("", true);
    }

    private void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + data);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (!children.isEmpty()) {
            children.getLast()
                    .printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    /**
     * Has other data then root or Inhaltsverzeichnis boolean.
     *
     * @return the boolean
     */
    public boolean hasOtherData() {
        // Check if current node's data is NOT "root" or "Inhaltsverzeichnis"
        if (!"root".equals(data) && !"Inhaltsverzeichnis".equals(data)) {
            return true;
        }
        if (children != null) {
            for (TreeNode<T> child : children) {
                if (child.hasOtherData()) {
                    return true;
                }
            }
        }
        return false;
    }
}
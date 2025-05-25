package de.techfak.gse.template.parsingutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Tree node.
 *
 * @param <T> the type parameter
 */
public class TreeNode<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private T data;
    private ArrayList<TreeNode<T>> children;
    private static final  Logger LOGGER = LoggerFactory.getLogger(TreeNode.class);

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
    public void logTree(String source) {
        logTree("", true, source);
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    private void logTree(String prefix, boolean isTail, String source) {
        LOGGER.debug("{}{}{}", prefix, isTail ? "└── " : "├── ", data);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).logTree(prefix + (isTail ? "    " : "│   "), false, source);
        }
        if (!children.isEmpty()) {
            children.getLast()
                    .logTree(prefix + (isTail ? "    " : "│   "), true, source);
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

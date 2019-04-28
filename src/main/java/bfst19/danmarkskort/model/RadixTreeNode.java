package bfst19.danmarkskort.model;

import java.util.HashMap;

/**
 * Doubly linked Radix Tree for strings. Case insensitive.
 */
public class RadixTreeNode {
    private String string;
    private RadixTreeNode parent;
    private HashMap<String, RadixTreeNode> children;
    private boolean isWord;

    public RadixTreeNode() {}

    private RadixTreeNode(String string, RadixTreeNode parent) {
        this.string = string;
        this.parent = parent;
    }

    public void insert(String word) {
        word = word.toLowerCase();
        RadixTreeNode candidate = null;
        for (int i = 0; i < word.length(); i++) {
            String subStr = word.substring(0, i + 1);
            RadixTreeNode temp = getChildStartingWith(subStr);
            if (temp != null) {
                candidate = temp;
            } else if (candidate != null) {
                String rest = word.substring(i);
                String prevSubStr = word.substring(0, i);
                if (prevSubStr.startsWith(candidate.string)) {
                    candidate.insert(rest);
                } else {
                    // Need to split node into new prefix, to accommodate children
                    RadixTreeNode newBranch = new RadixTreeNode(prevSubStr, this);
                    children.remove(candidate.string);
                    candidate.string = candidate.string.substring(i);
                    candidate.parent = newBranch;
                    newBranch.children = new HashMap<>();
                    RadixTreeNode newLeaf = new RadixTreeNode(rest, newBranch);
                    newLeaf.isWord = true;
                    newBranch.children.put(rest, newLeaf);
                    newBranch.children.put(candidate.string, candidate);
                    children.put(prevSubStr, newBranch);
                }
                return;
            }
        }
        if (candidate == null) {
            RadixTreeNode newLeaf = new RadixTreeNode(word, this);
            newLeaf.isWord = true;
            if (children == null) {
                children = new HashMap<>();
            }
            children.put(word, newLeaf);
        }
    }

    public RadixTreeNode search(String word) {
        word = word.toLowerCase();
        if (word.equals(string)) {
            if (isWord) {
                return this;
            } else {
                return null;
            }
        }
        if (string != null) {
            if (!word.startsWith(string)) {
                return null;
            }
            word = word.substring(string.length());
        }
        RadixTreeNode child = null;
        for (int i = 0; i < word.length(); i++) {
            String subStr = word.substring(0, i + 1);
            RadixTreeNode temp = getChildStartingWith(subStr);
            if (temp != null) {
                child = temp;
            } else {
                break;
            }
        }
        if (child == null) {
            return null;
        }
        return child.search(word);
    }

    private RadixTreeNode getChildStartingWith(String word) {
        if (children == null) {
            return null;
        }
        for (RadixTreeNode child : children.values()) {
            if (child.string.startsWith(word)) {
                return child;
            }
        }
        return null;
    }

    public String getString() {
        return string;
    }

    public RadixTreeNode getParent() {
        return parent;
    }

    public HashMap<String, RadixTreeNode> getChildren() {
        return children;
    }

    public boolean isWord() {
        return isWord;
    }
}

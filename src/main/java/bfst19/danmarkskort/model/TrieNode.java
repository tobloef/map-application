package bfst19.danmarkskort.model;

import java.util.HashMap;

public class TrieNode {
    private Character c;
    private TrieNode parent;
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {

    }

    private TrieNode(Character c, TrieNode parent) {
        this.c = c;
        this.parent = parent;
    }

    public void insert(String word) {
        word = word.toLowerCase();
        char c = word.charAt(0);
        if (children == null) {
            children = new HashMap<>();
        }
        if (!children.containsKey(c)) {
            TrieNode childNode = new TrieNode(c, this);
            children.put(c, childNode);
        }
        if (word.length() > 1) {
            String subStr = word.substring(1);
            children.get(c).insert(subStr);
        } else {
            isWord = true;
        }
    }

    public TrieNode search(String word) {
        word = word.toLowerCase();
        TrieNode node = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (node.children == null || !node.children.containsKey(c)) {
                return null;
            }
            node = node.children.get(c);
        }
        return node;
    }

    public Character getCharacter() {
        return c;
    }

    public TrieNode getParent() {
        return parent;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isWord() {
        return isWord;
    }
}

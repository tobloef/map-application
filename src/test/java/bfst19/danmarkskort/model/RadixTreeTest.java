package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RadixTreeTest {
    @Test
    public void testSingleWord() {
        RadixTreeNode tree = new RadixTreeNode();
        tree.insert("Test");
        assertNotNull(tree.search("Test"));
    }

    @Test
    public void testMultipleWords() {
        RadixTreeNode tree = new RadixTreeNode();
        tree.insert("Me");
        tree.insert("Meme");
        tree.insert("Memes");
        tree.insert("Men");
        tree.insert("Man");
        assertNotNull(tree.search("Me"));
        assertNotNull(tree.search("Meme"));
        assertNotNull(tree.search("Memes"));
        assertNotNull(tree.search("Men"));
        assertNotNull(tree.search("Man"));
    }
    @Test
    public void testWordNotFound() {
        RadixTreeNode tree = new RadixTreeNode();
        tree.insert("Me");
        tree.insert("Meme");
        assertNull(tree.search("Mem"));
    }
}

package bfst19.danmarkskort.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {
    @Test
    public void testSingleWord() {
        TrieNode trie = new TrieNode();
        trie.insert("Test");
        assertNotNull(trie.search("Test"));
    }

    @Test
    public void testMultipleWords() {
        TrieNode trie = new TrieNode();
        trie.insert("Me");
        trie.insert("Meme");
        trie.insert("Memes");
        trie.insert("Men");
        trie.insert("Man");
        assertNotNull(trie.search("Me"));
        assertNotNull(trie.search("Meme"));
        assertNotNull(trie.search("Memes"));
        assertNotNull(trie.search("Men"));
        assertNotNull(trie.search("Man"));
    }
    @Test
    public void testWordNotFound() {
        TrieNode trie = new TrieNode();
        trie.insert("Me");
        trie.insert("Meme");
        assertNull(trie.search("Mem"));
    }
}

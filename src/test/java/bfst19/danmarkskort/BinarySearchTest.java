package bfst19.danmarkskort;

import bfst19.danmarkskort.utils.BinarySearch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinarySearchTest {
    @Test
    public void testSearch_integerFound1() {
        List<Integer> list = new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        ));
        list.sort(Integer::compareTo);
        int index = BinarySearch.search(list, 8, Integer::compareTo);
        assertEquals(8, index);
    }

    @Test
    public void testSearch_integerFound2() {
        List<Integer> list = new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        ));
        list.sort(Integer::compareTo);
        int index = BinarySearch.search(list, 2, Integer::compareTo);
        assertEquals(2, index);
    }

    @Test
    public void testSearch_stringFound1() {
        List<String> list = new ArrayList<>(Arrays.asList(
                "One",
                "Two",
                "Three",
                "Four",
                "Five"
        ));
        list.sort(String::compareToIgnoreCase);
        String target = "Four";
        int index = BinarySearch.search(list, target, String::compareTo);
        assertEquals(list.indexOf(target), index);
    }

    @Test
    public void testSearch_stringFound2() {
        List<String> list = new ArrayList<>(Arrays.asList(
                "One",
                "Two",
                "Three",
                "Four",
                "Five"
        ));
        list.sort(String::compareToIgnoreCase);
        String target = "One";
        int index = BinarySearch.search(list, target, String::compareToIgnoreCase);
        assertEquals(list.indexOf(target), index);
    }

    @Test
    public void testSearch_stringFound3() {
        List<String> list = new ArrayList<>(Arrays.asList(
                "One",
                "Two",
                "Three",
                "Four",
                "Five"
        ));
        list.sort(String::compareToIgnoreCase);
        String target = "Five";
        int index = BinarySearch.search(list, target, String::compareToIgnoreCase);
        assertEquals(list.indexOf(target), index);
    }

    @Test
    public void testSearch_stringNotFound() {
        List<String> list = new ArrayList<>(Arrays.asList(
                "One",
                "Two",
                "Three",
                "Four",
                "Five"
        ));
        list.sort(String::compareToIgnoreCase);
        String target = "Six";
        int index = BinarySearch.search(list, target, String::compareToIgnoreCase);
        assertEquals(list.indexOf(target), index);
    }
}

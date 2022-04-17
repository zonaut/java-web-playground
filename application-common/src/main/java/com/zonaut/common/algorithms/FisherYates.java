package com.zonaut.common.algorithms;

import java.util.Arrays;
import java.util.Random;

// https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
// https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
// Random permutation of array elements ('shuffle a deck of cards' or 'randomize a given array')
// Given an integer array, in-place shuffle it.
// The algorithm should produce an unbiased permutation, i.e., every permutation is equally likely.
public class FisherYates {

    public static void main(String[] args) {
        int[] arrayOfIntegers = {1, 2, 3, 4, 5, 6, 7, 8};

        example1(arrayOfIntegers, arrayOfIntegers.length);
        System.out.println(Arrays.toString(arrayOfIntegers));

        example2(arrayOfIntegers);
        System.out.println(Arrays.toString(arrayOfIntegers));

        example3(arrayOfIntegers);
        System.out.println(Arrays.toString(arrayOfIntegers));

        example4(arrayOfIntegers);
        System.out.println(Arrays.toString(arrayOfIntegers));
    }

    public static void example1(int[] arr, int n) {
        // Creating a object for Random class
        Random r = new Random();

        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = n - 1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = r.nextInt(i + 1);

            // Swap arr[i] with the element at random index
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static void example2(int[] array) {
        int n = array.length;
        Random random = new Random();
        // Loop over array.
        for (int i = 0; i < array.length; i++) {
            // Get a random index of the array past the current index.
            // ... The argument is an exclusive bound.
            //     It will not go past the array's end.
            int randomValue = i + random.nextInt(n - i);
            // Swap the random element with the present element.
            int randomElement = array[randomValue];
            array[randomValue] = array[i];
            array[i] = randomElement;
        }
    }

    // Utility function to swap elements `array[i]` and `array[j]` in the array
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Function to shuffle an array `array[]`
    public static void example3(int[] array) {
        // read array from the highest index to lowest
        for (int i = array.length - 1; i >= 1; i--) {
            Random rand = new Random();

            // generate a random number `j` such that `0 <= j <= i`
            int j = rand.nextInt(i + 1);

            // swap the current element with the randomly generated index
            swap(array, i, j);
        }
    }

    public static void example4(int[] array) {
        // read array from the lowest index to highest
        for (int i = 0; i <= array.length - 2; i++) {
            Random rand = new Random();

            // generate a random number `j` such that `i <= j < n`
            int j = i + rand.nextInt(array.length - i);

            // swap the current element with the randomly generated index
            swap(array, i, j);
        }
    }


}

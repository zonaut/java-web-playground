package com.zonaut.braindump.data_structures;

import com.google.common.hash.Funnels;

import java.util.HashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

// https://en.wikipedia.org/wiki/Bloom_filter
// Bloom filter is a probabilistic data structure that works on hash-coding methods (similar to HashTable).
public class BloomFilter {

    // Other resources
    // https://www.geeksforgeeks.org/bloom-filter-in-java-with-examples/
    // https://www.javamex.com/tutorials/collections/bloom_filter_java.shtml
    // https://github.com/sangupta/bloomfilter
    // https://www.baeldung.com/guava-bloom-filter
    // https://llimllib.github.io/bloomfilter-tutorial/
    // https://brilliant.org/wiki/bloom-filter/
    // https://redis.com/redis-best-practices/bloom-filter-pattern/

    // Imagine we want to check if a username already exists when creating an account.
    // Instead of doing database lookups we can use this filter to see if a username 'might' already exist.
    // Bloom filters can return false positives.
    //  - False positive means, it might tell you a given username already exists, but it's possible it's not.
    public static void main(String[] args) {
        //exampleWithoutBloomFilter();
        exampleWithGuavaBloomFilter();

        // there is also a counting blooming variant where you can remove entries
    }

    private static void exampleWithGuavaBloomFilter() {
        com.google.common.hash.BloomFilter<String> usernames = com.google.common.hash.BloomFilter
                .create(Funnels.stringFunnel(UTF_8), 100);
        usernames.put("user-1");
        usernames.put("user-2");
        usernames.put("user-3");

        System.out.println(usernames.mightContain("user-1")); // true
        System.out.println(usernames.mightContain("user-999")); // false
    }

    // HashSet or HashTable works well when we have limited data set, but might not fit as we move with a large data set.
    // With a large data set, it takes a lot of time to insert and uses a lot of memory.
    private static void exampleWithoutBloomFilter() {
        Set<String> usernames = new HashSet<>();
        usernames.add("user-1");
        usernames.add("user-2");
        usernames.add("user-3");

        System.out.println(usernames.contains("user-1")); // true
        System.out.println(usernames.contains("user-999")); // false
    }

}

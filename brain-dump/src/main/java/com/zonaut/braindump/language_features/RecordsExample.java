package com.zonaut.braindump.language_features;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RecordsExample {

    record Point(int x, int y) {
    }

    record Purchase(int invoiceNumber, double salesAmount, double salesTax) {
        static double taxRate = 0.075; // must be declared as static
    }

    private Set<Point> aPrivateMethodWithALocalRecord(List<Point> points) {
        record PeopleWithOrders(Point point, List<Purchase> orders) {
        }

        // do some logic...
        return Collections.emptySet();
    }

    // Calculate min and max value for some collection
    record MinMax<T>(T min, T max) {
    }
//    static<T> MinMax<T> calculateMinMax(Iterable<? extends T> elements,
//                               Comparator<? super T> comparator) {
//        return ...
//    }

    public static void main(String[] args) {
        Point p1 = new Point(10, 20);
        System.out.println(p1.x());         // 10
        System.out.println(p1.y());         // 20

        Point p2 = new Point(11, 22);
        System.out.println(p2.x());         // 11
        System.out.println(p2.y());         // 22

        Point p3 = new Point(10, 20);
        System.out.println(p3.x());         // 10
        System.out.println(p3.y());         // 20

        System.out.println(p1.hashCode());  // 330
        System.out.println(p2.hashCode());  // 363
        System.out.println(p3.hashCode());  // 330

        System.out.println(p1.equals(p2));  // false
        System.out.println(p1.equals(p3));  // true
        System.out.println(p2.equals(p3));  // false
    }

}

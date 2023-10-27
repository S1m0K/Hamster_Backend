package com.example.hamster_backend.model;

import java.util.ArrayList;

public class QuickSort {
    public static ArrayList<Comparable> sort(ArrayList<Comparable> a) {
        return quickSort(a,0,a.size()-1);
    }
    static ArrayList<Comparable> quickSort(ArrayList<Comparable> a, int left, int right)
    {
        int le, ri;
        Comparable pivot;
        le = left;
        ri = right; // Zwei Marken
        // Das Pivotelement
        pivot = a.get((le + ri) / 2);

        //printArr(a);
        while (le <= ri) // aeussere Schleife
        {
            while (a.get(le).compareTo(pivot)< 0)
                le++; // innere Schleife 1
            while (pivot.compareTo(a.get(ri)) < 0)
                ri--; //innere Schleife 2
            if (le <= ri)
            { // Tausch
                swap(a, le, ri);
                le++;
                ri--;
            }
        }
        // Jetzt haben sich die beiden Marken gekreuzt
        if (left < ri)
            return quickSort(a, left, ri);
        if (le < right)
            return quickSort(a, le, right);
        return a;
    } // quickSort

    private static void swap(ArrayList<Comparable> A, int i, int j)
    {
        Comparable temp = A.get(i);
        A.set(i, A.get(j));
        A.set(j, temp);
    }
    public String getSortAlgrithmName() {
        return "QuickSort";
    }
}

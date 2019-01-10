package com.danosoftware.galaxyforce.utilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Danny
 */

public class Reversed<T> implements Iterable<T> {
    private final List<T> original;

    private Reversed(List<T> original) {
        this.original = original;
    }

    @NonNull
    public Iterator<T> iterator() {
        final ListIterator<T> i = original.listIterator(original.size());

        return new Iterator<T>() {
            public boolean hasNext() {
                return i.hasPrevious();
            }

            public T next() {
                return i.previous();
            }

            public void remove() {
                i.remove();
            }
        };
    }

    public static <T> Reversed<T> reversed(List<T> original) {
        return new Reversed<>(original);
    }

}

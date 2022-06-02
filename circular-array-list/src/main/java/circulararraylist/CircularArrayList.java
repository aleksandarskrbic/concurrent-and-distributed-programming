package circulararraylist;

import java.util.*;

public class CircularArrayList<E> extends AbstractList<E> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private Object[] elements;
    private int size;
    private int head = 0;

    public CircularArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public CircularArrayList(final int initialCapacity) {
        elements = new Object[initialCapacity];
        size = 0;
    }

    public CircularArrayList(final Collection<? extends E> collection) {
        elements = collection.toArray();
        size = elements.length;
    }

    @Override
    public E get(final int index) {
        checkIndexRange(index);
        int realIndex = calculateRealIndex(index);
        return (E) elements[realIndex];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E set(final int index, final E element) {
        checkIndexRange(index);
        int realIndex = calculateRealIndex(index);
        final E old = (E) elements[realIndex];
        elements[realIndex] = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        ensureCapacity();
        int realIndex = calculateRealIndex(index);

        if (index != size) {
            if (index == 0) {
                moveHead(-1);
                realIndex = calculateRealIndex(index);
            } else if (realIndex > head && head != 0) {
                System.arraycopy(elements, head, element, head - 1, index);
                moveHead(-1);
                realIndex = calculateRealIndex(index);
            } else {
                System.arraycopy(elements, realIndex, elements, realIndex + 1, size - index);
            }
        }

        elements[realIndex] = element;
        size++;
        modCount++;
    }

    private void moveHead(final int steps) {
        head += steps;
        head %= elements.length;
        if (head < 0) head += elements.length;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            final Object[] newElements = new Object[(elements.length + 1) * 3/2]; // make it 50% larger
            System.arraycopy(elements, head, newElements, 0, elements.length - head);
            System.arraycopy(elements, 0, newElements,  elements.length - head, head);
            elements = newElements;
            head = 0;
        }
    }

    @Override
    public E remove(int index) {
        checkIndexRange(index);
        final E old = (E) elements[index];

        if (index != size - 1) {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        }

        elements[size - 1] = null;
        size--;
        modCount++;

        return old;
    }

    private void checkIndexRange(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int calculateRealIndex(final int index) {
        return (head + index) % elements.length;
    }
}

package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] array;
    private int size;
    private int head;
    private int tail;

    private boolean isFull()
    {
        return size == array.length;
    }

    public void resize(int NewSize)
    {
        T[] newArr = (T[]) new Object[NewSize];
        for (int i = 0; i < size; i++) {
            newArr[i] = array[(head + i) % array.length];
        }
        array = newArr;
        head = 0;
        tail = size;
    }


    public ArrayDeque()
    {
        array = (T[]) new Object[8];
        head = tail = 0;
        size = 0;
    }


    public int size()
    {
        return size;
    }


    public void addFirst(T item)
    {
        if(isFull()) resize(array.length * 2);
        head = (head - 1 + array.length) % array.length;
        array[head] = item;
        size++;
    }

    public void addLast(T item)
    {
        if(isFull()) resize(array.length * 2);
        array[tail] = item;
        tail = (tail + 1) % array.length;
        size++;
    }

    public T removeFirst()
    {
        if(isEmpty()) return null;
        T value = array[head];
        array[head] = null;
        head = (head + 1 + array.length) % array.length;
        size--;
        if (array.length >= 16 && size < array.length / 4) {
            resize(array.length / 2);
        }
        return value;
    }

    public T removeLast()
    {
        if(isEmpty()) return null;
        tail = (tail - 1 + array.length) % array.length;
        T value = array[tail];
        array[tail] = null;
        size--;
        if (array.length >= 16 && size < array.length / 4) {
            resize(array.length / 2);
        }
        return value;
    }

    public T get(int index)
    {
        if (isEmpty()) return null;
        if (index < 0 || index >= size) return null;
        return array[(head + index) % array.length];
    }

    public void printDeque()
    {
        int NowHead = head;
        for (int i = 0 ; i < size; i++)
        {
            System.out.print(array[NowHead] + " ");
            NowHead = (NowHead + 1 + array.length) % array.length;
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T>
    {
        int index;
        int seen;
        public ArrayIterator()
        {
            index = head;
            seen = 0;
        }
        @Override
        public boolean hasNext() {
            return seen < size;
        }
        @Override
        public T next()
        {
            if(!hasNext()) throw new NoSuchElementException();
            T value = array[index];
            index = (index + 1) % array.length;
            seen++;
            return value;
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ArrayDeque<?>)) return false;
        ArrayDeque<?> that = (ArrayDeque<?>) o;
        if (size != that.size()) return false;
        int NowHead = head;
        int OHead = that.head;
        for (int i = 0; i < size; i++)
        {
            if (!array[NowHead].equals(that.array[OHead])) return false;
            NowHead = (NowHead + 1 + array.length) % array.length;
            OHead = (OHead + 1 + that.array.length) % array.length;
        }
        return true;
    }
}


package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Iterable<T> {
    private T[] array;
    private int size;
    private int head;
    private int tail;

    private boolean isFull()
    {
        if (isEmpty()) return false;
        return size == array.length;
    }

    private void resize()
    {
        T[] newArr = (T[]) new Object[array.length * 2];
        for (int i = 0; i < size; i++) {
            newArr[i] = array[(head + i) % array.length];
        }
        array = newArr;
        head = 0;
        tail = size;
    }

    private void CutSize()
    {
        int newCapacity = array.length / 2;
        if (newCapacity < 8) return;  // 通常避免缩得太小

        T[] newArray = (T[]) new Object[newCapacity];

        // 按照逻辑顺序重新拷贝
        for (int i = 0; i < size; i++) {
            newArray[i] = array[(head + i) % array.length];
        }

        array = newArray;
        head = 0;
        tail = size;
    }

    public ArrayDeque()
    {
        array = (T[]) new Object[8];
        head = tail = 0;
        size = 0;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }


    public void addFirst(T item)
    {
        if(isFull()) resize();
        head = (head - 1 + array.length) % array.length;
        array[head] = item;
        size++;
    }

    public void addLast(T item)
    {
        if(isEmpty()) resize();
        tail = (tail + 1) % array.length;
        array[tail] = item;
        size++;
    }

    public T removeFirst()
    {
        if(isEmpty()) throw new NoSuchElementException();
        T value = array[head];
        array[head] = null;
        head = (head + 1 + array.length) % array.length;
        size--;
        return value;
    }

    public T removeLast()
    {
        if(isEmpty()) throw new NoSuchElementException();
        T value = array[tail];
        array[tail] = null;
        tail = (tail - 1 + array.length) % array.length;
        size--;
        return value;
    }

    public T get(int index)
    {
        if (isEmpty()) return null;
        if (index < 0 || index >= size) return null;
        return array[index];
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
        public ArrayIterator()
        {
            index = head;
        }
        @Override
        public boolean hasNext() {
            return index != tail;
        }
        @Override
        public T next()
        {
            if(!hasNext()) throw new NoSuchElementException();
            return array[index++];
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayDeque<?> that = (ArrayDeque<?>) o;
        if (size != that.size()) return false;
        int NowHead = head;
        int OHead = that.head;
        for (int i = 0; i < size; i++)
        {
            if (array[NowHead] != that.array[OHead]) return false;
            NowHead = (NowHead + 1 + array.length) % array.length;
            OHead = (OHead + 1 + array.length) % array.length;
        }
        return true;
    }
}


package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private Node sentinel;
    private int size;

    // 初始化空双向循环链表
    public LinkedListDeque() {
        sentinel = new Node(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    // 头插
    public void addFirst(T item) {
        Node newNode = new Node(item);

        newNode.next = sentinel.next;
        newNode.prev = sentinel;

        sentinel.next.prev = newNode;
        sentinel.next = newNode;

        size++;
    }

    // 尾插
    public void addLast(T item) {
        Node newNode = new Node(item);

        newNode.prev = sentinel.prev;
        newNode.next = sentinel;

        sentinel.prev.next = newNode;
        sentinel.prev = newNode;

        size++;
    }

    public int size() {
        return size;
    }

    // 打印
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.value + " ");
            p = p.next;
        }
        System.out.println();
    }

    // 删除头
    public T removeFirst() {
        if (isEmpty()) return null;

        Node first = sentinel.next;
        T value = first.value;

        sentinel.next = first.next;
        first.next.prev = sentinel;

        size--;
        return value;
    }

    // 删除尾
    public T removeLast() {
        if (isEmpty()) return null;

        Node last = sentinel.prev;
        T value = last.value;

        sentinel.prev = last.prev;
        last.prev.next = sentinel;

        size--;
        return value;
    }

    // 非递归 get
    public T get(int index) {
        if (index < 0 || index >= size) return null;

        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.value;
    }

    // 递归 get
    public T getRecursive(int index) {
        if (index < 0 || index >= size) return null;
        return getRecursiveHelper(index, sentinel.next);
    }

    private T getRecursiveHelper(int index, Node node) {
        if (index == 0) return node.value;
        return getRecursiveHelper(index - 1, node.next);
    }

    // 迭代器
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node p = sentinel.next;

        public boolean hasNext() {
            return p != sentinel;
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T value = p.value;
            p = p.next;
            return value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedListDeque)) return false;

        LinkedListDeque<?> other = (LinkedListDeque<?>) o;
        if (this.size != other.size) return false;

        Node p1 = this.sentinel.next;
        LinkedListDeque<?>.Node p2 = other.sentinel.next;

        while (p1 != this.sentinel) {
            if (!p1.value.equals(p2.value)) return false;
            p1 = p1.next;
            p2 = p2.next;
        }
        return true;
    }

    // Node 类
    private class Node {
        private Node prev;
        private Node next;
        private T value;

        Node(T val) {
            value = val;
        }
    }
}

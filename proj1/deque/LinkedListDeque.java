package deque;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Iterable<T> {
    private Node sentinel;
    private Node head;
    private Node tail;
    private int size;

    //初始化双端链表
    public LinkedListDeque(T data)
    {
        this.sentinel = new Node(null);
        this.head = new Node(data);
        this.tail = this.head;
        this.size = 1;
        this.sentinel.next = this.head;
    }

    //初始化空的双端链表
    public LinkedListDeque()
    {
        this.sentinel = new Node(null);
        this.head = null;
        this.tail = this.head;
        this.sentinel.next = this.head;
        this.size = 0;
    }

    //判断是否为空
    public boolean isEmpty()
    {
        return this.sentinel.next == null;
    }

    //头插
    public void addFirst(T item)
    {
        if (this.isEmpty())
        {
            this.head = this.tail = new Node(item);
            return;
        }
        Node tempNode = new Node(item);
        this.sentinel.next = tempNode;
        tempNode.previous = this.sentinel;
        tempNode.next = this.head;
        this.head = tempNode;
        this.size++;
    }

    //尾插
    public void addLast(T item)
    {
        if (this.isEmpty())
        {
            this.tail = this.head = new Node(item);
            return;
        }
        Node tempNode = new Node(item);
        this.tail.next = tempNode;
        tempNode.previous = this.tail;
        tempNode.next = this.sentinel;
        this.tail = tempNode;
        this.size++;
    }

    //大小获取
    public int size()
    {
        return this.size;
    }

    //打印
    public void printDeque()
    {
        LinkedListDeque<T> tempList = this;
        Node p =  tempList.head;
        if (this.size == 0) return;
        if (this.size == 1) System.out.println(this.head.value);
        while (p != tempList.sentinel)
        {
            System.out.println(p.value + " ");
            p = p.next;
        }
        System.out.println();
    }

    //删除头上的
    public T removeFirst()
    {
        if (this.isEmpty()) return null;
        if (this.isEmpty()) return null;
        T value = this.head.value;
        if (this.size == 1) {
            this.head = this.tail = this.sentinel; // 设置head和tail为sentinel
        } else {
            this.head = this.head.next;
            this.head.previous = null;
        }
        this.size--;
        return value;
    }

    public T removeLast()
    {
        if (this.isEmpty()) return null;
        if (this.isEmpty()) return null;
        T value = this.tail.value;
        if (this.size == 1) {
            this.head = this.tail = this.sentinel; // 设置head和tail为sentinel
        } else {
            this.tail = this.tail.previous;
            this.tail.next = null;
        }
        this.size--;
        return value;
    }

    //查找
    public T get(int index)
    {
        if (this.isEmpty()) return null;
        if (index >= this.size || index < 0) return null;
        int nowindex = 0;
        LinkedListDeque<T> tempList = this;
        Node p =  tempList.head;
        while(nowindex < index)
        {
            p = p.next;
            nowindex++;
        }
        return p.value;
    }

    //迭代器
    public Iterator<T> iterator()
    {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T>
    {
        private Node NowHead;

        public  LinkedListDequeIterator()
        {
            NowHead = head;
        }

        @Override
        public boolean hasNext() {
            return NowHead.next != sentinel;
        }

        @Override
        public T next()
        {
            if (!hasNext()) throw new NoSuchElementException();
            T value = NowHead.value;
            NowHead = NowHead.next;
            return value;
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        LinkedListDeque<?> that = (LinkedListDeque<?>) o;
        LinkedListDeque<T> tempList = this;
        LinkedListDeque<?>.Node tempOhead = that.head;
        Node tempHead =  tempList.head;
        if(that.size() != tempList.size()) return false;
        for (int i = 0 ; i < that.size ; i++)
        {
            if (tempHead.getClass() != tempOhead.getClass()) return false;
            if(!tempHead.value.equals(tempOhead.value)) return false;
            tempHead = tempHead.next;
            tempOhead = tempOhead.next;
        }
        return true;
    }

    public T getRecursive(int index)
    {
        if (index >= this.size || index < 0) return null;
        return getRecursiveHelper(index, this.head);
    }

    private T getRecursiveHelper(int index,Node tempNode)
    {
        if (index == 0)
            return tempNode.value;
        return getRecursiveHelper(index-1, tempNode.next);
    }
    //Node类实现
    private class Node {
        private Node previous;
        private Node next;
        private T value;

        private Node(T val)
        {
            this.value = val;
            this.previous = null;
            this.next = null;
        }
    }
}

package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Iterable<T> {
    private Node sentinel;
    private Node head;
    private Node tail;
    private int size;



    //初始化空的双端链表
    public LinkedListDeque()
    {
        this.sentinel = new Node(null);
        this.head = this.tail = null;
        this.sentinel.next = null;
        this.size = 0;
    }

    //判断是否为空
    public boolean isEmpty()
    {
        return this.size == 0;
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
        this.head.previous = tempNode;
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
        this.sentinel.previous = tempNode;
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
        T value = this.head.value;
        if (this.size == 1)
        {
            this.head = this.tail =null;
            sentinel.previous = null;
            sentinel.next = null;
            size = 0; // 设置head和tail为sentinel
        }
        else
        {
            this.head = this.head.next;
            this.head.previous = null;
            this.sentinel.next = this.head;
        }
        this.size--;
        return value;
    }

    public T removeLast()
    {
        if (this.isEmpty()) return null;
        T value = this.tail.value;
        if (this.size == 1) {
            this.head = this.tail =null;
            sentinel.previous = null;
            sentinel.next = null;
            size = 0;
        } else {
            this.tail = this.tail.previous;
            this.tail.next = null;
            this.sentinel.previous = this.tail;
        }
        this.size--;
        return value;
    }

    //查找
    public T get(int index)
    {
        if (this.isEmpty()) return null;
        if (index >= this.size || index < 0) return null;
        Node p =  this.head;
        for(int i = 0; i < index; i++)
        {
            p = p.next;
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
            return NowHead != null;
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
        LinkedListDeque<?>.Node TempOHead = that.head;
        Node tempHead =  tempList.head;
        if(that.size() != tempList.size()) return false;
        for (int i = 0 ; i < that.size ; i++)
        {
            if (tempHead.getClass() != TempOHead.getClass()) return false;
            if(!tempHead.value.equals(TempOHead.value)) return false;
            tempHead = tempHead.next;
            TempOHead = TempOHead.next;
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

package bstmap;

import java.util.*;

//先放个abstract在这里免得标红很烦
public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{
    BSTNode BST;

    @Override
    public void clear()
    {
       ClearHelper(BST);
    }

    @Override
    public boolean containsKey(K key) {
        return ContainsKeyHelper(key, BST);
    }

    @Override
    public V get(K key)
    {
        return getHelper(key, BST);
    }

    @Override
    public int size()
    {
        return SizeHelper(BST);
    }

    @Override
    public void put(K key, V value)
    {
        BSTNode TmpNode = new BSTNode(key, value);
        putHelper(TmpNode,BST);
    }

    private BSTNode putHelper(BSTNode node, BSTNode subtree) {
        if (subtree == null) {
            return node; // 找到位置，插入这个节点
        }

        int cmp = node.key.compareTo(subtree.key);

        if (cmp < 0) {
            subtree.left = putHelper(node, subtree.left);
        } else if (cmp > 0) {
            subtree.right = putHelper(node, subtree.right);
        } else {
            subtree.value = node.value; // key 相等 -> 更新
        }

        return subtree;
    }

    @Override
    public Set<K> keySet()
    {
        return GetSet(BST);
    }

    private Set<K> GetSet(BSTNode node)
    {
        Set<K> keys = new HashSet<>();
        keys.add(node.key);
        if (node.left != null) GetSet(node.left);
        if (node.right != null) GetSet(node.right);
        return keys;
    }

    @Override
    public V remove(K key)
    {
        BSTNode TmpNode = FindKey(BST, key);
        V ret = TmpNode.value;
        removeHelper(TmpNode,BST);
        return ret;
    }

    private BSTNode removeHelper(BSTNode node, BSTNode subtree)
    {
        if (subtree == null)
        {
            return null;
        }

        int cmp = node.key.compareTo(subtree.key);

        if (cmp < 0)
        {
            subtree.left =  removeHelper(node, subtree.left);
        }
        else if (cmp > 0)
        {
            subtree.right = removeHelper(node, subtree.right);
        }
        else
        {
            if (subtree.left == null && subtree.right == null)
            {
                return null;
            }
            else if (subtree.left != null && subtree.right == null)
            {
                return subtree.left;
            }
            else if (subtree.left == null )
            {
               return subtree.right;
            }
            else
            {
                BSTNode DeleteNode = FindMin(subtree.right);
                subtree.value = DeleteNode.value;
                subtree.right = removeHelper(DeleteNode, subtree.right);
            }
        }
        return subtree;
    }

    private BSTNode FindMax(BSTNode NowTree)
    {
        if (NowTree == null) return null;
        BSTNode MaxNode = NowTree;
        while (MaxNode.right != null)
        {
            MaxNode = MaxNode.right;
        }
        return MaxNode;
    }
    private BSTNode FindMin(BSTNode NowTree)
    {
        if (NowTree == null) return null;
        BSTNode MinNode = NowTree;
        while (MinNode.left !=null)
        {
            MinNode = MinNode.left;
        }
        return MinNode;
    }

    private BSTNode FindKey(BSTNode NowTree, K key)
    {
        if (NowTree == null)
        {
            return null;
        }
        int cmp = NowTree.key.compareTo(key);
        if (cmp == 0)
        {
            return NowTree;
        }
        else if (cmp > 0)
        {
            return FindKey(NowTree.right, key);
        }
        else
        {
        return FindKey(NowTree.left, key);
        }
    }

    @Override
    public  V remove(K key, V value)
    {
        throw  new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<K> iterator()
    {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        private Stack<BSTNode> stack = new Stack<>();

        public BSTMapIterator() {
            pushLeft(BST);        // 初始化，把整棵树的左链入栈
        }

        private void pushLeft(BSTNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                return null;
            }

            BSTNode node = stack.pop();  // 中序：先访问最左
            K result = node.key;

            if (node.right != null) {
                pushLeft(node.right);    // 再处理右子树
            }

            return result;
        }
    }




    private void ClearHelper(BSTNode node)
    {
        if (node.value != null)
        {
            node.value = null;
        }

        if (node.left != null)
        {
            ClearHelper(node.left);
        }
        if (node.right != null)
        {
            ClearHelper(node.right);
        }
    }
    private int SizeHelper(BSTNode node)
    {
        if (node.value == null)
            return 0;
        else
        {
            return 1+SizeHelper(node.left)+SizeHelper(node.right);
        }
    }

    private V getHelper(K key, BSTNode node)
    {
        int cmp = node.key.compareTo(key);
        if (cmp == 0)
            {
            return node.value;
            }
        else if  (cmp < 0)
            {
                return getHelper(key, node.left);
            }
        else
            {
                return getHelper(key, node.right);
            }
    }

    private boolean ContainsKeyHelper(K key,  BSTNode node)
    {
        int cmp = node.key.compareTo(key);
        if (cmp == 0)
        {
            return node.value != null;
        }
        else if  (cmp < 0)
        {
            return ContainsKeyHelper(key, node.left);
        }
        else
        {
            return ContainsKeyHelper(key, node.right);
        }
    }

    private class BSTNode
    {
        K key;
        V value;
        BSTNode left;
        BSTNode right;
        BSTNode(K key, V value)
        {
            this.key = key;
        }

        public void add(K AddKey,V AddValue)
        {
            int CmpNum = key.compareTo(AddKey);
            if (CmpNum < 0)
            {
                if (left == null)
                    left = new BSTNode(AddKey, AddValue);
                else
                    left.add(AddKey, AddValue);
            }
            else if (CmpNum > 0)
            {
                if (right == null)
                    right = new BSTNode(AddKey, AddValue);
                else
                    right.add(AddKey, AddValue);
            }
            else
            {
                value = AddValue;
            }
        }

        public V  get(K Key)
        {
            int CmpNum = key.compareTo(Key);
            if (CmpNum < 0)
            {
                if (left == null)
                    return null;
                else
                    return left.get(Key);
            }
            else if (CmpNum > 0)
            {
                if (right == null)
                    return null;
                else
                    return right.get(Key);
            }
            else
            {
                return value;
            }
        }


    }
}

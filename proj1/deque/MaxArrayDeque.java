package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T>  comparator)
    {
        super();
        cmp =  comparator;
    }

    public T max(Comparator<T>  comparator)
    {
        if (isEmpty()) return null;
        T MaxItem = get(0);
        for (T item : this)
        {
            if (cmp.compare(item, MaxItem) > 0)
            {
                MaxItem = item;
            }
        }
        return MaxItem;
    }

    @Override
    public boolean equals(Object o)
    {
        return this == o;
    }

    public T max()
    {
        return max(cmp);
    }

}

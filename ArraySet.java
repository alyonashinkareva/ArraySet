package info.kgeorgiy.ja.shinkareva.arrayset;
import java.util.*;

public class ArraySet<E> implements NavigableSet<E> {
    private final ArrayList<E> array;
    private final Comparator<E> comparator;


    public ArraySet() {
        this.array = new ArrayList<>();
        this.comparator = null;
    }

    public ArraySet(Collection<E> collection) {
        this.array = new ArrayList<>(collection);
        this.comparator = null;
    }

    public ArraySet(Collection<E> collection, Comparator<E> comparator) {
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(collection);
        this.comparator = comparator;
        this.array = new ArrayList<>(set);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(array, (E) o, comparator) >= 0;
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object e: c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object[] toArray() {
        return array.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return array.toArray(a);
    }

    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

    @Override
    public Comparator comparator() {
        return comparator;
    }

    @Override
    public E first() {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            return array.get(0);
        }
    }

    @Override
    public E last() {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            return array.get(size() - 1);
        }
    }

    @Override
    public E lower(E e) {
        return getElementByIndex(e, -1, -1);
    }

    @Override
    public E floor(E e) {
        return getElementByIndex(e, 0, -1);
    }

    @Override
    public E ceiling(E e) {
        return getElementByIndex(e, 0, 0);
    }

    @Override
    public E higher(E e) {
        return getElementByIndex(e, 1, 0);
    }

    private E getElementByIndex(E element, int shiftIfFound, int shiftIfNotFound) {
        int index = getRightIndex(element, shiftIfFound, shiftIfNotFound);
        return checkIndex(index) ? array.get(index) : null;
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return array.iterator();
    }


    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(array, Collections.reverseOrder(comparator()));
    }

    @Override
    public Iterator descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return subSetWithCheckToThrow(fromElement, fromInclusive, toElement, toInclusive, true);
    }

    private int getRightIndex(E element, int shiftIfFound, int shiftIfNotFound) {
        int index = Collections.binarySearch(array, element, comparator);
        if (index < 0) {
            index = - index - 1;
            index += shiftIfNotFound;
        } else {
            index += shiftIfFound;
        }
        return index;
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < size();
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return isEmpty() ? new ArraySet<E>() : subSetWithCheckToThrow(first(), true, toElement, inclusive, false);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return isEmpty() ? new ArraySet<E>() : subSetWithCheckToThrow(fromElement, inclusive, last(), true, false);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    private NavigableSet<E> subSetWithCheckToThrow(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive, boolean toThrow) {
        if (toThrow && comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        int indexOfFromElement = getRightIndex(fromElement, fromInclusive ? 0 : 1, 0);
        int indexOfToElement = getRightIndex(toElement, toInclusive ? 0 : -1, -1);
        if (indexOfFromElement > indexOfToElement || !checkIndex(indexOfFromElement) || !checkIndex(indexOfToElement)) {
            return new ArraySet<>();
        } else {
            return new ArraySet<>(array.subList(indexOfFromElement, indexOfToElement + 1), comparator);
        }
    }
}
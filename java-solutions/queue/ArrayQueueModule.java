package queue;

/*
Model:
Let immutable(b):
    b'.size == b.size && for all i in 0..b.length-1 b'[i] = b[i]
Invariant:
    n >= 0; b[0], ..., b[n - 1] && (for all i in 0..n - 1: b[i] != null)
 */
public class ArrayQueueModule {
    private static int head = 0, size = 0;
    private static Object[] elements = new Object[2];

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] tmp = new Object[elements.length * 2];
            System.arraycopy(elements, head, tmp, 0, elements.length - head);
            System.arraycopy(elements, 0, tmp, elements.length - head, head);
            elements = tmp;
            head = 0;
        }
    }

    private static int movePosition(int x) {
        int res = (head + x) % elements.length;
        return res >= 0 ? res : (res + elements.length) % elements.length;
    }

    /*
    Pred: element != null
    Post: n' = n + 1 && for all i in 1..n b'[i] = b[i - 1] && b'[0] = element
     */
    public static void push(Object element) {
        ensureCapacity(size + 1);
        int cur = movePosition(-1);
        elements[cur] = element;
        head = cur;
        size++;
    }

    /*
    Pred: n >= 1
    Post: R = b[n - 1] && immutable(b)
     */
    public static Object peek() {
        assert size >= 1;
        return elements[movePosition(size - 1)];
    }

    /*
    Pred: n >= 1
    Post: n' = n - 1 && for all i in 0..n - 2 b'[i] = b[i] && R = b[n - 1]
     */
    public static Object remove() {
        assert size >= 1;
        int cur = movePosition(size - 1);
        final Object result = elements[cur];
        elements[cur] = null;
        size--;
        return result;
    }

    /*
    Pred: element != null
    Post: immutable(b) &&
          (
          ( R == -1 && for all i in 0..n-1 b[i] != element                           ) ||
          ( R in [0..n-1] && b[R] = element && for all j in [0..R-1] b[j] != element )
          )
     */
    public static int indexOf(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[movePosition(i)].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /*
    Pred: element != null
    Post: immutable(b) &&
          (
          ( R == -1 && for all i in 0..n-1 b[i] != element                               ) ||
          ( R in [0..n-1] && b[R] = element && for all j in [R + 1..n-1] b[j] != element )
          )
     */
    public static int lastIndexOf(Object element) {
        for (int i = size - 1; i >= 0; i--) {
            if (elements[movePosition(i)].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /*
    Pred: element != null
    Post: n' = n + 1 && for all i in 0..n - 1 b'[i] = b[i] && b'[n] = element
     */
    public static void enqueue(Object element) {
        ensureCapacity(size + 1);
        elements[movePosition(size)] = element;
        size++;
    }

    /*
    Pred: n >= 1
    Post: R = b[0] && immutable(b)
     */
    public static Object element() {
        assert size >= 1;
        return elements[head];
    }

    /*
    Pred: n >= 1
    Post: n' = n - 1 && for all i in 0 ... n - 2 b[i]' = b[i + 1] && R = b[0]
     */
    public static Object dequeue() {
        assert size >= 1;
        final Object result = elements[head];
        elements[head] = null;
        head = movePosition(1);
        size--;
        return result;
    }

    /*
    Pred: true
    Post: R = n && immutable(b)
     */
    public static int size() {
        return size;
    }

    /*
    Pred: true
    Post: R = n == 0 && immutable(b)
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /*
    Pred: true
    Post: n = 0
     */
    public static void clear() {
        elements = new Object[2];
        head = size = 0;
    }
}

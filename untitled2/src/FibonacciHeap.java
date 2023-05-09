import java.util.*;

public class FibonacciHeap {
    static Random random = new Random();

    public static void main(String[] args) {
        int[] fib = new int[10000];
        List<Number> timeAdd = new ArrayList<>();
        List<Number> cntAdd = new ArrayList<>();
        for (int i = 0; i < fib.length; i++) {
            long start = System.nanoTime();
            fib[i] = random.nextInt();

            FibonacciHeap fb = new FibonacciHeap(fib);
            long end = System.nanoTime();
            timeAdd.add((end - start) / 1e6);
            cntAdd.add(fb.getCntAdd());
        }
        System.out.println("Среднее время добавления: " + timeAdd.stream().mapToDouble(Number::doubleValue).sum() / timeAdd.size() + " мс");

        FibonacciHeap fibonacciHeap = new FibonacciHeap(fib);
        List<Number> timeDelete = new ArrayList<>();
        List<Number> cntDelete = new ArrayList<>();
        int[] arrForDelete = new int[1000];
        create(fib, arrForDelete);
        for (int i = 0; i < fib.length; i++) {
//            int index = indexOf(fib,arrForDelete[i]);
            long start = System.nanoTime();

            fibonacciHeap.removeMin();
            long end = System.nanoTime();
            cntDelete.add(fibonacciHeap.getCntAdd());
            timeDelete.add((end - start) / 1e6);
        }
        System.out.println("Среднее время удаления: " + timeDelete.stream().mapToDouble(Number::doubleValue).sum() / timeDelete.size() + " мс");

        List<Number> timeFind = new ArrayList<>();
        List<Number> cntFind = new ArrayList<>();
        /*
        В фибоначчиевой куче нет метода найти элемент, а есть метод найти минимальный элемент
        а когда мы берем случайные числа, это не означает, что в выбранной последовательности есть минимальный элемент
         */
        int[] arrForFind = new int[100];

        create(fib, arrForFind);
        for (int i = 0; i < arrForFind.length; i++) {
            int index = indexOf(fib, arrForFind[i]);
            long startTime = System.nanoTime();
            if (!(fibonacciHeap.findMin() == arrForFind[i])) {
                throw new RuntimeException("ошибка!!");
            }
            long endTime = System.nanoTime();
            cntFind.add(fibonacciHeap.getCntFind());
            timeFind.add((endTime - startTime) / 1e6);
        }

        System.out.println("Среднее время добавления: " + timeAdd.stream().mapToDouble(Number::doubleValue).sum() / timeAdd.size() + " мс");
        System.out.println("Среднее время поиска: " + timeFind.stream().mapToDouble(Number::doubleValue).sum() / timeFind.size() + " мс");
        System.out.println("Среднее время удаления: " + timeDelete.stream().mapToDouble(Number::doubleValue).sum() / timeDelete.size() + " мс");

        System.out.println("Колво итераций добавления:" + cntAdd);
        System.out.println("Кол-во итераций поиска: " + cntFind);
        System.out.println("Кол-во итераций удаления: " + cntDelete);

    }
//    public static void main(String[] args) {
//
//        int [] heapArr = new int[4];
//        FibonacciHeap heap = new FibonacciHeap(heapArr);
//        // Вставляем элементы
//        heap.insert(5);
//        heap.insert(8);
//        heap.insert(3);
//        heap.insert(9);
//
//        System.out.println(heap);
//        // Получаем минимальный элемент и выводим его на экран
//        int min = heap.findMin();
//        System.out.println("Min: " + min);
//
//        // Удаляем минимальный элемент
//        min = heap.removeMin();
//        System.out.println("Removed min: " + min);
//
//        // Уменьшаем ключ элемента из кучи
//        FibonacciHeap.Node node = heap.insert(10);
//        heap.decreaseKey(node, 6);
//
//        // Удаляем заданный элемент из кучи
//        heap.cascadingCut(node);
//
//        // Очищаем кучу
//        heap.clear();
//    }

    public static void create(int[] arr, int[] arrForFind) {
        int randomIndex;
        int randomElem;
        for (int i = 0; i < arrForFind.length; i++) {
            randomIndex = random.nextInt(arrForFind.length);
            randomElem = arr[randomIndex];
            arrForFind[i] = randomElem;
        }
    }

    public static int indexOf(int[] arr, int x){
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x){
                return i;
            }
        }
        return -1;
    }
    public static int[] getNewArr(int[] arr, int j){
        int[] newArr = new int[arr.length-1];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != j){
                newArr[index] = arr[i];
                index++;
            }
        }
        return newArr;
    }

    private Node min;
    private int count;
    private static int [] heap;

    private int cntAdd;
    private int cntDelete;
    private int cntFind;

    public int getCntAdd() {
        return cntAdd;
    }
    public int getCntDelete() {
        return cntDelete;
    }
    public int getCntFind() {
        return cntFind;
    }

    public FibonacciHeap(int[] heap) {
        this.heap = heap;
        this.min = null;
        this.count = 0;
    }

    public boolean isEmpty() {
        return this.min == null;
    }

    public int size() {
        return this.count;
    }

    public void clear() {
        this.min = null;
        this.count = 0;
    }

    public Node insert(int key) {
        Node newNode = new Node(key);
        if (this.min == null) {
            this.min = newNode;
        } else {
            this.min = merge(this.min, newNode);
        }
        this.count++;
        return newNode;
    }

    public int findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty.");
        }
        return this.min.key;
    }

    public int removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty.");
        }
        Node prevMin = this.min;
        Node newMin = null;
        if (prevMin.child != null) {
            newMin = prevMin.child;
            Node curr = newMin;
            do {
                curr.parent = null;
                curr = curr.next;
            } while (curr != newMin);
        }
        this.min = merge(this.min.next, newMin);
        if (this.min != null) {
            consolidate();
        }
        this.count--;
        return prevMin.key;
    }

    private void consolidate() { //объединение
        int maxDegree = (int) Math.ceil(Math.log(this.count) / Math.log((1.0 + Math.sqrt(5)) / 2.0));
        Node[] degree = new Node[maxDegree + 1];
        Node curr = this.min;
        do {
            Node next = curr.next;
            int currDegree = curr.degree;
            while (degree[currDegree+1] != null) {
                Node other = degree[currDegree];
                if (curr.key > other.key) {
                    Node temp = curr;
                    curr = other;
                    other = temp;
                }
                link(other, curr);
                degree[currDegree] = null;
                currDegree++;
            }
            degree[currDegree] = curr;
            curr = next;
        } while (curr != this.min);
        this.min = null;
        for (Node node : degree) {
            if (node != null) {
                if (this.min == null) {
                    this.min = node;
                } else {
                    this.min = merge(this.min, node);
                }
            }
        }
    }

    private static void link(Node child, Node parent) {  //ссылка
        child.prev.next = child.next;
        child.next.prev = child.prev;
        child.parent = parent;
        if (parent.child == null) {
            parent.child = child;
            child.prev = child;
            child.next = child;
        } else {
            child.prev = parent.child.prev;
            child.next = parent.child;
            parent.child.prev.next = child;
            parent.child.prev = child;
        }
        parent.degree++;
        child.marked = false;
    }

    private static Node merge(Node a, Node b) { //объединение 2 куч
        if (a == null && b == null) {
            return null;
        } else if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            Node temp = a.next;
            a.next = b.next;
            a.next.prev = a;
            b.next = temp;
            b.next.prev = b;
            return (a.key < b.key ? a : b);
        }
    }

    public void decreaseKey(Node node, int newKey) { //уменьшение
        if (newKey > node.key) {
            throw new IllegalArgumentException("New key is greater than current key.");
        }
        node.key = newKey;
        Node parent = node.parent;
        if (parent != null && node.key < parent.key) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.key < this.min.key) {
            this.min = node;
        }
    }

    private void cut(Node child, Node parent) {
        child.prev.next = child.next;
        child.next.prev = child.prev;
        parent.degree--;
        if (parent.child == child) {
            parent.child = child.next;
        }
        if (parent.degree == 0) {
            parent.child = null;
        }
        child.prev = this.min.prev;
        child.next = this.min;
        this.min.prev.next = child;
        this.min.prev = child;
        child.parent = null;
        child.marked = false;
    }

    private void cascadingCut(Node node) {
        Node parent = node.parent;
        if (parent != null) {
            if (!node.marked) {
                node.marked = true;
            } else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }


    public String toString() {
        return Arrays.toString(heap);
    }

    public static class Node {
        private int key;
        private int degree;
        private Node child;
        private Node prev;
        private Node next;
        private Node parent;
        private boolean marked; //помеченные узлы

        private Node(int key) {
            this.key = key;
            this.degree = 0;
            this.child = null;
            this.prev = this;
            this.next = this;
            this.parent = null;
            this.marked = false;
        }

        public int getKey() {
            return this.key;
        }
    }
}

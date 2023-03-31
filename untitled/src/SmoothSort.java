import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class SmoothSort {
    public static void sort(int[] arr) {
        int n = arr.length;
        int p = 1;
        int q = 1;
        while (q < n) {
            p = nextP(p);
            q = p + q;
        }
        q = q - p;
        p = p - q;
        int r = 0;
        while (q > 0) {
            p = prevP(p);
            q = q - p;
            int i = r + q;
            int j = i + p;
            while (i < j) {
                swapIfGreater(arr, i, j);
                i = i + p;
                j = j - p;
            }
            r = r + p;
        }
        insertionSort(arr);
    }

    private static int nextP(int p) {
        return p << 1 | 1;
    }

    private static int prevP(int p) {
        return (p - 1) >> 1;
    }

    //меняем местами если i < j
    private static void swapIfGreater(int[] arr, int i, int j) {
        if (i < j && arr[i] > arr[j]) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    //сортировка по вставке
    private static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int j = i;
            while (j > 0 && arr[j - 1] > arr[j]) {
                int temp = arr[j - 1];
                arr[j - 1] = arr[j];
                arr[j] = temp;
                j--;
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        Scanner scanner = new Scanner(new File("numbers.txt"));
        System.out.println("Original array: " + Arrays.toString(arr));
        SmoothSort.sort(arr);
        System.out.println(Arrays.toString(arr));

    }
}

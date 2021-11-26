import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {

        Node<Integer> n1 = new Node<>(10);
        Node<Integer> n2 = new Node<>(20);

        new LinkedList<String>();

        MyLinkedList<Integer> l = new MyLinkedList<>();
        l.prepend(1);
        l.prepend(2);
        l.prepend(3);
        System.out.printf("Search: %s%n", l.search(2));
        System.out.printf("List size = %d%n", l.size());
        System.out.printf("list: %s%n", l);

        l.insert(4, 0);
        System.out.printf("list: %s%n", l);

        System.out.println(l.removeAtIndex(3));
        System.out.printf("list: %s%n", l);
        System.out.println(l.get(0));

        List<Integer> l1 = Arrays.asList(9, 3, 5, 1, 2, 1, 0, 32);
        System.out.printf("Initial list: %s%n", l1);
        List<Integer> seedList = IntStream.range(1, 1_000_000).boxed().collect(Collectors.toList());
        Collections.shuffle(seedList);
        List<Integer> l2 = new ArrayList<>(seedList);
        List<Integer> l3 = new ArrayList<>(seedList);
        long t1 = new Date().getTime();
        System.out.printf("List sorted(Selection sort): %s%n", verifySort(selectionSortList(l2)));
        System.out.printf("Time taken(Selection sort):  %d%n", (new Date().getTime()) - t1);
        long t2 = new Date().getTime();
        System.out.printf("List sorted(Merge sort): %s%n", verifySort(mergeSortList(l3)));
        System.out.printf("Time taken(Merge sort):  %d%n", (new Date().getTime()) - t2);
        MyLinkedList<Integer> l4 = new MyLinkedList<>();
        l4.prepend(9);
        l4.prepend(3);
        l4.prepend(5);
        l4.prepend(1);
        l4.prepend(2);
        System.out.printf("List sorted(Merge sort MyLinkedList): %s%n", mergeSortLinkedList(l4));

        List<Integer> l5 = new ArrayList<>(seedList);
        long t3 = new Date().getTime();
        System.out.printf("List sorted(Quicksort): %s%n", verifySort(quicksort(l5)));
        System.out.printf("Time taken(Quicksort):  %d%n", (new Date().getTime()) - t3);

//        compareSearchWorstCases();
//        verify(linearSearch(Arrays.asList(1,2,3,4,5,6,7,8,9,10), 1));
//        verify(binarySearch(Arrays.asList(1,2,3,4,5,6,7,8,9,10), 1));
//        System.out.println(recursiveBinarySearch(Arrays.asList(1,2,3,4,5,6,7,8,9,10), 1));
    }

    private static List<Integer> quicksort(List<Integer> list) {
        if (list.size() <= 1) return list;

        List<Integer> lThan = new ArrayList<>();
        List<Integer> gThan = new ArrayList<>();
        int pivot = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            int value = list.get(i);
            if (value <= pivot) lThan.add(value);
            else gThan.add(value);
        }

        List<Integer> leftResult = quicksort(lThan);
        List<Integer> rightResult = quicksort(gThan);
        leftResult.add(pivot);
        leftResult.addAll(rightResult);
        return leftResult;
    }

    private static List<Integer> selectionSortList(List<Integer> list) {
        if (list.size() <= 1) return list;

        List<Integer> sorted = new ArrayList<>();

        while (list.size() > 1) {
            int min = list.get(0);
            int minIndex = 0;

            for (int i = 1; i < list.size(); i++) {
                int value = list.get(i);
                if (value <  min) {
                    min = value;
                    minIndex = i;
                }
            }

            list.remove(minIndex);
            sorted.add(min);
        }

        return sorted;
    }

    /**
     * Sorts a list of values.
     * Runs in quasilinear (Linearithmic) time {@code O(n log n)}. A step runs in {@code O(n)},
     * and there are {@code O(log n)} steps.
     * @param list {@link List} of items to be sorted
     * @return sorted {@link List} of items
     */
    private static List<Integer> mergeSortList(List<Integer> list) {
        //base condition
        if (list.size() <= 1) return list;

        //split. Each split call runs in constant time O(1)
        List<Integer> leftMerge = mergeSortList(list.subList(0, (list.size()/2)));
        List<Integer> rightMerge = mergeSortList(list.subList((list.size()/2), list.size()));

        //merge while sorting. Each call runs in linear time O(n)
        List<Integer> result = new ArrayList<>();
        int lIndex = 0;
        int rIndex = 0;

        while (lIndex < leftMerge.size() && rIndex < rightMerge.size()) {
            if (leftMerge.get(lIndex) < rightMerge.get(rIndex)) {
                result.add(leftMerge.get(lIndex));
                lIndex++;
            } else {
                result.add(rightMerge.get(rIndex));
                rIndex++;
            }
        }

        while (lIndex < leftMerge.size()) {// while left list still has uncompared items
            result.add(leftMerge.get(lIndex));
            lIndex++;
        }
        while (rIndex < rightMerge.size()) {// while right list still has uncompared items
            result.add(rightMerge.get(rIndex));
            rIndex++;
        }

        return result;
    }

    /**
     * Sorts a {@link MyLinkedList} in ascending order.<br/>
     * Recursively divides the list into sublists containing a single node.<br/>
     * Repeatedly merge the sublists, while sorting, to produce a sorted {@link MyLinkedList}<br/>
     * Runs in quasilinear time {@code O(kn log n)}
     * @param linkedList list of items to sort
     * @return sorted {@link MyLinkedList}
     */
    private static MyLinkedList<Integer> mergeSortLinkedList(MyLinkedList<Integer> linkedList) {
        if (linkedList.size() <= 1) return linkedList;

        //split. Each call runs in O(k) where k = n/2.
        MyLinkedList<Integer>[] split = splitMyLinkedListMergeSort(linkedList);
        MyLinkedList<Integer> leftMerge = mergeSortLinkedList(split[0]);
        MyLinkedList<Integer> rightMerge = mergeSortLinkedList(split[1]);

        //merge
        //merge while sorting. Each call runs in linear time O(n)
        MyLinkedList<Integer> result = new MyLinkedList<>();
        result.setHead(new Node<>(-1));//fake head that's discarded later
        Node<Integer> currentNode = result.getHead();
        int lIndex = 0;
        int rIndex = 0;

        Node<Integer> lCurrent = leftMerge.getHead();
        Node<Integer> rCurrent = rightMerge.getHead();

        int leftSize = leftMerge.size();
        int rightSize = rightMerge.size();

        while (lIndex < leftSize && rIndex < rightSize) {
            if (lCurrent.getData() < rCurrent.getData()) {
                currentNode.setNextNode(new Node<>(lCurrent.getData()));
                lCurrent = lCurrent.getNextNode();
                lIndex++;
            } else {
                currentNode.setNextNode(new Node<>(rCurrent.getData()));
                rCurrent = rCurrent.getNextNode();
                rIndex++;
            }
            currentNode = currentNode.getNextNode();
        }

        while (lIndex < leftSize) {// while left list still has uncompared items
            currentNode.setNextNode(new Node<>(lCurrent.getData()));
            currentNode = currentNode.getNextNode();
            lCurrent = lCurrent.getNextNode();
            lIndex++;
        }
        while (rIndex < rightSize) {// while right list still has uncompared items
            currentNode.setNextNode(new Node<>(rCurrent.getData()));
            currentNode = currentNode.getNextNode();
            rCurrent = rCurrent.getNextNode();
            rIndex++;
        }

        result.setHead(result.getHead().getNextNode());
        return result;
    }

    /**
     * Splits a {@link MyLinkedList} in half.<br/>
     * <b>Assumes min list size of {@literal 2}. To be called from {@link #mergeSortLinkedList(MyLinkedList)}</b>
     * Runs in {@code O(k)} time where k is n/2
     * @param list list to be split
     * @return An array of {@link MyLinkedList}
     */
    private static MyLinkedList<Integer>[] splitMyLinkedListMergeSort(MyLinkedList<Integer> list) {
        int size = list.size();

        if (size == 1) //noinspection unchecked
            return new MyLinkedList[] {list, new MyLinkedList<Integer>()};

        Node<Integer> midNode = list.get(size/2 - 1);

        MyLinkedList<Integer> r = new MyLinkedList<>();
        r.setHead(midNode.getNextNode());
        midNode.setNextNode(null);

        //noinspection unchecked
        return new MyLinkedList[] {list, r};
    }

    private static void compareSearchWorstCases() {
        List<Integer> list = IntStream.range(1, 1000).boxed().collect(Collectors.toList());

        long time1 = new Date().getTime();
        for (int i = 0; i < 100_000_000; i++) {
            binarySearch(list, 1);
        }
        System.out.println("First: " + (new Date().getTime() - time1));

        long time2 = new Date().getTime();
        for (int i = 0; i < 100_000_000; i++) {
            recursiveBinarySearch(list, 1);
        }
        System.out.println("Second: " + (new Date().getTime() - time2));

        long time3 = new Date().getTime();
        for (int i = 0; i < 100_000_000; i++) {
            linearSearch(list, 1000);
        }
        System.out.println("Third: " + (new Date().getTime() - time3));
    }

    static int linearSearch(List<Integer> list, int target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == target) return i;
        }
        return -1;
    }

    static int binarySearch(List<Integer> list, int target) {
        int first = 0;
        int last = list.size() - 1;

        while(first <= last) {
            int midpoint = (first + last) / 2;

            if (list.get(midpoint) == target) return midpoint;
            else if (list.get(midpoint) < target) first = midpoint + 1;
            else last = midpoint - 1;
        }

        return -1;
    }

    static boolean recursiveBinarySearch(List<Integer> list, int target) {
        if (list.size() == 0) return false;
        else {
            int midpoint = list.size() / 2;

            if (list.get(midpoint) == target) return true;
            else if (list.get(midpoint) < target) return recursiveBinarySearch(list.subList(midpoint + 1, list.size() - 1), target);
            else return recursiveBinarySearch(list.subList(0, midpoint - 1), target);
        }
    }

    static void verify(int index) {
        if (index == -1) System.out.println("Not found");
        else System.out.printf("Found at index: %d%n", index);
    }

    static boolean verifySort(List<Integer> list){
//        for (int i = 0; i < list.size() - 1; i++) {
//            if (list.get(i) > list.get(i+1)) return false;
//        }
        return true;
    }

    static boolean verifySort(MyLinkedList<Integer> list){

        if (list.getHead() == null || list.getHead().getNextNode() == null) return true;
        Node<Integer> current = list.getHead();

        while (true) {
            if (current.getNextNode().getData() == null) return true;

            if (current.getData() <= current.getNextNode().getData()) current = current.getNextNode();
            else {
                return false;
            }
        }
    }
}

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DynamicProgrammingAndTabulation {

    public static void main(String[] args) {

        long t1 = new Date().getTime();
        System.out.println(fib(35, null));
        System.out.println("Tabulation: " + fibTabulation(35));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t1);

        long t2 = new Date().getTime();
        System.out.println(fib(500, null));
        System.out.println("Tabulation: " + fibTabulation(500));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t2);

        long t3 = new Date().getTime();
        System.out.println(gridTraveler(500, 500, null));
        System.out.println("Tabulation: " + gridTravelerTabulation(500, 500));//faster
        System.out.printf("Time: %d%n", (new Date().getTime()) - t3);

        long t4 = new Date().getTime();
        System.out.println(canSum(7, Arrays.asList(1), null));
        System.out.println("Tabulation: " + canSumTabulation(7, Arrays.asList(1)));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t4);

        long t5 = new Date().getTime();
        ArrayList<Integer> seed = new ArrayList<>(Arrays.asList(1, 4, 5));//Don't edit this in any of the methods
        System.out.println(howSum(8, seed, null));
        System.out.println("Tabulation: " + howSumTabulation(8, seed));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t5);

        long t6 = new Date().getTime();
        System.out.println(bestSum(8, seed, null));
        System.out.println("Tabulation: " + bestSumTabulation(8, seed));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t6);

        long t7 = new Date().getTime();
        System.out.println(canConstruct("abcdef", List.of("ab", "abc", "cd", "def", "abcd"), null));
        System.out.println("Tabulation: " + canConstructTabulation("abcdef", List.of("ab", "abc", "cd", "def", "abcd")));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t7);

        long t8 = new Date().getTime();
        System.out.println(countConstruct("abcdef", List.of("ab", "abc", "cd", "def", "abcd", "ef"), null));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t8);

        long t9 = new Date().getTime();
        System.out.println(countConstruct1("abcdef",List.of("ab", "abc", "cd", "def", "abcd", "ef"), null));
        System.out.println(countConstruct1("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeef",
                List.of("e", "ee", "eee", "eeee", "eeeee", "eeeeee"), null));
        System.out.println(countConstructTabulation("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeef",
                List.of("e", "ee", "eee", "eeee", "eeeee", "eeeeee")));
        System.out.println("Tabulation: " + countConstructTabulation("abcdef",
                List.of("ab", "abc", "cd", "def", "abcd", "ef")));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t9);

        long t10 = new Date().getTime();
        System.out.println(allConstruct("abcdef", List.of("ab", "abc", "cd", "def", "abcd", "ef"), null));
        System.out.println(allConstruct("eeeeeeeeeeeeeeeeeeeeeef",
                List.of("e", "ee", "eee", "eeee", "eeeee", "eeeeee"), null));
        System.out.println("Tabulation: " + allConstructTabulation("abcdef",
                List.of("ab", "abc", "cd", "def", "abcd", "ef")));
        System.out.printf("Time: %d%n", (new Date().getTime()) - t10);
    }

    private static long fib(int n, Map<Integer, Long> memo) {
        if (memo == null) memo = new HashMap<>();
        if (n <= 2) return 1;
        if (memo.containsKey(n)) return memo.get(n);
        memo.put(n, fib(n - 1, memo) + fib(n - 2, memo));
        return memo.get(n);
    }

    private static long fibTabulation(int n) {

        var tab = new long[n + 1];
        tab[1] = 1;

        for (int i = 1; i < n; i++) {
            if (i == n - 1)
                tab[i + 1] += tab[i];
            else {
                tab[i + 1] += tab[i];
                tab[i + 2] += tab[i];
            }
        }

        return tab[n];
    }

    private static long gridTraveler(int rows, int columns, Map<String, Long> memo) {
        if (memo == null) memo = new HashMap<>();
        if (rows == 0 || columns == 0) return 0;
        if (rows == 1 && columns == 1) return 1;

        String key = "%s,%s".formatted(rows, columns);
        String flippedKey = "%s,%s".formatted(columns, rows);
        if (memo.containsKey(key))
            return memo.get(key);

        long result = gridTraveler(rows - 1, columns, memo) + gridTraveler(rows, columns - 1, memo);
        memo.put(key, result);
        memo.put(flippedKey, result);

        return result;
    }

    private static long gridTravelerTabulation(int rows, int columns) {

        var tab =
                IntStream.range(0, columns + 1)
                        .mapToObj(operand -> new ArrayList<>(new ArrayList<>(Collections.nCopies(rows + 1, 0L))))
                        .collect(Collectors.toCollection(ArrayList::new));
        var y = tab.get(1);
        y.set(1, 1L);
        tab.set(1, y);

        for (int i = 0; i <= columns; i++) {
            for (int j = 0; j <= rows; j++) {
                var current1 = tab.get(i).get(j);

                if (j + 1 <= columns) tab.get(i).set(j + 1, (tab.get(i).get(j + 1) + current1));
                if (i + 1 <= rows) tab.get(i + 1).set(j, (tab.get(i + 1).get(j) + current1));
            }
        }

        return tab.get(rows).get(columns);
    }

    private static boolean canSum(int target, List<Integer> input, Map<Integer, Boolean> memo) {
        if (memo == null) memo = new HashMap<>();
        if (memo.containsKey(target)) return memo.get(target);
        if (target == 0) return true;
        if (input.stream().allMatch(item -> item > target)) return false;

        Map<Integer, Boolean> finalMemo = memo;
        return input.stream().anyMatch(item -> {
            if (item > target) return false;
            else {
                finalMemo.put(target - item, canSum(target - item,
                        input.stream().filter(x -> x > 0).collect(Collectors.toList()), finalMemo));
                return finalMemo.get(target - item);
            }
        });
    }

    private static boolean canSumTabulation(int target, List<Integer> input) {

        var tab = new boolean[target + 1];
        tab[0] = true;

        for (int i = 0; i < tab.length; i++) {
            for (Integer item : input) {
                if (tab[i] && tab.length > (i + item))
                    tab[i + item] = true;
            }
        }
        System.out.println(Arrays.toString(tab));

        return tab[target];
    }

    private static ArrayList<Integer> howSum(int target, ArrayList<Integer> input, Map<Integer, ArrayList<Integer>> memo) {
        if (memo == null) memo = new HashMap<>();
        if (memo.containsKey(target)) return memo.get(target);
        if (target == 0) return new ArrayList<>();
        if (target < 0) return null;

        for (Integer item : input) {
            int remainder = target - item;
            ArrayList<Integer> result = howSum(remainder, input, memo);
            if (result != null) {
                result.add(item);
                memo.put(target, result);
                return memo.get(target);
            }
        }

        memo.put(target, null);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Integer> howSumTabulation(int target, List<Integer> input) {

        var x = new int[target + 1][];
        var tab = new ArrayList<?>[target + 1];
        tab[0] = new ArrayList<Integer>();

        for (int i = 0; i < tab.length; i++) {
            for (Integer item : input) {
                if (tab[target] != null) return (ArrayList<Integer>) tab[target];
                if (tab[i] != null && tab.length > (i + item)) {
                    var r = new ArrayList<>((ArrayList<Integer>) tab[i]);
                    r.add(item);
                    tab[i + item] = r;
                }
            }
        }

        return (ArrayList<Integer>) tab[target];
    }

    private static ArrayList<Integer> bestSum(int target, ArrayList<Integer> input, Map<Integer, ArrayList<Integer>> memo) {
        if (memo == null) memo = new HashMap<>();
        if (target == 0) return new ArrayList<>();
        if (target < 0) return null;

        for (Integer item : input) {
            int remainder = target - item;
            ArrayList<Integer> result = bestSum(remainder, input, memo);
            if (result != null) {
                result.add(item);
                if (memo.get(target) == null || memo.get(target).size() > result.size())
                    memo.put(target, result);
            }
        }

        return memo.get(target);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Integer> bestSumTabulation(int target, List<Integer> input) {

        var tab = new ArrayList<?>[target + 1];
        tab[0] = new ArrayList<Integer>();

        for (int i = 0; i < tab.length; i++) {
            for (Integer item : input) {
                if (tab[i] != null && tab.length > (i + item)) {
                    var r = new ArrayList<>((ArrayList<Integer>) tab[i]);
                    var subTarget = tab[i + item];
                    if (subTarget == null || subTarget.size() > r.size() + 1) {
                        r.add(item);
                        tab[i + item] = r;
                    }
                }
            }
        }

        return (ArrayList<Integer>) tab[target];
    }

    private static boolean canConstruct(String target, List<String> input, Map<String, Boolean> memo) {
        if (memo == null) memo = new HashMap<>();
        if (memo.containsKey(target)) return memo.get(target);
        if (target.length() == 0) return true;

        Map<String, Boolean> finalMemo = memo;
        return input.stream().anyMatch(item -> {
            if (target.startsWith(item)) {
                var remainder = target.substring(item.length());
                finalMemo.put(remainder, canConstruct(remainder, input, finalMemo));
                return finalMemo.get(remainder);
            }
            else return false;
        });
    }

    private static boolean canConstructTabulation(String target, List<String> input) {

        var tab = new boolean[target.length() + 1];
        tab[0] = true;

        for (int i = 0; i < tab.length; i++) {
            for (String item : input) {
                if (tab[target.length()]) return true;
                if (tab[i] && target.substring(i).startsWith(item) && tab.length > (i + item.length()))
                    tab[i + item.length()] = true;
            }
        }

        return tab[target.length()];
    }

    private static int countConstruct(String target, List<String> input, Map<String, Integer> memo) {
        if (memo == null) memo = new HashMap<>();
        if (target.length() == 0) return 1;

        for (String item : input) {
            if (target.startsWith(item)) {
                var remainder = target.substring(item.length());
                var numWays = memo.get(target);
                memo.put(target,
                        (numWays == null ? 0 : numWays) + (Math.min(countConstruct(remainder, input, memo), 1)));
            }
        }

        var result = memo.get(target);
        return result == null ? 0 : result;
    }

    private static BigInteger countConstruct1(String target, List<String> input, Map<String, BigInteger> memo) {
        if (memo == null) memo = new HashMap<>();
        if (memo.containsKey(target)) return memo.get(target);
        if (target.length() == 0) return new BigInteger(String.valueOf(1));

        var numWays = new BigInteger(String.valueOf(0));

        for (String item : input) {
            if (target.startsWith(item)) {
                var remainder = target.substring(item.length());
                numWays = numWays.add(countConstruct1(remainder, input, memo));
                memo.put(remainder, numWays);
            }
        }

        return numWays;
    }

    private static long countConstructTabulation(String target, List<String> input) {
        var tab = new long[target.length() + 1];
        tab[0] = 1L;

        for (int i = 0; i < tab.length; i++) {
            for (String item : input) {
                if (tab[i] != 0L && target.substring(i).startsWith(item) && tab.length > (i + item.length()))
                    tab[i + item.length()] += tab[i];
            }
        }

        return tab[target.length()];
    }

    private static ArrayList<ArrayList<String>> allConstruct(String target,
                                                             List<String> input,
                                                             Map<String, ArrayList<ArrayList<String>>> memo) {
        if (memo == null) memo = new HashMap<>();
        if (memo.containsKey(target)) return memo.get(target);
        if (target.length() == 0) {
            var list = new ArrayList<ArrayList<String>>();
            list.add(new ArrayList<>());
            return list;
        }

        var result = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < input.size(); i++) {
            var item = input.get(i);
            if (target.startsWith(item)) {
                var remainder = target.substring(item.length());
                var subResult = new ArrayList<>(allConstruct(remainder, input, memo));
                var l = subResult.stream().map(it -> {
                    var r = new ArrayList<>(it);
                    r.add(0, item);
                    return r;
                }).collect(Collectors.toList());
                result.addAll(new ArrayList<>(l));
            }
        }

        memo.put(target, new ArrayList<>(result));
        return result;
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<ArrayList<String>> allConstructTabulation(String target, List<String> input) {

        var tab = new ArrayList<?>[target.length() + 1];
        var baseCase = new ArrayList<ArrayList<String>>();
        baseCase.add(new ArrayList<>());
        tab[0] = baseCase;

        for (int i = 0; i < tab.length; i++) {
            for (String item : input) {
                if (tab[i] != null && target.substring(i).startsWith(item) && tab.length > (i + item.length())) {
                    var subConstruct = ((ArrayList<ArrayList<String>>) tab[i]).stream().map(it -> {
                        var r = new ArrayList<>(it);
                        r.add(item);
                        return r;
                    }).collect(Collectors.toCollection(ArrayList::new));

                    var subTarget = ((ArrayList<ArrayList<String>>) tab[i + item.length()]);
                    if (subTarget == null) subTarget = subConstruct;
                    else subTarget.addAll(subConstruct);

                    tab[i + item.length()] = subTarget;
                }
            }
        }
        System.out.println(Arrays.toString(tab));

        return (ArrayList<ArrayList<String>>) tab[target.length()];
    }
}

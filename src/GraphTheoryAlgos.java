import java.util.*;
import java.util.stream.Collectors;

public class GraphTheoryAlgos {

    public static void main(String[] args) {
        var t1 = new Date().getTime();
        var node1 = new AdjListEdge(0, null);
        var adjList1 = getAdjListMap();
        adjList1.forEach((k, v) -> System.out.printf("%s => %s%n", k, Arrays.toString(v)));
        depthFirstSearch(adjList1, node1);
        adjList1.forEach((k, v) -> System.out.printf("%s => %s%n", k, Arrays.toString(v)));
        System.out.printf("Time: %d%n", new Date().getTime() - t1);

        var t2 = new Date().getTime();
        var adjList2 = getNodeMap();
        adjList2.put(new Node(7), new Node[]{new Node(8)});
        adjList2.put(new Node(8), new Node[]{});
        System.out.println("findComponents: " + findComponents(adjList2));
        System.out.printf("Time: %d%n", new Date().getTime() - t2);

        var t3 = new Date().getTime();
        var adjList3 = getNodeMap();
        var node2 = new Node(0);
        adjList3.forEach((k, v) -> System.out.printf("%s => %s%n", k, Arrays.toString(v)));
        breadFirstSearch(adjList3, node2);
        adjList3.forEach((k, v) -> System.out.printf("%s => %s%n", k, Arrays.toString(v)));
        System.out.printf("Time: %d%n", new Date().getTime() - t3);

        var t4 = new Date().getTime();
        var adjList4 = getNodeMap();
        var node3 = new Node(0);
        var node4 = new Node(6);
        System.out.println("findShortestPath: " + findShortestPathBFS(adjList4, node3, node4));
        System.out.printf("Time: %d%n", new Date().getTime() - t4);

        var t5 = new Date().getTime();
        var grid = getGrid();
        var node5 = new GridNode(1, 0, 'S');
        var node6 = new GridNode(3, 3, 'E');
        System.out.println("findShortestPathGrid: " + findShortestPathGridBFS(grid, node5, node6));
        System.out.printf("Time: %d%n", new Date().getTime() - t5);

        var t6 = new Date().getTime();
        var adjList5 = getNodeMap();
        adjList5.put(new Node(7), new Node[]{new Node(8)});
        adjList5.put(new Node(8), new Node[]{});
        System.out.println("topSort: " + topSort(adjList5).stream().map(it -> it.vertex).collect(Collectors.toList()));
        System.out.printf("Time: %d%n", new Date().getTime() - t6);

        var t7 = new Date().getTime();
        var adjList6 = getNodeMap();
        var adjList7 = getAdjListMap();
        System.out.println("shortestPathDAG: " + shortestPathDAG(adjList6, adjList7, 2));
        System.out.printf("Time: %d%n", new Date().getTime() - t7);

        var t8 = new Date().getTime();
        var adjList8 = getNodeMap();
        var adjList9 = getAdjListMap();
        System.out.println("longestPathDAG: " + longestPathDAG(adjList8, adjList9, 2));
        System.out.printf("Time: %d%n", new Date().getTime() - t8);

        var t9 = new Date().getTime();
        var adjList10 = getAdjListMap();
        System.out.println("dijkstraSSSP: " + dijkstraSSSPath(2, adjList10)
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        i -> "dist->%d prev->%d".formatted(i.getValue().getKey(), i.getValue().getValue()))));
        System.out.printf("Time: %d%n", new Date().getTime() - t9);

        var t10 = new Date().getTime();
        var adjList11 = getAdjListMap();
        System.out.println("dijkstraShortestPath: " + dijkstraShortestPath(2, 6, adjList11));
        System.out.printf("Time: %d%n", new Date().getTime() - t10);

        var t11 = new Date().getTime();
        var adjList12 = getAdjListMap();
        System.out.println("bellmanFordSSSP: " + bellmanFordSSSP(2, adjList12));
        System.out.printf("Time: %d%n", new Date().getTime() - t11);

        var t12 = new Date().getTime();
        var adjList13 = getAdjListMap();
        var i = new AdjListEdge[adjList13.get(5).length + 1];
        System.arraycopy(adjList13.get(5), 0, i, 0, adjList13.get(5).length);
        i[adjList13.get(5).length] = new AdjListEdge(7, 1);
        adjList13.replace(5, i);
        adjList13.put(7, new AdjListEdge[]{new AdjListEdge(8, 1)});
        adjList13.put(8, new AdjListEdge[]{new AdjListEdge(5, -3)});
        System.out.println("bellmanFordNegativeNodes: " + bellmanFordNegativeNodes(2, adjList13));
        System.out.printf("Time: %d%n", new Date().getTime() - t12);
    }

    private static void depthFirstSearch(Map<Integer, AdjListEdge[]> adjList, AdjListEdge node) {
        if (node.isVisited) return;
        node.isVisited = true;

        var neighbours = adjList.get(node.toVertex);
        for (AdjListEdge item : neighbours) {
            depthFirstSearch(adjList, item);
        }
    }

    private static HashMap<Integer, HashSet<Node>> findComponents(Map<Node, Node[]> adjList) {
        var componentCount = 0;
        var componentMap = new HashMap<Integer, HashSet<Node>>();
        var nodes = new ArrayList<>(adjList.keySet());

        for (Node node : nodes) {
            if (!node.isVisited) {
                componentCount++;
                findComponentsDfs(adjList, nodes, node, componentCount, componentMap);
            }
        }

        return componentMap;
    }

    private static void findComponentsDfs(Map<Node, Node[]> adjList,
                                          List<Node> nodes,
                                          Node node,
                                          int componentCount,
                                          HashMap<Integer, HashSet<Node>> componentMap) {
        node.isVisited = true;
        var value = ((componentMap.get(componentCount) == null)
                ? new HashSet<Node>()
                : componentMap.get(componentCount));
        value.add(node);
        componentMap.put(componentCount, value);

        var neighbours = adjList.get(node);
        for (Node neighbour : neighbours) {
            var nodeIndex = nodes.indexOf(neighbour);
            if (!nodes.get(nodeIndex).isVisited)
                findComponentsDfs(adjList, nodes, nodes.get(nodeIndex), componentCount, componentMap);
        }
    }

    private static void breadFirstSearch(Map<Node, Node[]> adjList, Node startNode) {
        var nodes = new ArrayList<>(adjList.keySet());
        var queue = new ArrayDeque<Node>();

        startNode = nodes.get(nodes.indexOf(startNode));
        queue.add(startNode);
        startNode.isVisited = true;

        while (!queue.isEmpty()) {
            var node = queue.remove();

            for (Node neighbour : adjList.get(node)) {
                var nodeIndex = nodes.indexOf(neighbour);
                var next = nodes.get(nodeIndex);
                if (!next.isVisited){
                    queue.add(next);
                    next.isVisited = true;
                }
            }
        }
    }

    private static List<GridNode> findValidGridNeighbours(int numRows, int numColumns, GridNode node) {
        //define valid directions
        var rowVectors = Arrays.asList(-1, 0, 1, 0, -1, -1, 1, 1);
        var columnVectors = Arrays.asList(0, 1, 0, -1, -1, 1, 1, -1);

        var result = new ArrayList<GridNode>();

        for (int i = 0; i < 8; i++) {
            var neighbour = new GridNode(node.row + rowVectors.get(i), node.column + columnVectors.get(i));

            if (neighbour.row < 0 || neighbour.column < 0) continue;
            if (neighbour.row >= numRows || neighbour.column >= numColumns) continue;

            //neighbour is valid
            result.add(neighbour);
        }

        return result;
    }

    private static Map<GridNode, GridNode> getPrevPathMapGridBFS(List<List<GridNode>> grid, GridNode startNode) {
        var nodes = grid.stream().flatMap(Collection::stream).collect(Collectors.toList());
        var queue = new ArrayDeque<GridNode>();
        var prevMap = new HashMap<GridNode, GridNode>();

        startNode = nodes.get(nodes.indexOf(startNode));
        queue.add(startNode);
        startNode.isVisited = true;

        while (!queue.isEmpty()) {
            var node = queue.remove();

            for (GridNode neighbour : findValidGridNeighbours(grid.size(), grid.get(0).size(), node)) {
                var nodeIndex = nodes.indexOf(neighbour);
                var next = nodes.get(nodeIndex);
                if (!next.isVisited) {
                    next.isVisited = true;
                    if (next.content == 'A') continue;
                    queue.add(next);
                    prevMap.put(next, node);
                    if (next.content == 'E') return prevMap;
                }
            }
        }

        return Collections.EMPTY_MAP;
    }

    private static Map<Node, Node> getPrevPathMapBFS(Map<Node, Node[]> adjList, Node startNode) {
        var nodes = new ArrayList<>(adjList.keySet());
        var queue = new ArrayDeque<Node>();
        var prevMap = new HashMap<Node, Node>();

        startNode = nodes.get(nodes.indexOf(startNode));
        queue.add(startNode);
        startNode.isVisited = true;

        while (!queue.isEmpty()) {
            var node = queue.remove();

            for (Node neighbour : adjList.get(node)) {
                var nodeIndex = nodes.indexOf(neighbour);
                var next = nodes.get(nodeIndex);
                if (!next.isVisited){
                    queue.add(next);
                    next.isVisited = true;
                    prevMap.put(next, node);
                }
            }
        }

        return prevMap;
    }

    private static List<GridNode> findShortestPathGridBFS(List<List<GridNode>> grid, GridNode startNode, GridNode endNode) {
        var prevPath = getPrevPathMapGridBFS(grid, startNode);
        var result = new ArrayList<GridNode>();

        if (!prevPath.isEmpty()) {
            //construct path
            result.add(endNode);
            var prev = prevPath.get(endNode);
            while (prev != null) {
                result.add(prev);
                prev = prevPath.get(prev);
            }

            Collections.reverse(result);
            return result;
        } else return Collections.EMPTY_LIST;
    }

    private static List<Node> findShortestPathBFS(Map<Node, Node[]> adjList,
                                                  Node startNode, Node endNode) {
        var prevPath = getPrevPathMapBFS(adjList, startNode);
        var result = new ArrayList<Node>();

        if (prevPath.containsKey(endNode)) {
            //construct path
            result.add(endNode);
            var prev = prevPath.get(endNode);
            while(prev != null) {
                result.add(prev);
                prev = prevPath.get(prev);
            }

            Collections.reverse(result);
            return result;
        } else return Collections.EMPTY_LIST;
    }

    private static List<Node> topSort(Map<Node, Node[]> adjList) {
        var nodes = new ArrayList<>(adjList.keySet());
        var result = new ArrayList<Node>(Collections.nCopies(nodes.size(), null));
        var insertIndex = nodes.size() - 1;

        for (Node node : nodes) {
            if (!node.isVisited) insertIndex = topSortDFS(result, adjList, nodes, node, insertIndex);
        }

        return result;
    }

    private static int topSortDFS(List<Node> topOrdering,
                                  Map<Node, Node[]> adjList,
                                  List<Node> nodes,
                                  Node node,
                                  int insertIndex) {
        node.isVisited = true;

        var neighbours = adjList.get(node);

        for (Node neighbour : neighbours) {
            var index = nodes.indexOf(neighbour);
            var next = nodes.get(index);

            if (!next.isVisited) insertIndex = topSortDFS(topOrdering, adjList, nodes, next, insertIndex);
        }
        topOrdering.set(insertIndex, node);
        return --insertIndex;
    }

    private static Map<Integer, Integer> shortestPathDAG(HashMap<Node, Node[]> topAdjList,
                                                         HashMap<Integer, AdjListEdge[]> adjList,
                                                         Integer startVertex) {
        var topOrdering = topSort(topAdjList).stream().map(it -> it.vertex).collect(Collectors.toList());
        var result = new HashMap<Integer, Integer>();
        var startIndex = topOrdering.indexOf(startVertex);
        if (startIndex < 0) return null;
        result.put(topOrdering.get(startIndex), 0);

        //loop over vertices starting from startVertex
        for (int i = startIndex; i < topOrdering.size(); i++) {
            Integer vertex = topOrdering.get(i);
            var neighbours = adjList.get(vertex);
            for (AdjListEdge neighbour : neighbours) {
                var newWeight = result.get(vertex) + neighbour.weight;
                var existingWeight = result.get(neighbour.toVertex);
                //relaxation step
                if (existingWeight == null || existingWeight > newWeight)
                    result.put(neighbour.toVertex, newWeight);
            }
        }

        return result;
    }

    private static Map<Integer, Integer> longestPathDAG(HashMap<Node, Node[]> topAdjList,
                                                        HashMap<Integer, AdjListEdge[]> adjList,
                                                        Integer startVertex) {
        var topOrdering = topSort(topAdjList).stream().map(it -> it.vertex).collect(Collectors.toList());
        var result = new HashMap<Integer, Integer>();
        var startIndex = topOrdering.indexOf(startVertex);
        if (startIndex < 0) return null;
        result.put(topOrdering.get(startIndex), 0);

        adjList.values().forEach(it -> {for (AdjListEdge node : it) {node.weight *= -1;}});

        //loop over vertices starting from startVertex
        for (int i = startIndex; i < topOrdering.size(); i++) {
            Integer vertex = topOrdering.get(i);
            var neighbours = adjList.get(vertex);
            for (AdjListEdge neighbour : neighbours) {
                var newWeight = result.get(vertex) + neighbour.weight;
                var existingWeight = result.get(neighbour.toVertex);
                //relaxation step
                if (existingWeight == null || existingWeight > newWeight)
                    result.put(neighbour.toVertex, newWeight);
            }
        }

        result.replaceAll((k, v) -> v *= -1);
        return result;
    }

    private static HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> dijkstraSSSPath(Integer startVertex,
                                                                                               Map<Integer, AdjListEdge[]> adjList) {
        // major difference between Dijkstra and BFS (and variants) is the check for visited. In dijkstra, neighbours
        // are only marked as visited when they are accessed from the queue, not when they are first encountered as neighbours.
        // This allows for multiple visitation of the same node as it can be added to the priorityQueue more thann once,
        // hence the O(E*log(V)) complexity
        var visited = new HashMap<Integer, Boolean>();
        //HashMap of vertex(key) to  distance & previous node(value) respectively
        var result = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        var priorityQueue = new PriorityQueue<AbstractMap.SimpleEntry<Integer, Integer>>(Map.Entry.comparingByValue());

        result.put(startVertex, new AbstractMap.SimpleEntry<>(0, null));
        priorityQueue.add(new AbstractMap.SimpleEntry<>(startVertex, 0));

        while (!priorityQueue.isEmpty()) {
            var tuple = priorityQueue.remove();
            visited.put(tuple.getKey(), true);
            var dist = result.get(tuple.getKey());
            if (dist != null && dist.getKey() < tuple.getValue()) continue;

            for (AdjListEdge neighbour : adjList.get(tuple.getKey())) {
                if (visited.get(neighbour.toVertex) != null) continue;
                var newDistance = result.get(tuple.getKey()).getKey() + neighbour.weight;
                var existingDistance = result.get(neighbour.toVertex);
                if (existingDistance == null || newDistance < existingDistance.getKey()) {
                    result.put(neighbour.toVertex, new AbstractMap.SimpleEntry<>(newDistance, tuple.getKey()));
                    priorityQueue.add(new AbstractMap.SimpleEntry<>(neighbour.toVertex, newDistance));
                }
            }
        }

        return result;
    }

    private static HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> dijkstraSSSPathWithEndVertex(Integer startVertex,
                                                                                               Integer endVertex,
                                                                                               Map<Integer, AdjListEdge[]> adjList) {
        // major difference between Dijkstra and BFS (and variants) is the check for visited. In dijkstra, neighbours
        // are only marked as visited when they are accessed from the queue, not when they are first encountered as neighbours.
        // This allows for multiple visitation of the same node as it can be added to the priorityQueue more thann once,
        // hence the O(E*log(V)) complexity
        var visited = new HashMap<Integer, Boolean>();
        //HashMap of vertex(key) to  distance & previous node(value) respectively
        var result = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        var priorityQueue = new PriorityQueue<AbstractMap.SimpleEntry<Integer, Integer>>(Map.Entry.comparingByValue());

        result.put(startVertex, new AbstractMap.SimpleEntry<>(0, null));
        priorityQueue.add(new AbstractMap.SimpleEntry<>(startVertex, 0));

        while (!priorityQueue.isEmpty()) {
            var tuple = priorityQueue.remove();
            visited.put(tuple.getKey(), true);
            var dist = result.get(tuple.getKey());
            if (dist != null && dist.getKey() < tuple.getValue()) continue;

            for (AdjListEdge neighbour : adjList.get(tuple.getKey())) {
                if (visited.get(neighbour.toVertex) != null) continue;
                var newDistance = result.get(tuple.getKey()).getKey() + neighbour.weight;
                var existingDistance = result.get(neighbour.toVertex);
                if (existingDistance == null || newDistance < existingDistance.getKey()) {
                    result.put(neighbour.toVertex, new AbstractMap.SimpleEntry<>(newDistance, tuple.getKey()));
                    priorityQueue.add(new AbstractMap.SimpleEntry<>(neighbour.toVertex, newDistance));
                }
            }
            if (tuple.getKey().equals(endVertex)) {
                return result;
            }
        }

        //noinspection unchecked
        return (HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>) Collections.EMPTY_MAP;
    }

    private static List<Integer> dijkstraShortestPath(Integer startVertex,
                                                      Integer endVertex,
                                                      Map<Integer, AdjListEdge[]> adjList) {
        //call endVertex-based early exit dijkstraSSSP
        var sssp = dijkstraSSSPathWithEndVertex(startVertex, endVertex, adjList);
        if (sssp.isEmpty()) //noinspection unchecked
            return Collections.EMPTY_LIST;

        var path = new ArrayList<Integer>();

        for (Integer i = endVertex; i != null; i = sssp.get(i) == null ? null : sssp.get(i).getValue()) {
            path.add(i);
        }
        Collections.reverse(path);
        return path;
    }

    private static Map<Integer, Double> bellmanFordSSSP(Integer startNode,
                                        Map<Integer, AdjListEdge[]> adjList) {
        var dist = new HashMap<Integer, Double>();
        var vertices = adjList.keySet().stream().filter(it -> it >= startNode).collect(Collectors.toList());
        System.out.println(vertices);
        dist.put(startNode, 0d);

        for (int i = 0; i < vertices.size() - 1; i++) {
            for (Integer from : vertices) {
                for (AdjListEdge adjListEdge : adjList.get(from)) {
                    var fromWeight = dist.get(from) == null ? Double.POSITIVE_INFINITY : dist.get(from);
                    var currentDistance = dist.get(adjListEdge.toVertex) == null ? Double.POSITIVE_INFINITY : dist.get(adjListEdge.toVertex);
                    if (fromWeight +  adjListEdge.weight < currentDistance)
                        dist.put(adjListEdge.toVertex, fromWeight + adjListEdge.weight);
                    System.out.println(dist);
                }
            }
        }

        return dist;
    }

    private static Map<Integer, Double> bellmanFordNegativeNodes(Integer startNode,
                                                                  Map<Integer, AdjListEdge[]> adjList) {
        var vertices = adjList.keySet().stream().filter(it -> it >= startNode).collect(Collectors.toList());
        var dist = bellmanFordSSSP(startNode, adjList);
        System.out.println(dist);

        for (int i = 0; i < vertices.size() - 1; i++) {
            for (Integer from : vertices) {
                for (AdjListEdge adjListEdge : adjList.get(from)) {
                    var fromWeight = dist.get(from) == null ? Double.POSITIVE_INFINITY : dist.get(from);
                    var currentDistance = dist.get(adjListEdge.toVertex) == null ? Double.POSITIVE_INFINITY : dist.get(adjListEdge.toVertex);
                    if (fromWeight +  adjListEdge.weight < currentDistance)
                        dist.put(adjListEdge.toVertex, Double.NEGATIVE_INFINITY);
                }
            }
        }

        return dist;
    }

    private static HashMap<Integer, AdjListEdge[]> getAdjListMap() {
        var origAdjList = new HashMap<Integer, AdjListEdge[]>();
        origAdjList.put(0, new AdjListEdge[] {new AdjListEdge(2, 3), new AdjListEdge(1, 7)});
        origAdjList.put(1, new AdjListEdge[]{});
        origAdjList.put(2, new AdjListEdge[]{new AdjListEdge(4, 8), new AdjListEdge(1, 2)});
        origAdjList.put(4, new AdjListEdge[]{new AdjListEdge(5, 1), new AdjListEdge(6, 2)});
        origAdjList.put(5, new AdjListEdge[]{new AdjListEdge(6, 7)});
        origAdjList.put(6, new AdjListEdge[]{});
        return origAdjList;
    }

    private static HashMap<Node, Node[]> getNodeMap() {
        var origAdjList = new HashMap<Node, Node[]>();
        origAdjList.put(new Node(0), new Node[]{new Node(2), new Node(1)});
        origAdjList.put(new Node(1), new Node[]{});
        origAdjList.put(new Node(2), new Node[]{new Node(4), new Node(1)});
        origAdjList.put(new Node(4), new Node[]{new Node(5), new Node(6)});
        origAdjList.put(new Node(5), new Node[]{new Node(6)});
        origAdjList.put(new Node(6), new Node[]{});
        return origAdjList;
    }

    private static List<List<GridNode>> getGrid() {
        var row1 = Arrays.asList(new GridNode(0, 0), new GridNode(0, 1),
                new GridNode(0, 2, 'A'), new GridNode(0, 3),
                new GridNode(0, 4, 'A'));
        var row2 = Arrays.asList(new GridNode(1, 0, 'S'),
                new GridNode(1, 1), new GridNode(1, 2), new GridNode(1, 3),
                new GridNode(1, 4));
        var row3 = Arrays.asList(new GridNode(2, 0), new GridNode(2, 1),
                new GridNode(2, 2, 'A'), new GridNode(2, 3), new GridNode(2, 4));
        var row4 = Arrays.asList(new GridNode(3, 0, 'A'),
                new GridNode(3, 1), new GridNode(3, 2), new GridNode(3, 3, 'E'),
                new GridNode(3, 4, 'A'));

        return Arrays.asList(row1, row2, row3, row4);
    }

    public static class AdjListEdge {
        Integer toVertex;
        Integer weight;
        boolean isVisited = false;

        @Override
        public String toString() {
            return "(toVertex= %d, weight= %d, isVisited= %s)".formatted(toVertex, weight, isVisited);
        }

        public AdjListEdge(Integer toVertex, Integer weight) {
            this.toVertex = toVertex;
            this.weight = weight;
        }
    }

    public static class Node {
        Integer vertex;
        boolean isVisited = false;

        public Node(Integer vertex) {
            this.vertex = vertex;
        }

        @Override
        public String toString() {
            return "(vertex = %d, isVisited = %s)".formatted(vertex, isVisited);
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == this.getClass() && vertex.equals(((Node) obj).vertex);
        }

        @Override
        public int hashCode() {
            return vertex;
        }
    }

    public static class GridNode {
        Integer row;
        Integer column;
        Character content;
        boolean isVisited = false;

        public GridNode(Integer row, Integer column) {
            this.row = row;
            this.column = column;
            this.content = '.';
        }

        public GridNode(Integer row, Integer column, Character content) {
            this.row = row;
            this.column = column;
            this.content = content;
        }

        @Override
        public String toString() {
            return "(row = %d, column = %d, content = %s, isVisited = %s)".formatted(row, column, content, isVisited);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GridNode gridNode = (GridNode) o;

            if (!row.equals(gridNode.row)) return false;
            return column.equals(gridNode.column);
        }

        @Override
        public int hashCode() {
            int result = row.hashCode();
            result = 31 * result + column.hashCode();
            return result;
        }
    }
}

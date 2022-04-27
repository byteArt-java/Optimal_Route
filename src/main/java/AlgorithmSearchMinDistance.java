import java.util.*;

public class AlgorithmSearchMinDistance implements Graph{
    private final List<City> vertexList;//список городов
    private final int[][] adjacencyMatrix;//расстояние от одного города, до другого
    private final boolean[][] adjacencyMatrixB;//можно ли проехать с одного города до другого

    public AlgorithmSearchMinDistance(int maxVertexCount) {
        this.vertexList = new ArrayList<>(maxVertexCount);
        this.adjacencyMatrix = new int[maxVertexCount][maxVertexCount];
        this.adjacencyMatrixB = new boolean[maxVertexCount][maxVertexCount];
    }

    @Override public void addVertex(String label) {
        vertexList.add(new City(label));
    }

    @Override public boolean addEdge(String startLabel, String secondLabel, String... others) {
        boolean result = addEdge(startLabel, secondLabel);
        for (String other : others) {
            result &= addEdge(startLabel, other);
        }
        return result;
    }

    @Override public boolean addEdge(String startLabel, String secondLabel,int distance) {
        int startIndex = indexOf(startLabel);
        int endIndex = indexOf(secondLabel);
        if (startIndex == -1 || endIndex == -1){
            return false;
        }
        adjacencyMatrix[startIndex][endIndex] = distance;
        adjacencyMatrixB[startIndex][endIndex] = true;
        return true;
    }

    @Override public boolean addEdge(String startLabel, String secondLabel) {
        int startIndex = indexOf(startLabel);
        int endIndex = indexOf(secondLabel);
        if (startIndex == -1 || endIndex == -1){
            return false;
        }
        adjacencyMatrixB[startIndex][endIndex] = true;//тут находится вес
        return true;
    }

    @Override public int indexOf(String label) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i).getLabel().equals(label)){
                return i;
            }
        }
        return -1;
    }

    @Override public int getSize() {
        return vertexList.size();
    }

    @Override public void display() {
        System.out.println(this);
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            sb.append(vertexList.get(i));
            for (int j = 0; j < getSize(); j++) {
                if (adjacencyMatrix[i][j] > 0){
                    sb.append(" -> ").append(vertexList.get(j));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override public Map<String,Integer> dfs(String startCity, String endCity) {
        Map<String,Integer> pathCitiesAndDistance = new HashMap<>();
        int tempTotalDistancePath = Integer.MAX_VALUE;
        StringBuilder sb = new StringBuilder();
        int startIndex = indexOf(startCity);
        if (startIndex == -1){
            throw new IllegalArgumentException("Incorrect City " + startCity);
        }
        Stack<City> stack = new Stack<>();
        City city = vertexList.get(startIndex);
        visitVertex(stack,city,sb,endCity);
        int localTempDistance = 0;
        while (!stack.isEmpty()){
            city = getNearUnvisitedCity(stack.peek(),endCity);
            if (city == null){
                if (stack.size() == 1){
                    break;
                }
                int j = indexOf(stack.pop().getLabel());
                int i = indexOf(stack.peek().getLabel());
                localTempDistance += getAdjacencyMatrix()[i][j];
            }else {
                visitVertex(stack,city,sb,endCity);
            }
            //этот блок сравнивает 2 расстояния и в зависимости от этого обнуляет и добавляет инфу когда достигаем,
            // начала. Также если у города более 1 ответвления, то нужно у этого города setVisited установить false
            if (stack.size() == 1 && stack.peek().getVisited() && localTempDistance < tempTotalDistancePath){
                tempTotalDistancePath = localTempDistance;
                pathCitiesAndDistance.put(sb.toString(),localTempDistance);
                localTempDistance = 0;
                sb.setLength(0);
                sb.append(startCity).append(" -> ");
                for (City city1 : vertexList) {
                    if (city1.getCountBranch() > 1){
                        city1.setVisited(false);
                        int countBranchOld = city1.getCountBranch();
                        city1.setCountBranch(--countBranchOld);
                    }
                }
            }
        }

        String cities = "";
        int distance = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> pair : pathCitiesAndDistance.entrySet()) {
            if (pair.getValue() < distance){
                distance = pair.getValue();
                cities = pair.getKey();
            }
        }
        pathCitiesAndDistance = new HashMap<>();
        pathCitiesAndDistance.put(cities,distance);
//        System.out.println(Arrays.toString(new Map[]{pathCitiesAndDistance}));
        return pathCitiesAndDistance;
    }


    private City getNearUnvisitedCity(City city, String destinationCity) {
        int currentIndex = vertexList.indexOf(city);
        City cityReturn = null;
        boolean isFined = false;
        for (int i = 0; i < getSize(); i++) {
            if (adjacencyMatrixB[currentIndex][i] && !vertexList.get(i).getVisited() && !isFined){
                adjacencyMatrixB[currentIndex][i] = false;
                cityReturn = vertexList.get(i);
                isFined = true;
                continue;
            }
            if (adjacencyMatrixB[currentIndex][i] && !vertexList.get(i).getVisited()){
                int a = city.getCountBranch();
                city.setCountBranch(++a);
            }
        }
        return cityReturn;
    }

    private City getNearUnvisitedCity(City city) {
        int currentIndex = vertexList.indexOf(city);
        for (int i = 0; i < getSize(); i++) {
            if (adjacencyMatrix[currentIndex][i] > 0 && !vertexList.get(i).getVisited()){
                return vertexList.get(i);
            }
        }
        return null;
    }

    private void visitVertex(Stack<City> stack, City city,StringBuilder sb,String destinationCity) {
        stack.push(city);
        if (city.getLabel().equalsIgnoreCase(destinationCity)){
            city.setVisited(false);
            sb.append(city.getLabel());
        }else {
            city.setVisited(true);
            sb.append(city.getLabel()).append(" -> ");
        }
    }

    private void visitVertex(Queue<City> queue, City city) {
        System.out.println(city.getLabel() + " ");
        queue.add(city);
        city.setVisited(true);
    }

    @Override public void bfs(String startLabel) {//Москва
        int startIndex = indexOf(startLabel);
        if (startIndex == -1){
            throw new IllegalArgumentException("неверная вершина " + startLabel);
        }
        Queue<City> queue = new LinkedList<>();
        City city = vertexList.get(startIndex);
        visitVertex(queue,city);
        while (!queue.isEmpty()){
            city = getNearUnvisitedCity(queue.peek());
            if (city == null){
                queue.remove();
            }else {
                visitVertex(queue,city);
            }
        }
    }

    @Override public List<City> getCityList() {
        return vertexList;
    }

    @Override public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    @Override public boolean[][] getAdjacencyMatrixB() {
        return adjacencyMatrixB;
    }

    @Override public void printInfoMatrix(){
        for (int i = 0; i < getAdjacencyMatrix().length; i++) {
            for (int j = 0; j < getAdjacencyMatrix()[i].length; j++) {
                System.out.printf("%d.%d ",j,getAdjacencyMatrix()[i][j]);
            }
            System.out.println();
        }
    }
}

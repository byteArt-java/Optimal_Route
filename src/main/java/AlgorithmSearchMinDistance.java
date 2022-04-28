import java.util.*;

public class AlgorithmSearchMinDistance implements Graph{
    private final List<City> vertexList;//список городов
    private final int[][] adjacencyMatrix;//расстояние от одного города, до другого
    private final boolean[][] adjacencyMatrixB;//можно ли проехать с одного города до другого
    static boolean nextSetVisited = false;//если город до этого имел 2 ссылки, то prev города, тоже не должны быть посещенными

    public AlgorithmSearchMinDistance(int maxVertexCount) {
        this.vertexList = new ArrayList<>(maxVertexCount);
        this.adjacencyMatrix = new int[maxVertexCount][maxVertexCount];
        this.adjacencyMatrixB = new boolean[maxVertexCount][maxVertexCount];
    }

    @Override public void addVertex(String label) {
        vertexList.add(new City(label));
    }

    @Override public boolean addEdge(String startLabel, String secondLabel,int distance) {
        int startIndex = indexOf(startLabel);
        int endIndex = indexOf(secondLabel);
        int oldCountReferenced = vertexList.get(endIndex).getCountReferenced();
        vertexList.get(endIndex).setCountReferenced(++oldCountReferenced);
        if (startIndex == -1 || endIndex == -1){
            return false;
        }
        adjacencyMatrix[startIndex][endIndex] = distance;
        adjacencyMatrixB[startIndex][endIndex] = true;
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
            city = getNearUnvisitedCity(stack.peek(),startCity,stack);
            //если самый начальный город не имеет ветвей, то конец выходим из цикла
            if (vertexList.get(startIndex).getCountBranch() == 0){
                break;
            }
            //когда после города назначения идет null
            if (city == null){
                stack.clear();
                int oldCountBranch = vertexList.get(startIndex).getCountBranch();
                vertexList.get(startIndex).setCountBranch(--oldCountBranch);
                stack.push(vertexList.get(startIndex));
                sb.append(startCity).append(" -> ");
                //когда мы нашли след город
            }else {
                int j = indexOf(city.getLabel());
                int i = indexOf(stack.peek().getLabel());
                localTempDistance += getAdjacencyMatrix()[i][j];
                visitVertex(stack,city,sb,endCity);
            }
            //этот блок проверяет дошли ли мы до конца, и если дошли, то скидывает в Map список городов и общее расстояние
            if (stack.peek().getLabel().equals(endCity) && localTempDistance < tempTotalDistancePath){
                tempTotalDistancePath = localTempDistance;
                pathCitiesAndDistance.put(sb.toString(),localTempDistance);
                localTempDistance = 0;
                sb.setLength(0);
            }
        }
        //===============находим минимальное расстояние из списка маршрутов
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
        //==================================================================
        System.out.println(Arrays.toString(new Map[]{pathCitiesAndDistance}));
        return pathCitiesAndDistance;
    }


    private City getNearUnvisitedCity(City city, String startCity,Stack stack) {
        int currentIndex = vertexList.indexOf(city);
        City cityReturn = null;
        boolean isFinedOneCity = false;
        for (int i = 0; i < getSize(); i++) {
            if (adjacencyMatrixB[currentIndex][i] && !vertexList.get(i).getVisited() && !isFinedOneCity){
                cityReturn = vertexList.get(i);
                isFinedOneCity = true;
                continue;
            }
            //ситуация при которой мы еще находим ветку
            if (adjacencyMatrixB[currentIndex][i] && !vertexList.get(i).getVisited()){
                int oldCountBranch = vertexList.get(indexOf(startCity)).getCountBranch();
                vertexList.get(indexOf(startCity)).setCountBranch(++oldCountBranch);
                vertexList.get(currentIndex).setVisited(false);

                //если мы еще нашли у кого то ветку, то устанавливаем знач SetVisited= false, чтобы потом еще раз пройтись
                for (int j = 0; j < stack.size(); j++) {
                    City city1 = (City) stack.get(j);
                    if (city1.getVisited()){
                        city1.setVisited(false);
                    }
                }
            }
        }
        return cityReturn;
    }

    private void visitVertex(Stack<City> stack, City city,StringBuilder sb,String destinationCity) {
        stack.push(city);
        if (city.getLabel().equalsIgnoreCase(destinationCity)){
            city.setVisited(false);
            sb.append(city.getLabel());
        }else {
            city.setVisited(!nextSetVisited);
            sb.append(city.getLabel()).append(" -> ");
        }
        //если больше двух веток, то нужно установить false setVisited, чтобы мы еще раз могли пройтись по ней
        if (city.getCountBranch() > 1){
            city.setVisited(false);
            sb.append(city.getLabel()).append(" -> ");
        }
        if (city.getCountReferenced() > 1){
            int oldCountReferenced = vertexList.get(indexOf(city.getLabel())).getCountReferenced();
            vertexList.get(indexOf(city.getLabel())).setCountReferenced(--oldCountReferenced);
            nextSetVisited = true;
            city.setVisited(false);
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

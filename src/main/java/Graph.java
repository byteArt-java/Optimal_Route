import java.util.List;
import java.util.Map;

public interface Graph {
    void addVertex(String label);
    boolean addEdge(String startLabel,String secondLabel,String... other);
    boolean addEdge(String startLabel,String secondLabel,int distance);
    boolean addEdge(String startLabel,String secondLabel);
    int getSize();
    void display();
    List<City> getCityList();
    int[][] getAdjacencyMatrix();
    boolean[][] getAdjacencyMatrixB();
    void printInfoMatrix();
    int indexOf(String label);

    //Depth first search
    Map<String,Integer> dfs(String startCity, String endCity);//return minDistance and List Cities
    //breadth first search
    void bfs(String startLabel);
}

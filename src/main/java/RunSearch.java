public class RunSearch {
    private final static String[] citiStr = {"Москва","Ярославль","Кострома","Шарья","Киров","Пермь","Кунгур",
            "Екатеринбург", "Владимир","Нижний Новгород",
            "Чебоксары","Нижнекамск","Ижевск","Уфа","Челябинск","Рязань","Пенза","Самара","Салават","Курган"};

    public static void main(String[] args) {
//        testGraph();
//        graphDfs();
//        graphBfs();
        minDistance("Москва","Курган");
    }

    private static void minDistance(String sourceCity, String destinationCity){
        Graph graph = new AlgorithmSearchMinDistance(citiStr.length);
        for (int i = 0; i < citiStr.length; i++) {
            graph.addVertex(citiStr[i]);
        }
        addDistance(graph);
        graph.dfs(sourceCity, destinationCity);
    }

    private static void addDistance(Graph graph){
        graph.addEdge("Москва","Владимир",185);
        graph.addEdge("Владимир","Нижний Новгород",236);
        graph.addEdge("Нижний Новгород","Чебоксары",242);
        graph.addEdge("Чебоксары","Нижнекамск",400);

        graph.addEdge("Чебоксары","Курган",16);

        graph.addEdge("Нижнекамск","Ижевск",213);
        graph.addEdge("Ижевск","Уфа",340);
        graph.addEdge("Уфа","Челябинск",420);
        graph.addEdge("Челябинск","Курган",273);

        graph.addEdge("Владимир","Рязань",239);
        graph.addEdge("Рязань","Пенза",440);
        graph.addEdge("Пенза","Самара",420);
        graph.addEdge("Самара","Салават",560);
        graph.addEdge("Салават","Уфа",166);


        graph.addEdge("Москва","Ярославль",265);
        graph.addEdge("Ярославль","Кострома",84);
        graph.addEdge("Кострома","Шарья",320);
        graph.addEdge("Шарья","Киров",297);
        graph.addEdge("Киров","Пермь",530);
        graph.addEdge("Пермь","Кунгур",91);
        graph.addEdge("Кунгур","Екатеринбург",281);
        graph.addEdge("Екатеринбург","Курган",570);
    }
}

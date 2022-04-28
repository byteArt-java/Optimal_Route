public class RunSearch {
    private final static String[] citiStr = {"������","���������","��������","�����","�����","�����","������",
            "������������", "��������","������ ��������",
            "���������","����������","������","���","���������","������","�����","������","�������","������"};

    public static void main(String[] args) {
//        testGraph();
//        graphDfs();
//        graphBfs();
        minDistance("������","������");
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
        graph.addEdge("������","��������",185);
        graph.addEdge("��������","������ ��������",236);
        graph.addEdge("������ ��������","���������",242);
        graph.addEdge("���������","����������",400);

        graph.addEdge("���������","������",16);

        graph.addEdge("����������","������",213);
        graph.addEdge("������","���",340);
        graph.addEdge("���","���������",420);
        graph.addEdge("���������","������",273);

        graph.addEdge("��������","������",239);
        graph.addEdge("������","�����",440);
        graph.addEdge("�����","������",420);
        graph.addEdge("������","�������",560);
        graph.addEdge("�������","���",166);


        graph.addEdge("������","���������",265);
        graph.addEdge("���������","��������",84);
        graph.addEdge("��������","�����",320);
        graph.addEdge("�����","�����",297);
        graph.addEdge("�����","�����",530);
        graph.addEdge("�����","������",91);
        graph.addEdge("������","������������",281);
        graph.addEdge("������������","������",570);
    }
}

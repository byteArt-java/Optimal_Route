import java.util.Objects;

public class City {
    private final String label;
    private boolean isVisited;
    private int countBranch;
    private int countReferenced;

    public City(String label) {
        this.label = label;
        countBranch = 1;
        countReferenced = 0;
    }

    public int getCountReferenced() {
        return countReferenced;
    }

    public void setCountReferenced(int countReferenced) {
        this.countReferenced = countReferenced;
    }

    public boolean getVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public int getCountBranch() {
        return countBranch;
    }

    public void setCountBranch(int countBranch) {
        this.countBranch = countBranch;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(label, city.label);
    }

    @Override public int hashCode() {
        return Objects.hash(label);
    }

    @Override public String toString() {
        return "Label = " + label;
    }

    public String getLabel() {
        return label;
    }
}

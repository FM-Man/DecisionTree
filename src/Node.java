import java.util.ArrayList;

public class Node {
    ArrayList<Sample> samples = new ArrayList<>();
    ArrayList<Node> children = new ArrayList<>();
    double total = 0;
    double class1total=0;
    double class2total=0;
    double class3total=0;

    public void init(){
        total = samples.size();
        for (Sample sample : samples) {
            if (sample._class == 1) class1total++;
            else if (sample._class == 2) class2total++;
            else class3total++;
        }
    }

    public double impurity(){
        double imp = 0;
        imp -= (class1total / total) * log(class1total / total, 2);
        imp -= (class2total / total) * log(class2total / total, 2);
        imp -= (class3total / total) * log(class3total / total, 2);

        return imp;
    }

    public  double weightedImpurity(){
        double wi =0;
        for (Node child : children) {
            wi += child.total/total * child.weightedImpurity();
        }
        return wi;
    }

    private double log(double a, double b){
        return Math.log(a)/Math.log(b);
    }

}

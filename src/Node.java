import java.util.ArrayList;

public class Node {
    ArrayList<Sample> samples = new ArrayList<>();
    Node[] children = new Node[]{new Node(), new Node(), new Node()};
    Node parent;
    double total = 0;
    double class1total=0;
    double class2total=0;
    double class3total=0;
    int bestSplitAttribute=20;
    double bestSplit1;
    double bestSplit2;
    double bestWeightedEntropy;
    boolean homogenous = false;

    public void init(Node p){
        total = samples.size();
        parent = p;
        for (Sample sample : samples) {
            if (sample._class == 1) class1total++;
            else if (sample._class == 2) class2total++;
            else class3total++;
        }
    }



    public  void kill(){
        samples = new ArrayList<>();
        children = new Node[]{new Node(), new Node(), new Node()};
        total = 0;
        class1total=0;
        class2total=0;
        class3total=0;
        bestSplitAttribute=20;
    }

    public double entropy(){
        double imp = 0;

        if(class1total != 0)
            imp -= (class1total / total) * log(class1total / total);
        if(class2total != 0)
            imp -= (class2total / total) * log(class2total / total);
        if(class3total != 0)
            imp -= (class3total / total) * log(class3total / total);

        return imp;
    }

    public  double weightedEntropyOfTheChildren(){
        double wi =0;
        for (Node child : children) {
            if(child.samples.size() !=0 ){
                wi += child.total/total * child.entropy();
            }
        }
        return wi;
    }

    public void split(){
        if(class1total*class2total + class1total*class3total + class2total*class3total != 0) {
            makeChild();
        }
        else homogenous = true;
    }

    private void makeChild(){
        for(int i=0; i<13; i++){
            double[] randomPoints = new double[10];
            for (int j=0; j<10; j++){
                randomPoints[j] = Driver.min[i] + Driver.max[i]*Math.random();
            }
            for(int j =0; j<10; j++){
                for(int k=j+1; k<10; k++){
                    double a,b;
                    if(randomPoints[j]>randomPoints[k]){
                        a=randomPoints[k];
                        b=randomPoints[j];
                    }else{
                        a=randomPoints[j];
                        b=randomPoints[k];
                    }

                    divide(i,a,b);
                    if(bestSplitAttribute != 20){
                        if (weightedEntropyOfTheChildren()<bestWeightedEntropy){
                            bestSplitAttribute = i;
                            bestSplit1 = a;
                            bestSplit2 = b;
                            bestWeightedEntropy = weightedEntropyOfTheChildren();
                        }
                    }else{
                        bestSplitAttribute = i;
                        bestSplit1 = a;
                        bestSplit2 = b;
                        bestWeightedEntropy = weightedEntropyOfTheChildren();
                    }
                }
            }
        }
        divide(bestSplitAttribute,bestSplit1,bestSplit2);
        if (weightedEntropyOfTheChildren()>entropy()){
            makeChild();
        }else{
            children[0].split();
            children[1].split();
            children[2].split();
        }
    }

    private void divide(int attr, double a, double b){
        children[0].kill();
        children[1].kill();
        children[2].kill();
        for(Sample sample:samples){
            if(sample.get(attr) < a) children[0].samples.add(sample);
            else if(sample.get(attr) < b) children[1].samples.add(sample);
            else children[2].samples.add(sample);
        }
        children[0].init(this);
        children[1].init(this);
        children[3].init(this);
    }

    private double log(double a){
        return Math.log(a)/Math.log(2);
    }

}

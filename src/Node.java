import java.util.ArrayList;

public class Node {
    ArrayList<Sample> samples = new ArrayList<>();
    Node[] children = new Node[2];
    double total = 0;
    int class1total=0;
    int class2total=0;
    int class3total=0;
    int bestSplitAttribute=20;
    double bestSplit;
    double bestWeightedEntropy;
    boolean isHomogenous = false;


    public void initialize(){
        children[0] = new Node();
        children[1] = new Node();
        total = samples.size();
        class1total=0;
        class2total=0;
        class3total=0;
        for (Sample sample : samples) {
            if (sample._class == 1) class1total++;
            else if (sample._class == 2) class2total++;
            else class3total++;
        }
    }
    public  void kill(){
        samples = new ArrayList<>();
        children = new Node[]{new Node(), new Node()};
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
            imp -= (class3total / total) * log(class3total / total);//-p log p

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


    public void split(){/// a=0 b=1 c=5 ab+bc+ca
        if(class1total*class2total + class1total*class3total + class2total*class3total != 0) {
            makeChild();
        }
        else isHomogenous = true;
    }
    private void makeChild(){
        for(int i=0; i<13; i++){
            double quotient = (Driver.max[i]-Driver.min[i])/100;
            for(double j = Driver.min[i]; j<Driver.max[i]; j+=quotient){
                divide(i,j);
                if (weightedEntropyOfTheChildren() < bestWeightedEntropy || bestSplitAttribute == 20){
                    replaceBestSplit(i, j);
                }
            }
        }
        divide(bestSplitAttribute, bestSplit);
        initAll();
        splitAll();
    }
    private void divide(int attr, double split){
        killAllChildren();
        for(Sample sample:samples){
            if(sample.get(attr) < split) children[0].samples.add(sample);
            else children[1].samples.add(sample);
        }
        initAll();
    }


    private void replaceBestSplit(int i, double j) {
        bestSplitAttribute = i;
        bestSplit = j;
        bestWeightedEntropy = weightedEntropyOfTheChildren();
    }
    private double log(double a){
        return Math.log(a)/Math.log(2);
    }
    private void initAll(){
        children[0].initialize();
        children[1].initialize();
    }
    private void killAllChildren(){
        children[0].kill();
        children[1].kill();
    }
    private void splitAll(){
        children[0].split();
        children[1].split();
    }
}

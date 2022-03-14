import java.util.ArrayList;

public class Node {
    ArrayList<Sample> samples = new ArrayList<>();
    Node[] children = new Node[3];
    double total = 0;
    int class1total=0;
    int class2total=0;
    int class3total=0;
    int bestSplitAttribute=20;
    double bestSplit1;
    double bestSplit2;
    double bestWeightedEntropy;
    boolean isHomogenous = false;

    public void init(){
        children[0] = new Node();
        children[1] = new Node();
        children[2] = new Node();
        total = samples.size();
        for (Sample sample : samples) {
            if (sample._class == 1) class1total++;
            else if (sample._class == 2) class2total++;
            else class3total++;
        }
        //System.out.println(class1total+" "+class2total+" "+class3total);
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
        else isHomogenous = true;
    }

    private void makeChild(){
        for(int i=0; i<13; i++){
            double quotient = (Driver.max[i]-Driver.min[i])/20;
            for(double j = Driver.min[i]; j<Driver.max[i]; j+=quotient){
                for(double k=j+quotient; k<Driver.max[i]; k+=quotient){
                    divide(i,j,k);
                    if(bestSplitAttribute == 20){
                        replaceBestSplit(i, j, k);
                    }else{
                        if (weightedEntropyOfTheChildren()<bestWeightedEntropy){
                            replaceBestSplit(i, j, k);
                        }
                    }
                }
            }
        }
        divide(bestSplitAttribute,bestSplit1,bestSplit2);
        initAll();
        splitAll();
    }

    private void replaceBestSplit(int i, double j, double k) {
        bestSplitAttribute = i;
        bestSplit1 = j;
        bestSplit2 = k;
        bestWeightedEntropy = weightedEntropyOfTheChildren();
    }

    private void divide(int attr, double a, double b){
        killAll();
        for(Sample sample:samples){
            if(sample.get(attr) < a) children[0].samples.add(sample);
            else if(sample.get(attr) < b) children[1].samples.add(sample);
            else children[2].samples.add(sample);
        }
        initAll();
    }

    private double log(double a){
        return Math.log(a)/Math.log(2);
    }
    private void initAll(){
        children[0].init();
        children[1].init();
        children[2].init();
    }
    private void killAll(){
        children[0].kill();
        children[1].kill();
        children[2].kill();
    }
    private void splitAll(){
        children[0].split();
        children[1].split();
        children[2].split();
    }

}

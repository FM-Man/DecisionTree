import java.util.ArrayList;

public class Node {
    ArrayList<Sample> samples = new ArrayList<>();
    Node[] children = new Node[2];
    double total = 0;
    int[] classes;
    int bestSplitAttribute=-1;
    double bestSplit;
    double bestWeightedEntropy;
    boolean isHomogenous = false;
    double[] min;
    double[] max;


    public void initialize(int classNumber){
        min = new double[Driver.attributeNumber];
        max = new double[Driver.attributeNumber];
        for(int i=0; i<Driver.attributeNumber;i++){
            min[i] = Double.MAX_VALUE;
            max[i] = Double.MIN_VALUE;
        }
        children[0] = new Node();
        children[1] = new Node();
        total = samples.size();
        bestSplitAttribute = -1;

        classes = new int[classNumber];
        for (Sample sample : samples) {
            classes[sample.category-1] ++;

            for (int i = 0; i < Driver.attributeNumber; i++) {
                if (sample.get(i) < min[i]) min[i] = sample.get(i);
                if (sample.get(i) > max[i]) max[i] = sample.get(i);
            }
        }
        //System.out.println("i");
    }
    public  void kill(int classNumber){
        samples = new ArrayList<>();
        children = new Node[]{new Node(), new Node()};
        total = 0;
        classes = new int[classNumber];
        bestSplitAttribute=-1;
        min = new double[Driver.attributeNumber];
        max = new double[Driver.attributeNumber];
        //System.out.println("k");
    }


    public double entropy(){
        double imp = 0;

        for (int aClass : classes) {
            if (aClass != 0) {
                imp -= (aClass / total) * log(aClass / total);
            }
        }

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
        int homogenousFactor = 0;
        for (int i=0; i<classes.length;i++) {
            if(i==classes.length-1){
                homogenousFactor += classes[i]*classes[0];
            } else {
                homogenousFactor += classes[i]*classes[i+1];
            }
        }

        if(homogenousFactor != 0) makeChild();
        else isHomogenous = true;
    }
    private void makeChild(){
        for(int i = 0; i< Driver.attributeNumber; i++){
            double quotient = (max[i]-min[i])/100;

            if(min[i] != -1){
                for (double j = min[i]; j < max[i]; j += quotient) {
                    divide(i, j);//int k =0;System.out.format("%d\n",k++);
                    if (weightedEntropyOfTheChildren() < bestWeightedEntropy || bestSplitAttribute == -1) {
                        replaceBestSplit(i, j);
                    }
                }
            }
        }
        divide(bestSplitAttribute, bestSplit);
        //System.out.println("s");
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
        //System.out.println("d");
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
        children[0].initialize(Driver.totalClasses);
        children[1].initialize(Driver.totalClasses);
    }
    private void killAllChildren(){
        children[0].kill(Driver.totalClasses);
        children[1].kill(Driver.totalClasses);
    }
    private void splitAll(){
        children[0].split();
        children[1].split();
    }
}

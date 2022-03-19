import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Driver {
    public static ArrayList<Sample> samples = new ArrayList<>();
    public static ArrayList<Sample> training = new ArrayList<>();
    public static ArrayList<Sample> test = new ArrayList<>();
    public static int attributeNumber;
    public static int totalClasses = 10;


    public static Node root;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("winequality-red.csv"));

        while(scanner.hasNext()){
            String current = scanner.nextLine();
            String[] split = current.split(";",0);
            double[] data = new double[split.length];
            for (int i=0; i<split.length; i++){
                data[i] = Double.parseDouble(split[i]);
            }
            double t = data[0];
            data[0] = data[data.length-1];
            data[data.length-1] = t;

            attributeNumber= data.length-1;
            Sample sample = new Sample(data);
            samples.add(sample);
        }
        Collections.shuffle(samples);
        double totalCorrectTest=0;
        double totalWrong=0;
        for(int i=0;i<1/*samples.size()/10*/;i++){
            train(i);
            double correctRatio = test();
            totalCorrectTest+=correctRatio*test.size();
            totalWrong+=(1-correctRatio)*test.size();
            System.out.println();
            print();
            System.out.println("\n\n===========================================================================================\n\n");
        }
        System.out.format("\ntotal tested: %d\ntotal correct: %d\ntotal wrong: %d\naverage correctness: %.3f percent\n\n",Math.round(totalCorrectTest+totalWrong),Math.round(totalCorrectTest),Math.round(totalWrong),totalCorrectTest/(totalCorrectTest+totalWrong)*100);


    }

    public static void train(int i){
        training = new ArrayList<>();
        test = new ArrayList<>();
        for (int j=0;j<samples.size();j++) {
//            if(j<i*10 || j>=(i+1)*10){
//                training.add(samples.get(j));
//            } else {
//                test.add(samples.get(j));
//            }
            if(Math.random() < .8) training.add(samples.get(j));
            else test.add(samples.get(j));
        }

        root = new Node();
        root.samples.addAll(training);
        root.initialize(totalClasses);
        root.split();
    }

    public static double test(){
        double correct = 0;
        double wrong = 0;
        Node node;
        for (Sample sample:test) {
            node = root;
            while (!node.isHomogenous){
                if(sample.get(node.bestSplitAttribute) < node.bestSplit) {
                    node = node.children[0];
                } else {
                    node = node.children[1];
                }
            }
            if(node.samples.get(0).category == sample.category) {
                correct++;
            } else {
                wrong++;
            }
        }
        System.out.println("correct = "+correct+" wrong = "+wrong);
        return correct/(correct+wrong);
    }

    public static void print(){
        if(root == null) return;

        String nodeDetail = "[ ";
        for(int i=0; i<root.classes.length;i++){
            nodeDetail += (i+1)+ ":" + root.classes[i]+" ";
        }
        nodeDetail +="]";
        System.out.print(nodeDetail);

        if(root.bestSplitAttribute!=-1) {
            System.out.format("\nattr: %d split: %.3f\n", root.bestSplitAttribute, root.bestSplit);
        }
        else System.out.println();
        print("--------", root.children[0]);
        print("--------", root.children[1]);
    }
    public static void print(String s, Node node){
        if(node== null) return;

        String nodeDetail = "[ ";
        for(int i=0; i<node.classes.length;i++){
            nodeDetail += (i+1)+ ":" + node.classes[i]+" ";
        }
        nodeDetail +="]";
        System.out.print(s+nodeDetail);

        if(node.bestSplitAttribute!=-1) {
            System.out.println();
            for (int i=0; i<s.length(); i++)
                System.out.print(" ");
            System.out.format("attr: %d split: %.3f\n", node.bestSplitAttribute, node.bestSplit);
        }
        else System.out.println();
        if(!node.isHomogenous) {
            print(s + "--------", node.children[0]);
            print(s + "--------", node.children[1]);
        }
    }
}

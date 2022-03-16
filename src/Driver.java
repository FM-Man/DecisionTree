import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static ArrayList<Sample> samples = new ArrayList<>();
    public static ArrayList<Sample> training = new ArrayList<>();
    public static ArrayList<Sample> test = new ArrayList<>();

    public static double[] min = new double[13];
    public static double[] max = new double[13];
    public static Node root;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("wine.data"));

        for (int i=0; i<13; i++){
            min[i] = Double.MAX_VALUE;
            max[i] = Double.MIN_VALUE;
        }

        while(scanner.hasNext()){
            String current = scanner.nextLine();
            String[] split = current.split(",",0);
            double[] data = new double[14];
            for (int i=0; i<14; i++){
                data[i] = Double.parseDouble(split[i]);
            }

            Sample sample = new Sample(data);
            samples.add(sample);

            for (int i=0; i<13; i++){
                if(sample.get(i) < min[i]) min[i] = sample.get(i);
                if(sample.get(i) > max[i]) max[i] = sample.get(i);
            }
        }

        double totalCorrectTest=0;
        double totalWrong=0;
        for(int i=0;i<100;i++){
            train();
            double correctRatio = test();
            totalCorrectTest+=correctRatio*test.size();
            totalWrong+=(1-correctRatio)*test.size();
        }
        System.out.format("\ntotat tested: %d\ntotal correct: %d\ntotal wrong: %d\naverage correctness: %.3f percent\n\n",Math.round(totalCorrectTest+totalWrong),Math.round(totalCorrectTest),Math.round(totalWrong),totalCorrectTest/(totalCorrectTest+totalWrong)*100);

        print();
    }

    public static void train(){
        training = new ArrayList<>();
        test = new ArrayList<>();
        for (Sample sample:samples) {
            if(Math.random() < .8) {
                training.add(sample);
            } else {
                test.add(sample);
            }
        }

        root = new Node();
        root.samples.addAll(training);
        root.initialize();
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
            if(node.samples.get(0)._class == sample._class) {
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
        System.out.print("[1:"+root.class1total+" 2:"+root.class2total+" 3:"+root.class3total+"]");
        if(root.bestSplitAttribute!=20) {
            System.out.format("\nattr: %d split: %.3f\n", root.bestSplitAttribute, root.bestSplit);
        }
        else System.out.println();
        print("--------", root.children[0]);
        print("--------", root.children[1]);
    }
    public static void print(String s, Node node){
        if(node== null) return;
        System.out.print(s+"[1:"+node.class1total+" 2:"+node.class2total+" 3:"+node.class3total+"]");
        if(node.bestSplitAttribute!=20) {
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

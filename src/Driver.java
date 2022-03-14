import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static ArrayList<Sample> samples = new ArrayList<>();
    public static ArrayList<Sample> class1 = new ArrayList<>();
    public static ArrayList<Sample> class2 = new ArrayList<>();
    public static ArrayList<Sample> class3 = new ArrayList<>();
    public static ArrayList<Sample> training = new ArrayList<>();
    public static ArrayList<Sample> test = new ArrayList<>();

    public static int class1total=0, class2total=0, class3total=0, class1train=0, class2train=0, class3train=0;
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

            if (sample._class == 1) {
                class1.add(sample);
                class1total++;
            }
            else if (sample._class == 2) {
                class2.add(sample);
                class2total++;
            }
            else {
                class3.add(sample);
                class3total++;
            }

            if(Math.random() < .8) {
                training.add(sample);
                if(sample._class == 1) class1train++;
                if(sample._class == 2) class2train++;
                else class3train++;
            }
            else test.add(sample);

            for (int i=0; i<13; i++){
                if(sample.get(i) < min[i]) min[i] = sample.get(i);
                if(sample.get(i) > max[i]) max[i] = sample.get(i);
            }
        }
        train();
        test();
    }

    public static void train(){
        root = new Node();
        root.init(null);
        root.samples.addAll(training);
        root.split();
    }
    public static void test(){
        int correct = 0;
        int wrong = 0;
        Node node = root;
        for (Sample sample:test) {
            node = root;
            while (!node.isHomogenous){
                if(sample.get(node.bestSplitAttribute) < node.bestSplit1)
                    node = node.children[0];
                else if(sample.get(node.bestSplitAttribute) < node.bestSplit2)
                    node = node.children[1];
                else
                    node = node.children[2];
            }
            if(node.samples.get(0)._class == sample._class)
                correct++;
            else wrong++;
        }
        System.out.println("correct = "+correct+" wrong = "+wrong);
    }
}

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
        for(int i=0;i<10000;i++){
            train();
            double correctRatio = test();
            totalCorrectTest+=correctRatio*test.size();
            totalWrong+=(1-correctRatio)*test.size();
        }
        System.out.println("average correctness: "+totalCorrectTest/(totalCorrectTest+totalWrong)*100+"%");
    }

    public static void train(){
        training = new ArrayList<>();
        test = new ArrayList<>();
        for (Sample sample:samples) {
            if(Math.random() < .8) training.add(sample);
            else test.add(sample);
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
                if(sample.get(node.bestSplitAttribute) < node.bestSplit)
                    node = node.children[0];
                else
                    node = node.children[1];
            }
            if(node.samples.get(0)._class == sample._class) {
                correct++;
            }
            else wrong++;
        }
        System.out.println("correct = "+correct+" wrong = "+wrong);
        return correct/(correct+wrong);
    }
}

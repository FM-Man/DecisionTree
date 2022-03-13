import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("wine.data"));

        ArrayList<Sample> samples = new ArrayList<>();
        ArrayList<Sample> class1 = new ArrayList<>();
        ArrayList<Sample> class2 = new ArrayList<>();
        ArrayList<Sample> class3 = new ArrayList<>();
        ArrayList<Sample> training = new ArrayList<>();
        ArrayList<Sample> test = new ArrayList<>();

        int class1total=0, class2total=0, class3total=0, class1train=0, class2train=0, class3train=0;
        double[] min = new double[13];
        double[] max = new double[13];
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


    }

    //public static double entropy()
}

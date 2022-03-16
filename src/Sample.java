public class Sample {
    public int category;
    private double[] attributes;

    public Sample(double[] data){
        attributes = new double[data.length-1];
        category = (int) data[0];
        System.arraycopy(data, 1, attributes, 0, attributes.length);
    }

    public double get(int i){
        return attributes[i];
    }
}

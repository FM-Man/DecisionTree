public class Sample {
    public int _class;
    public double _1_alcohol;
    public double _2_malicAcid;
    public double _3_ash;
    public double _4_alcalinityOfAsh;
    public double _5_magnesium;
    public double _6_totalPhenols;
    public double _7_flavanoids;
    public double _8_nonflavanoidPhenols;
    public double _9_proanthocyanins;
    public double _10_colorIntensity;
    public double _11_hue;
    public double _12_OD280_OD315_OfDilutedWines;
    public double _13_proline;

    public Sample(double[] data){
        _class = (int) data[0];
        _1_alcohol = data[1];
        _2_malicAcid = data[2];
        _3_ash = data[3];
        _4_alcalinityOfAsh = data[4];
        _5_magnesium = data[5];
        _6_totalPhenols = data[6];
        _7_flavanoids = data[7];
        _8_nonflavanoidPhenols = data[8];
        _9_proanthocyanins = data[9];
        _10_colorIntensity = data[10];
        _11_hue = data[11];
        _12_OD280_OD315_OfDilutedWines = data[12];
        _13_proline = data[13];
    }
}

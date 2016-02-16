import Jama.Matrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Calculations {
    public static void main(String[] args) {
        LinkedList<double[]> LL = new LinkedList<>();
        LL.add(new double[] {1,2,3});
        LL.add(new double[] {2,6,10});
        LL.add(new double[] {0.2,2,13});

        LinkedList<double[]> LL2 = new LinkedList<>();
        LL2.add(new double[] {5,9,12});
        LL2.add(new double[] {17,4,0.3});
        LL2.add(new double[] {14.8,20.2,11.2});

        double[][] DL = new double[LL.size()][];
        for (int i = 0; i < LL.size(); i++) {
            DL[i] = ArrayUtils.addAll(LL.get(i), LL2.get(i));
        }

        for (double[] values : DL) {
            System.out.println(Arrays.toString(values));
        }

        PearsonsCorrelation pCorr = new PearsonsCorrelation();
        RealMatrix corrMatrix = pCorr.computeCorrelationMatrix(DL);
        double[][] corrMatrixArray = corrMatrix.getData();
        List<Double> pCoeffs = new ArrayList<>();
        for (int i = 0; i < corrMatrixArray.length; i++) {
            for (int j = 0; j < corrMatrixArray[0].length; j++) {
                if (j > i) {
                    if (Double.isNaN(corrMatrixArray[i][j])) {
                        corrMatrixArray[i][j] = 0;
                    }
                    pCoeffs.add(corrMatrixArray[i][j]);
                }
            }
        }
        double[] pCoeffsArray = ArrayUtils.toPrimitive(pCoeffs.toArray(new Double[pCoeffs.size()]));
        System.out.println(Arrays.toString(pCoeffsArray));
//        List<double[]> linAccSamples = new LinkedList<>(Arrays.asList(new double[15][3]));
//        for (int i = 0; i < linAccSamples.size(); i++) {
//            System.out.println(Arrays.toString(linAccSamples.get(i)));
//        }
    }
}

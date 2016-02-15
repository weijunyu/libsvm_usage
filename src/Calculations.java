import Jama.Matrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.Arrays;
import java.util.LinkedList;


public class Calculations {
    public static void main(String[] args) {
        LinkedList<double[]> LL = new LinkedList<>();
        System.out.println("size of ll is: " + LL.size());

        LL.addLast(new double[] {1,2,5});
        LL.addLast(new double[] {0,0,0});
        LL.addFirst(new double[] {0,0,0});
        System.out.println("Size of LL is: " + LL.size());
        System.out.println("LL[0] = " + Arrays.toString(LL.get(0)));

        double[][] DL = new double[LL.size()][];
        for (int i = 0; i < LL.size(); i++) {
            DL[i] = LL.get(i);
        }

        PearsonsCorrelation pCorr = new PearsonsCorrelation();
        RealMatrix corrMatrix = pCorr.computeCorrelationMatrix(DL);
        double[][] corrMatrixArray = corrMatrix.getData();
        for (int i = 0; i < corrMatrixArray.length; i++) {
            for (int j = 0; j < corrMatrixArray[0].length; j++) {
                if (j > i) {
                    System.out.println(corrMatrixArray[i][j]);
                }
            }
        }
    }
}

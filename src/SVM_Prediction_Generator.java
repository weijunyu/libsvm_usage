import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;

import java.io.*;
import java.util.StringTokenizer;

public class SVM_Prediction_Generator {
    private static double atof(String s)
    {
        return Double.valueOf(s);
    }

    private static int atoi(String s)
    {
        return Integer.parseInt(s);
    }

    private static double predict(String input, svm_model model, int predict_probability) throws IOException
    {
        int correct = 0;
        int total = 0;
        double error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;

        int svm_type=svm.svm_get_svm_type(model);
        int nr_class=svm.svm_get_nr_class(model); // get the number of class labels from the model
        double[] prob_estimates=null;

        if(predict_probability == 1)
        {
            if(svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR)
            {
                // not relevant since we're using C_SVC
                svm_predict.info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="+svm.svm_get_svr_probability(model)+"\n");
            }
            else
            {
                // array holding the different labels
                int[] labels=new int[nr_class];
                // populates the labels array
                svm.svm_get_labels(model,labels);
                String labels_string = "";
                for (int i=0; i<nr_class; i++) {
                    labels_string += labels[i] + " ";
                }
                System.out.println("The labels from the model are: " + labels_string);
                prob_estimates = new double[nr_class];
//                output.writeBytes("labels");
//                for(int j=0;j<nr_class;j++)
//                    output.writeBytes(" "+labels[j]);
//                output.writeBytes("\n");
            }
        }
//        while(true)
//        {
            // loop over each line (feature vector)
//            String line = input.readLine();
        String line = input;
//        if(line == null) break;

        StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

        // Reads the target class of the log line
        double target = atof(st.nextToken());
        // get number of features, eg 30
        int m = st.countTokens()/2;
        svm_node[] x = new svm_node[m];
        for(int j=0;j<m;j++)
        {
            // x is an array of svm_nodes. this populates x with the feature number and value
            x[j] = new svm_node();
            x[j].index = atoi(st.nextToken());
            x[j].value = atof(st.nextToken());
        }

        double v;
        if (predict_probability==1 && (svm_type==svm_parameter.C_SVC || svm_type==svm_parameter.NU_SVC))
        {
            // we're predicting probabilities with C_SVC
            v = svm.svm_predict_probability(model,x,prob_estimates);
            // v is the predicted class label for that data sample
            // prob_estimates holds probabilities that the vector is in each class
//            output.writeBytes("predicted class is " + v + " and probabilities are: ");
            // Write probability estimate that feature vector belongs to each class
//            for(int j=0;j<nr_class;j++)
//                output.writeBytes(prob_estimates[j]+" ");
//            output.writeBytes("\n");
        }
        else
        {
            // Writes the class label of the feature vector, without probabilities
            v = svm.svm_predict(model,x);
//            output.writeBytes(v+"\n");
        }

        if(v == target)
            ++correct;
        error += (v-target)*(v-target);
        sumv += v;
        sumy += target;
        sumvv += v*v;
        sumyy += target*target;
        sumvy += v*target;
        ++total;
//        }

        // This part evaluates the predictions against the actual class labels in the test file
        if(svm_type == svm_parameter.EPSILON_SVR ||
                svm_type == svm_parameter.NU_SVR)
            // irrelevant
        {
            svm_predict.info("Mean squared error = "+error/total+" (regression)\n");
            svm_predict.info("Squared correlation coefficient = "+
                    ((total*sumvy-sumv*sumy)*(total*sumvy-sumv*sumy))/
                            ((total*sumvv-sumv*sumv)*(total*sumyy-sumy*sumy))+
                    " (regression)\n");
        }
        else
            svm_predict.info("Accuracy = "+(double)correct/total*100+
                    "% ("+correct+"/"+total+") (classification)\n");

        // End of original predict() function
        return v;
    }

    public static void main(String argv[]) throws IOException {
        try {
            int predict_probability = 0;

            // input is the test file
            // BufferedReader input = new BufferedReader(new FileReader("tap_occurrence_scaled_dec.test"));

            // First predict whether a tap has occurred
            // input is a string containing all features
            // positive tap
            // String input = "1 1:-0.74217 2:-0.712455 3:-0.649395 4:-0.826558 5:-0.79186 6:-0.763935 7:0.101606 8:-0.211935 9:0.138287 10:-0.577632 11:-0.639349 12:-0.663309 13:-0.65029 14:-0.843734 15:-0.751924 16:-0.525621 17:-0.784266 18:-0.625866 19:-0.407277 20:-0.831711 21:-0.666421 22:-0.249417 23:-0.228567 24:-0.326802 25:-0.412511 26:-0.707243 27:-0.516447 28:-0.60481 29:-0.539865 30:-0.571744 ";
            // negative tap
            String input = "-1 1:-0.904258 2:-0.971711 3:-0.969527 4:-0.99703 5:-0.989086 6:-0.987469 7:0.0717096 8:0.151666 9:-0.044473 10:-0.882001 11:-0.935265 12:-0.935865 13:-0.918873 14:-0.986914 15:-0.965592 16:-0.983662 17:-0.995018 18:-0.997947 19:-0.98433 20:-0.995726 21:-0.994205 22:-0.0626794 23:0.0575041 24:0.0202466 25:-0.88927 26:-0.912602 27:-0.859485 28:-0.989535 29:-0.997235 30:-0.99411 ";
            // model is loaded from file
            svm_model tap_occurrence_model = svm.svm_load_model("tap_occurrence.scaled.model");
            svm_model holding_hand_model = svm.svm_load_model("hand_5p.scaled.model");
            svm_model lhand_location_model = svm.svm_load_model("location_lhand.scaled.model");
            svm_model rhand_location_model = svm.svm_load_model("location_rhand.scaled.model");
            // first see whether we can detect a tap
            // +1 for tap, -1 for no tap
            double tap_occurrence = predict(input, tap_occurrence_model, predict_probability);
            System.out.println("Predicted label is " + tap_occurrence);

//            // output file contains classifications
//            DataOutputStream output = new DataOutputStream(
//                    new BufferedOutputStream(new FileOutputStream("tap_occurrence_scaled_dec.output")));
//            // model is loaded from external file
//            svm_model model = svm.svm_load_model("tap_occurrence_scaled_dec.train.model");
//            // flag to use probabilities or not
//            predict(input,output,model,predict_probability);
        } catch (FileNotFoundException|ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}

/*
Parses the trained weights file
*/

import java.io.*;
import java.util.*;

public class ReadWeights {
    int dim_weights;
    int[][] weights;

    public ReadWeights(String filename){
        try {
            File infile = new File(filename);
            Scanner sca = new Scanner(infile);

            this.dim_weights = sca.nextInt(); //read dim weights
            this.weights = new int[dim_weights][dim_weights]; //init weights matrix

            for(int i = 0; i < this.dim_weights; i++){ 
                for(int j = 0; j < this.dim_weights; j++){
                    this.weights[i][j] = sca.nextInt(); //read weights
                }
            }
            sca.close();
        }
        catch (FileNotFoundException e){
            e.getMessage();
        }
    }
}

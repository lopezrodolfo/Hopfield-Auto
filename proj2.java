/*
1) Program Description: Driver
2) Authors: Rodolfo Lopez, Andrew Kirrane, Julia Paley
3) Last Modified: 11/10/20201 
*/

import java.io.*;
import java.util.*;

public class proj2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) { //allows program to be run multiple times
            System.out.println("Welcome to my second neural network -- A Hopfield Net!\n");
            program(sc);
            System.out.println();
            System.out.println("Do you want to run the program again (y for yes and n for no)?:");
            if (!sc.nextLine().equals("y")) {
                break;
            }
        }
        sc.close();
    }

    public static void program(Scanner sc) {
        System.out.println("Enter 1 to use a training data file, enter 2 to use a trained weights settings data file:\n");
        int training_type = Integer.parseInt(sc.nextLine());
        if(training_type == 1){ //train NN
            System.out.println("Enter the training data file name:\n");
            String filename = sc.nextLine();
            Readinput infile = new Readinput(filename); //read sample images
            int[][] weights = train(infile); //train with sample images
            System.out.println("Training is complete!\n");
            System.out.println("Enter a filename to save the trained weight settings:\n");
            String outfile = sc.nextLine();
            writeweights(outfile, infile.dim_images, weights); //write trained weights
            System.out.println("Enter 1 to test/deploy using a testing/deploying data file, enter 2 to quit:\n");
            int test_choice = Integer.parseInt(sc.nextLine());
            if(test_choice == 1){ //test NN
                    System.out.println("Enter the testing/deploying data file name:\n");
                    String inf = sc.nextLine();
                    System.out.println("Enter a file name to save the testing/deploying results:\n");
                    String outfilename = sc.nextLine();
                    Readinput infilename = new Readinput(inf); //read sample images
                    int[][] output = test(weights, infilename.images, infilename.dim_images, infilename.num_images); //test with sample images
                    Readinput in = new Readinput(inf); 
                    int[][] trainingimages = in.images;
                    writeresults(output, trainingimages, outfilename); //write image association results
            }
            else if(test_choice == 2){ //quit after training (with no testing)
                System.exit(0);
            }
        }
        else if(training_type == 2){ //did not train 
            System.out.println("Enter the trained weight settings input data file name:\n");
            String weightsfilename = sc.nextLine(); 
            ReadWeights file = new ReadWeights(weightsfilename); //read trained weights
            int[][] weights = file.weights;
            System.out.println("Enter 1 to test/deploy using a testing/deploying data file, enter 2 to quit:\n");
            int test_choice = Integer.parseInt(sc.nextLine());
            if(test_choice == 1){ //test NN
                System.out.println("Enter the testing/deploying data file name:\n");
                String filename = sc.nextLine();
                System.out.println("Enter a file name to save the testing/deploying results:\n");
                String outfilename = sc.nextLine();
                Readinput infilename = new Readinput(filename);
                int[][] output = test(weights, infilename.images, infilename.dim_images, infilename.num_images);
                Readinput in = new Readinput(filename);
                int[][] images = in.images;
                writeresults(output, images, outfilename);
            }
            else if(test_choice == 2){ //quit program
                System.exit(0);
            }
        }

    }

    public static int[][] train(Readinput infile) {
        int[][] weights = new int[infile.dim_images][infile.dim_images]; //init weights matrix
        for(int[] row: weights){
            Arrays.fill(row, 0); //initialize weights to zero
        }
        for (int i = 0; i < infile.num_images; i++) { //i in num images
            for (int j = 0; j < infile.dim_images; j++) { //j in dim images
                for (int k = 0; k < infile.dim_images; k++) { //k in dim images
                    if(k == j){ 
                        weights[k][j] = 0; //weight matrix diagonals set to zero
                    }
                    else{
                        weights[k][j] += infile.images[i][k] * infile.images[i][j]; // non diagonals row * col of weight matrix
                    }
                }
            }
        }
        return weights; //return weights matrix
    }

    public static int[][] test(int[][] weights, int[][] images, int dim_images, int num_images){
        int[][] output = new int[num_images][dim_images]; //init output
        int[][] tmpimages = new int[num_images][dim_images]; //init temporary copies of images
        tmpimages = images;
        int index = 0;
        for(int i = 0; i < num_images; i++){ //i in sample image
            boolean oneepoch = false;
            boolean cont = true;
            while(cont){ //not converged

                Integer[] indexseen = new Integer[dim_images]; //init random array
                Arrays.fill(indexseen,-1);
                int[] tmp = new int[dim_images];
                
                if(!oneepoch){ //first epoch
                    tmp = tmpimages[i]; //set temp array equal to sample image
                }
                else{
                    tmp = output[index]; //else set to previous epoch
                }
                boolean updated = false; //condition for activation updating

                for (int j = 0; j < dim_images; j++) { // j in dim images
                    List<Integer> indexlst = Arrays.asList(indexseen); //create array list holding previously seen random numbers
					double rand = Math.random() * dim_images; //gernate random number
					int random = (int)Math.floor(rand); 
					while (indexlst.contains(random)){ //check if random has been seen before
						rand = Math.random() * dim_images;
						random = (int)Math.floor(rand); //find another random number until unseen number 
					}
                    indexseen[j] = random;

                    int begin = tmp[random]; //initial node set
                    int y_in = tmp[random]; // begin y in calc

                    for (int k = 0; k < dim_images; k++) { 
						y_in += tmp[k] * weights[k][random]; //calc y in
					}

                    int y = activation(y_in); //activation for y in 

                    if(y != 0){ //if y changed
                        tmp[random] = y; //set node equal to new y
                    }
                    else if(y == 0 && oneepoch){ //if past first epoch and y has changed
                        tmp[random] = output[index][random]; //set node equal to old y
                    }
					if(y != begin){ //if node has been updated
                        updated = true;
                    }
					if(updated){ //if updated then go to next epoch
                        cont = true;
                    }
					else{ //if not update, then converged!
                        cont = false;
                    }
                }
                oneepoch = true;
                output[index] = tmp; //set output of testing sample
            }
            index++;
        }
        return output; 
    }

    public static void writeweights(String outfile, int dim_weights, int weights[][]){
        try{
            FileWriter writer = new FileWriter(outfile); //init trained weights file
            writer.write(dim_weights + "\n"); //write dim weights
            for(int i=0; i<dim_weights; i++){
                writer.write("\n");
                for(int j=0; j<dim_weights; j++){
                    writer.write(weights[i][j] + " "); //write trained weights 
                }
            }
            writer.flush();
            writer.close();
        }
        catch(FileNotFoundException e) {
            e.getMessage();
        }
        catch(IOException e) {
            e.getMessage();
        }
    }

    public static int activation(int y_in){
        //bipolar activation function
        if(y_in > 0){
            return 1; 
        }
        else if(y_in < 0){
            return -1;
        }
        else{
            return 0;
        }
    }

    public static void writeresults(int[][] output, int[][] images, String outfile){
        boolean correct = false;
        int count = 0;
        
        try{
            FileWriter writer = new FileWriter(outfile); //init results file

            for(int k=0; k<output.length; k++){ // k in output
                int row = (int) Math.sqrt(images[0].length); //obtain row
                writer.write("Input testing image:\n"); //write initial input image
                for(int i=0; i<row; i++){ // i in row
                    for(int j=0; j<row; j++){
                        if(images[k][j+(row*i)] == 1){
                            writer.write('O'); //write initial input
                        }
                        else if(images[k][j+(row*i)] == -1){
                            writer.write(" "); //write associated image after training
                        }
                        else{
                            writer.write("@"); //cannot associate 
                        }
                    }
                    writer.write("\n");
                }
                writer.write("\nThe associated stored image:\n"); //after testing write associated image
                for(int i=0; i<row; i++){
                    for(int j=0; j<row; j++){
                        if(output[k][j+(row*i)] == 1){
                            writer.write('O'); 
                        }
                        else if (output[k][j+(row*i)] ==-1){
                            writer.write(" "); 
                        }
                        else{
                            writer.write("@"); 
                        }
                    }
                    writer.write("\n");
                }
                for(int i=0; i < output.length; i++){
                    if(output[k][i] == 0){ //has not associated with an a image
                        correct = false;
                    }
                    else{
                        correct = true;
                    }
                }
                if(correct){ 
                    writer.write("\nPattern correctly associated\n");
                    count++; //number of samples correctly associated
                }
                else{
                    writer.write("\nPattern did not correctly associate\n");
                }
            }
            writer.write("The percentage of images associated is: " + (double)((count/output.length) * 100) + "%"); //calculate percent correct
            writer.flush();
            writer.close();
        }
        catch(FileNotFoundException e) {
            e.getMessage();
        }
        catch(IOException e) {
            e.getMessage();
        }
    }
}
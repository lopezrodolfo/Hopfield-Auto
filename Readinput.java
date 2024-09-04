/*
Parses the sample training file
*/

import java.io.*;

public class Readinput{
    int[][] images;
    int num_images;
    int dim_images;

    public Readinput(String inputFileName) {
        try{
            BufferedReader infile = new BufferedReader(new FileReader(inputFileName));

            this.dim_images = Integer.parseInt(infile.readLine()); //read dim images
            this.num_images = Integer.parseInt(infile.readLine()); //read num images

            infile.readLine();

            this.images = new int[this.num_images][this.dim_images]; //init image array

            for (int i = 0; i < this.num_images; i++){
                for (int j = 0; j < this.dim_images; j++){
                    int r = infile.read();
                    char ch = (char) r;
                    if((Character.compare(ch,'O')) == 0){
                        this.images[i][j] = 1; //read bit
                    }
                    else if((Character.compare(ch,' ')) == 0){
                        this.images[i][j] = -1; //read whitespace
                    }
                    else if((Character.compare(ch,'\n')) == 0){
                        j--; //next row
                    }
                    else{
                        this.images[i][j] = 0; //missing data
                    }
                } 
                infile.readLine();
            }
            infile.close();
        }    
        catch (FileNotFoundException e){
            e.getMessage();
        }
        catch (IOException e){
            e.getMessage();
        }
    }
}
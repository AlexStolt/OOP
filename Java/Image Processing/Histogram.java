package ce326.hw2;

import java.io.FileWriter;
import java.io.IOException;

public class Histogram {
    double [] luminosityArray;
    private int MAX_LUMINOSITY = 236;
    private int totalPixels;


    public Histogram(YUVImage img){
        short brightness;
        //Empty Array
        if(img.pixelMatrix == null)
            return;

        totalPixels = img.height * img.width;
        //Create Luminosity Matrix to Store Probability
        luminosityArray = new double[MAX_LUMINOSITY];

        for(int i = 0; i < img.height; i++){
            for (int j = 0; j < img.width; j++){
                //Set Value on Luminosity Array on Correct Position
                luminosityArray[img.pixelMatrix[i][j].getY()]++;
            }
        }
    }

    public void equalize(){
        //Calculate Cumulative Distribution
        for(int i = 0; i < MAX_LUMINOSITY; i++){
            luminosityArray[i] /= totalPixels;
            if(i != 0){
                luminosityArray[i] += luminosityArray[i - 1];
            }
        }

        //Multiply by MAX_LUMINOSITY and Cut the Decimal Part
        for(int i = 0; i < MAX_LUMINOSITY; i++){
            luminosityArray[i] *= (MAX_LUMINOSITY - 1);
            luminosityArray[i] = Math.floor(luminosityArray[i]);
        }
    }

    public short getEqualizedLuminosity(int luminosity){
        return (short) luminosityArray[luminosity];
    }

    public String toString(){
        StringBuilder histogramString = new StringBuilder();



        for(int i = 0; i < MAX_LUMINOSITY; i++){

            histogramString.append(String.format("\n%3d.(%4d)\t", i, (int) luminosityArray[i]));

            while (true){
                if(luminosityArray[i] >= 1000){
                    histogramString.append("#");
                    luminosityArray[i] -= 1000;
                }
                else if (luminosityArray[i] >= 100){
                    histogramString.append("$");
                    luminosityArray[i] -= 100;
                }
                else if (luminosityArray[i] >= 10){
                    histogramString.append("@");
                    luminosityArray[i] -= 10;
                }
                else if(luminosityArray[i] > 0){
                    histogramString.append("*");
                    luminosityArray[i]--;
                }
                else {
                    break;
                }
            }
        }

        histogramString.append("\n");

        return histogramString.toString();
    }

    void toFile(java.io.File file){
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(toString());
            writer.close();
        } catch (IOException exception){
            return;
        }
    }
}

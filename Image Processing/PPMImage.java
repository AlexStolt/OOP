package ce326.hw2;

import java.io.*;
import java.util.Scanner;

public class PPMImage extends RGBImage {
    public PPMImage(java.io.File file) throws java.io.FileNotFoundException, UnsupportedFileFormatException {
        int totalPixels;
        //File Must Exist
        if(!file.exists() || !file.canRead()){
            throw new java.io.FileNotFoundException();
        }

        //Try to Scan Image File
        try(Scanner scanner = new Scanner(file)) {
            //Image Format
            if(!scanner.next().equals("P3")){
                throw new UnsupportedFileFormatException();
            }

            //Image Dimensions
            width = scanner.nextInt();
            height = scanner.nextInt();

            //Image Color Depth
            colordepth = scanner.nextInt();

            //Create Image Matrix
            pixelMatrix = new RGBPixel[height][width];

            //Image Content
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    pixelMatrix[i][j] = new RGBPixel(scanner.nextShort(), scanner.nextShort(), scanner.nextShort());
                }
            }

            //More Content than the Specified Width and Height
            if(scanner.hasNext()){
                throw new UnsupportedFileFormatException();
            }
        } catch (FileNotFoundException exception){
            throw new java.io.FileNotFoundException();
        }
    }
    public PPMImage(RGBImage img){
        super(img.getWidth(), img.getHeight(), img.getColorDepth());

        //Create Image Matrix
        pixelMatrix = new RGBPixel[height][width];

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                pixelMatrix[i][j] = new RGBPixel(img.pixelMatrix[i][j]);
            }
        }
    }
    public PPMImage(YUVImage img){
        super(img);
    }


    public String toString(){
        //Append Image "HEADER"
        StringBuilder content = new StringBuilder();
        content.append(String.format("P3\n%d %d %d\n", width, height, colordepth));

        //Append Image Content
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                content.append(pixelMatrix[i][j].toString());
            }
        }

        return content.toString();
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

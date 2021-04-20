package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PPMImageStacker {
    //Store Images on a List
    private List<File> imageList = new ArrayList<File>();
    PPMImage stackedImagePPM;
    private int totalImages = 0;
    //Constructors
    public PPMImageStacker(java.io.File dir) {
        File[] images;
        if (!dir.exists()) {
            System.out.println(String.format("[ERROR] Directory %s does not exist", dir.getName()));
            return;
        } else if (!dir.isDirectory()) {
            System.out.println(String.format("[ERROR] %s is not a directory!", dir.getName()));
            return;
        }

        images = dir.listFiles();
        for (File image : images) {
            imageList.add(image);
            totalImages++;
        }
    }

    //Methods
    //Image Stacker Functionality
    public void stack() throws FileNotFoundException, UnsupportedFileFormatException {
        PPMImage currentImage;
        RGBPixel index;
        RGBImage stackedMatrix;
        PixelAccumulator [][] stackedImagePixelMatrix = new PixelAccumulator[0][0];
        int height, width;
        short R, G, B;

        height = width = 0;
        for(int i = 0; i < totalImages; i++){
            try {
                currentImage = new PPMImage(imageList.get(i));

                //Correct Height
                if(height == 0){
                    height = currentImage.height;
                }
                else if(height != currentImage.height){
                    return;
                }

                //Correct Width
                if(width == 0){
                    width = currentImage.width;
                }
                else if(width != currentImage.width){
                    return;
                }
            }
            catch (FileNotFoundException | UnsupportedFileFormatException exception) {
                return;
            }


            //Initialize Accumulator
            if(i == 0){
                stackedImagePixelMatrix = new PixelAccumulator[currentImage.height][currentImage.width];
                for(int row = 0; row < height; row++){
                    for(int col = 0; col < width; col++){
                        stackedImagePixelMatrix[row][col] = new PixelAccumulator(0,0,0);
                    }
                }
            }


            //Stack Image
            for(int row = 0; row < height; row++){
                for(int col = 0; col < width; col++){
                    index = currentImage.pixelMatrix[row][col];

                    stackedImagePixelMatrix[row][col].incrementRed(index.getRed());
                    stackedImagePixelMatrix[row][col].incrementGreen(index.getGreen());
                    stackedImagePixelMatrix[row][col].incrementBlue(index.getBlue());
                }
            }
        }

        stackedMatrix = new RGBImage(width, height, 255);
        stackedMatrix.pixelMatrix = new RGBPixel[height][width];
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                R = (short) (stackedImagePixelMatrix[row][col].getR() / totalImages);
                G = (short) (stackedImagePixelMatrix[row][col].getG() / totalImages);
                B = (short) (stackedImagePixelMatrix[row][col].getB() / totalImages);

                stackedMatrix.pixelMatrix[row][col] = new RGBPixel(R, G, B);
            }
        }
        stackedImagePPM = new PPMImage(stackedMatrix);
    }


    public PPMImage getStackedImage(){
        return stackedImagePPM;
    }


    //Class to Accumulate Pixel RGB Values
    private class PixelAccumulator {
        long R, G, B;
        public PixelAccumulator (int R, int G, int B){
            this.R = R;
            this.G = G;
            this.B = B;
        }
        long getR(){
            return R;
        }
        long getG(){
            return G;
        }
        long getB(){
            return B;
        }
        void incrementRed(int red){
            R += red;
        }

        void incrementGreen(int green){
            G += green;
        }

        void incrementBlue(int blue){
            B += blue;
        }
    }
}

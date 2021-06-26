package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class YUVImage {
    YUVPixel[][] pixelMatrix;
    int height, width;

    public YUVImage(int width, int height) {
        this.height = height;
        this.width = width;

        pixelMatrix = new YUVPixel[height][width];

        pixelMatrixInit();
    }

    public YUVImage(YUVImage copyImg) {
        height = copyImg.height;
        width = copyImg.width;

        //Create Matrix
        pixelMatrix = new YUVPixel[height][width];

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                pixelMatrix[i][j] = new YUVPixel(copyImg.pixelMatrix[i][j]);
            }
        }
    }

    public YUVImage(RGBImage RGBImg) {
        //Create Matrix
        pixelMatrix = new YUVPixel[RGBImg.height][RGBImg.width];
        height = RGBImg.height;
        width = RGBImg.width;

        //Convert YUV to RGB
        convertRGBImage(RGBImg);
    }

    public YUVImage(java.io.File file) throws FileNotFoundException, UnsupportedFileFormatException {
        //File Must Exist
        if (!file.exists() || !file.canRead()) {
            throw new java.io.FileNotFoundException();
        }

        //Try to Scan File
        try (Scanner scanner = new Scanner(file)) {
            if (!scanner.next().equals("YUV3")) {
                throw new UnsupportedFileFormatException();
            }

            //Image Dimensions
            width = scanner.nextInt();
            height = scanner.nextInt();


            //Create Array
            pixelMatrix = new YUVPixel[height][width];

            //Initialize Matrix
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    pixelMatrix[i][j] = new YUVPixel(scanner.nextShort(), scanner.nextShort(), scanner.nextShort());
                }
            }
        } catch (UnsupportedFileFormatException exception) {
            throw new UnsupportedFileFormatException();
        } catch (java.io.FileNotFoundException exception) {
            throw new java.io.FileNotFoundException();
        }
    }

    //Used in the Constructor
    private void pixelMatrixInit() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                pixelMatrix[i][j] = new YUVPixel((short) 16, (short) 128, (short) 128);
            }
        }
    }

    //Used in the Constructor
    private void convertRGBImage(RGBImage RGBImg) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelMatrix[i][j] = new YUVPixel(RGBImg.pixelMatrix[i][j]);
            }
        }
    }

    public String toString() {
        //Append Image "HEADER"
        StringBuilder content = new StringBuilder();
        content.append(String.format("YUV3\n%d %d\n", width, height));

        //Append Image Content
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                content.append(pixelMatrix[i][j].toString());
            }
        }
        return content.toString();
    }


    void toFile(java.io.File file) {
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(toString());
            writer.close();
        } catch (IOException exception) {
            System.out.println("ERROR");
        }
    }

    void equalize(){
        YUVPixel index;
        //Create a Histogram for "This" Object
        Histogram histogram = new Histogram(this);

        //Equalize Pixels
        histogram.equalize();

        //Update YUVPixel[][] Matrix
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                index = pixelMatrix[i][j];
                index.setY(histogram.getEqualizedLuminosity(index.getY()));
            }
        }
    }
}

package ce326.hw2;

public class RGBImage implements Image {
    //Attributes
    int width, height, colordepth;
    static int MAX_COLORDEPTH = 255;
    RGBPixel [][] pixelMatrix;

    //Default Constructor
    public RGBImage(){}

    public RGBImage(int width, int height, int colordepth){
        this.width = width;
        this.height = height;
        this.colordepth = colordepth;
    }
    public RGBImage(RGBImage copyImg){
        if(copyImg == null)
            return;

        if(copyImg.pixelMatrix == null)
            return;

        width = copyImg.width;
        height = copyImg.height;
        colordepth = copyImg.colordepth;

        pixelMatrix = new RGBPixel[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                pixelMatrix[i][j] = new RGBPixel(copyImg.pixelMatrix[i][j]);
            }
        }

    }
    public RGBImage(YUVImage YUVImg){
        width = YUVImg.width;
        height = YUVImg.height;
        colordepth = 255;

        pixelMatrix = new RGBPixel[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                pixelMatrix[i][j] = new RGBPixel(YUVImg.pixelMatrix[i][j]);
            }
        }
    }
    int getWidth(){
        return width;
    }
    int getHeight(){
        return height;
    }
    int getColorDepth(){
        return MAX_COLORDEPTH;
    }
    RGBPixel getPixel(int row, int col){
        return pixelMatrix != null ? pixelMatrix[row][col] : null;
    }
    void setPixel(int row, int col,  RGBPixel pixel){
        pixelMatrix[row][col] = pixel;
    }

    //Interface Method Implementation
    //Gray Filter Image
    public void grayscale(){
        //Gray = Red * 0.3 + Green * 0.59 + Blue * 0.11
        if(pixelMatrix == null)
            return;

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                short gray = (short) (pixelMatrix[i][j].getRed() * 0.3 + pixelMatrix[i][j].getGreen() * 0.59 + pixelMatrix[i][j].getBlue() * 0.11);
                pixelMatrix[i][j].setRGB(gray, gray, gray);
            }
        }
    }
    public void doublesize(){
        RGBPixel [][] doubleSizeMatrix;

        doubleSizeMatrix = new RGBPixel[height * 2][width * 2];

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                doubleSizeMatrix[i * 2][j * 2] = pixelMatrix[i][j];
                doubleSizeMatrix[i * 2 + 1][j * 2] = pixelMatrix[i][j];
                doubleSizeMatrix[i * 2][j * 2 + 1] = pixelMatrix[i][j];
                doubleSizeMatrix[i * 2 + 1][j * 2 + 1] = pixelMatrix[i][j];
            }
        }
        width *= 2;
        height *= 2;

        pixelMatrix = doubleSizeMatrix;
    }
    public void halfsize(){
        int red, green, blue;
        RGBPixel [][] halfSizeMatrix;
        int newWidth, newHeight;

        newWidth = width / 2;
        newHeight = height / 2;
        halfSizeMatrix = new RGBPixel[newHeight][newWidth];

        for(int i = 0; i < newHeight; i++){
            for(int j = 0; j < newWidth; j++){
                red =
                        pixelMatrix[i * 2][j * 2].getRed() +
                        pixelMatrix[i * 2 + 1][j * 2].getRed() +
                        pixelMatrix[i * 2][j * 2 + 1].getRed() +
                        pixelMatrix[i * 2 + 1][j * 2 + 1].getRed();

                green =
                        pixelMatrix[i * 2][j * 2].getGreen() +
                        pixelMatrix[i * 2 + 1][j * 2].getGreen() +
                        pixelMatrix[i * 2][j * 2 + 1].getGreen() +
                        pixelMatrix[i * 2 + 1][j * 2 + 1].getGreen();


                blue =
                        pixelMatrix[i * 2][j * 2].getBlue() +
                        pixelMatrix[i * 2 + 1][j * 2].getBlue() +
                        pixelMatrix[i * 2][j * 2 + 1].getBlue() +
                        pixelMatrix[i * 2 + 1][j * 2 + 1].getBlue();


                halfSizeMatrix[i][j] = new RGBPixel((short) (red / 4), (short) (green / 4), (short) (blue / 4));
            }
        }
        width = newWidth;
        height = newHeight;
        pixelMatrix = halfSizeMatrix;
    }
    public void rotateClockwise(){
        RGBPixel [][] clockwiseMatrix = new RGBPixel[width][height];
        int row = 0, col = 0, tmp;
        //Horizontal
        for(int i = 0; i < width; i++){
            //Vertical
            for(int j = height - 1; j > -1; j--){
                clockwiseMatrix[row][col] = pixelMatrix[j][i];
                col = (++col % height);
            }
            row = (++row % width);
        }

        //Swap Width-Height Values
        tmp = width;
        width = height;
        height = tmp;

        //Swap Matrix
        pixelMatrix = clockwiseMatrix;
    }
}

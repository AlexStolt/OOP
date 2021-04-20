package ce326.hw2;

public class YUVPixel {
    //Attributes
    private short Y, U, V;

    //Constructors
    public YUVPixel(short Y, short U, short V){
        this.Y = Y;
        this.U = U;
        this.V = V;
    }
    public YUVPixel(YUVPixel pixel){
        Y = pixel.getY();
        U = pixel.getU();
        V = pixel.getV();
    }
    public YUVPixel(RGBPixel pixel){
        //Get RGB Values
        short R = pixel.getRed(), G = pixel.getGreen(), B = pixel.getBlue();

        //Convert RGB to YUV
        Y = (short) (((66 * R + 129 * G + 25 * B + 128) >> 8) + 16);
        U =  (short) (((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128);
        V = (short) ((( 112 * R - 94 * G - 18 * B + 128) >> 8) + 128);
    }

    //Methods
    short getY(){
        return Y;
    }
    short getU(){
        return U;
    }
    short getV(){
        return V;
    }
    void setY(short Y){
        this.Y = Y;
    }
    void setU(short U){
        this.U = U;
    }
    void setV(short V){
        this.V = V;
    }

    //Custom Methods
    public String toString(){
        return String.format("%d %d %d\n", getY(), getU(), getV());
    }
}

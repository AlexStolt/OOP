package ce326.hw2;

public class RGBPixel {
    //Attributes
    int color = 0;

    //Constructors (Static Polymorphism)
    public RGBPixel(short red, short green, short blue){
        setRGB(red, green, blue);
    }
    public RGBPixel(RGBPixel pixel){
        color = pixel.getRGB();
    }

    public RGBPixel(YUVPixel pixel){
        short R, G, B, C, D, E;

        //YUV Values
        C = (short) (pixel.getY() - 16);
        D = (short) (pixel.getU() - 128);
        E = (short) (pixel.getV() - 128);

        //RGB Values
        R = clip((short) ((298 * C + 409 * E + 128) >> 8));
        G = clip((short) ((298 * C - 100 * D - 208 * E + 128) >> 8));
        B = clip((short) ((298 * C + 516 * D + 128) >> 8));

        //Set RGB Values
        setRGB(R, G, B);
    }

    private short clip(short value){
        if(value < 0){
            return 0;
        }
        else if(value > 255){
            return 255;
        }
        return value;
    }


    //Methods
    //Return Red Color in Decimal
    short getRed(){
        return (short) (this.color >> 16);
    }
    //Return Green Color in Decimal
    short getGreen(){
        return (short) ((this.color >> 8) & 0x00FF);
    }
    //Return Blue Color in Decimal
    short getBlue(){
        return (short) (this.color & 0x00FF);
    }
    //Set Red Color
    void setRed(short red){
        color = ((red << 16) | 0x0000FFFF) & (color | 0x00FF0000);
    }
    //Set Green Color
    void setGreen(short green){
        color = ((green << 8) | 0x00FF00FF) & (color | 0x0000FF00);
    }
    //Set Blue Color
    void setBlue(short blue){
        color = (blue| 0x00FFFF00) & (color | 0x000000FF);
    }
    //Return Color
    int getRGB(){
        return color;
    }
    //Set Color based on Value
    void setRGB(int value){
        color = value;
    }
    //Set Color based on Values
    final void setRGB(short red, short green, short blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }
    //Return RGB
    public String toString(){
        return String.format("%d %d %d\n", getRed(), getGreen(), getBlue());
    }


}

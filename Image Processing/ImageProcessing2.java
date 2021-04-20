
package ce326.hw2;

import java.util.*;
import java.io.*;

public class ImageProcessing2 {

  public static void main(String []args) {
    PPMImage rgbImg = null;
    Scanner sc = new Scanner(System.in);
    YUVImage yuvImg = null;
    String filename;
    File file;
    do {
      System.out.print(menu());
      String input = sc.next();
      switch(input.substring(0,1).toLowerCase()) {
        case "i":
          System.out.print("Enter filename to import: ");
          filename = sc.next();
          try(Scanner fsc = new Scanner(new File(filename))) {
            String magicNumber = fsc.next();
            if(magicNumber.equals("P3"))
              rgbImg = new PPMImage(new File(filename));
            else if(magicNumber.equals("YUV3")) {
              yuvImg = new YUVImage(new File(filename));
              rgbImg = new PPMImage(yuvImg);
            }
            else
              System.out.println("\nERROR: Invalid magic number for input filename!\n");
          } catch(FileNotFoundException ex) {
            System.err.println("File \""+filename+"\" was not found!");
          } catch(UnsupportedFileFormatException ex) {
            System.out.format("Incorrectly formatted file '%s'%n"+filename);
          }
          break;
        case "e":
          System.out.println("Export as:\n\t(p)PPM file\n\t(y)YUV file\n");
          input = sc.next();
          switch(input.toLowerCase().substring(0,1)) {
            case "p":
              System.out.print("Enter PPM filename to export to: ");
              filename = sc.next();
              file = new File(filename);
              /*
              if(file.exists()) {
                System.out.print("File exists! Overwrite? (Y/N) ");
                input = sc.next().substring(0,1).toLowerCase();
                if(input.compareTo("y")!=0) {
                  System.out.println("Skipping overwrite...");
                  break;
                }
              }*/             
              rgbImg.toFile(file);
              break;            
            case "y":
              System.out.print("Enter YUV filename to export to: ");
              filename = sc.next();
              file = new File(filename);
              /*
              if(file.exists()) {
                System.out.print("File exists! Overwrite? (Y/N) ");
                input = sc.next().substring(0,1).toLowerCase();
                if(input.compareTo("y")!=0) {
                  System.out.println("Skipping overwrite...");
                  break;
                }
              }*/
              yuvImg = new YUVImage(rgbImg);
              yuvImg.toFile(file);
              break;
          }          
          break;
        case "r":
          rgbImg.rotateClockwise();
          break;
        case "d":
          rgbImg.doublesize();
          break;
        case "h":
          rgbImg.halfsize();
          break;
        case "g":
          rgbImg.grayscale();
          break;        
        case "z":
          yuvImg = new YUVImage(rgbImg);          
          yuvImg.equalize();          
          rgbImg = new PPMImage(yuvImg);          
          break;
        case "p":
          yuvImg = new YUVImage(rgbImg);          
          Histogram h = new Histogram(yuvImg);
          System.out.println(h.toString());
          rgbImg = new PPMImage(yuvImg);          
          break;
        case "s":
          System.out.println("Enter directory to load images from: ");
          String dirname = sc.next();
          File directory = new File(dirname);
          try {
            PPMImageStacker stacker = new PPMImageStacker(directory);
            stacker.stack();
            rgbImg = stacker.getStackedImage();
          }
          catch(FileNotFoundException ex) {
            System.out.format("Unable to locate directory '%s'%n", dirname);
          }
          catch(UnsupportedFileFormatException ex) {
            System.out.format("Incorrectly formatted file inside directory '%s'%n"+dirname);
          }
          break;
        case "q":
          return;
        default:
      }
    } while(true);
  }
  
  public static String menu() {
    StringBuffer str = new StringBuffer();
    str.append("+------  MENU ------+\n");
    str.append("(i)mport image\n");
    str.append("(e)xport image\n");
    str.append("(r)otate image\n");
    str.append("(d)ouble image size\n");
    str.append("(h)alf image size\n");
    str.append("(g)rayscale image\n");
    str.append("(z)equalize histogram\n");
    str.append("(p)rint histogram\n");
    str.append("(s)tacking algorithm\n");
    str.append("(q)uit\n");
    str.append("+-------------------+\n\n");
    str.append("Your option: ");
    return str.toString();
  }
}


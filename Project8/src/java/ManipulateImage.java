import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
import org.apache.commons.codec.binary.Base64;
 
public class ManipulateImage {
 
    // Decode String into an Image
    public static void convertStringtoImage(String encodedImageStr,    String fileName) {
 
        try {
            byte[] imageByteArray; 
            imageByteArray = Base64.decodeBase64(encodedImageStr.getBytes()); 
 
            // Write Image into File system - Make sure you update the path
            FileOutputStream imageOutFile = new FileOutputStream("G:\\Documents\\Project VIII\\Project8\\web\\images\\" + fileName+".jpg");
            imageOutFile.write(imageByteArray);
 
            imageOutFile.close();
            FileOutputStream imageOutFile2 = new FileOutputStream("C:\\xampp\\htdocs\\images\\" + fileName+".jpg");
            imageOutFile2.write(imageByteArray);
 
            imageOutFile2.close();
 
            System.out.println("Complaint Successfully Stored");
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Exception while converting the Image " + ioe);
        }
 
    }
 
}
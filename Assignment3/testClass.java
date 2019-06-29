import java.io.*;
import java.util.*;

public class testClass {
    public static void main(String[] args) throws IOException{
        InputStream input = new FileInputStream(args[0]);
        ArrayList<Integer> bytes = new ArrayList<>();
        Integer currByte = input.read();
        while(currByte != -1){
            bytes.add(currByte);
            currByte = input.read();
        }

        for(Integer bytee : bytes){
            System.out.print(bytee + " ");
        }
    }
}

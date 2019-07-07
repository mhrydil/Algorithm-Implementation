import java.util.*;

public class Add128 implements SymCipher {

    private static byte[] key = new byte[128];

    // initialize the byte array (key) with 128 random ints from 0 - 255
    public Add128(){
        Random r = new Random();
        for(int i=0; i<128; i++){
            key[i] = (byte) (r.nextInt() % 256);
        }

    }

    //copies key that is passed in to the private key object in this class
    public Add128(byte[] byteKey){
        for(int i=0; i<byteKey.length; i++){
            key[i] = byteKey[i];
        }
    }

    // return the key
    public byte[] getKey(){
        return key;

    }

    //encode the string using the key and return the encoded version as an array of bytes
    public byte[] encode(String s){
        byte[] sArray = s.getBytes();
        for(int i=0; i<s.length(); i++){
            sArray[i] = (byte) (key[i % 128] + sArray[i]);
        }
        return sArray;
    }


//    public static byte[] encodeTest(String s){
//        byte[] sArray = s.getBytes();
//        for(int i=0; i<sArray.length; i++){
//            sArray[i] = (byte) (key[i % 128] + sArray[i]);
//        }
//        return sArray;
//    }

    //decode the byte array and return it as a string
    public String decode(byte[] bytes){
        for(int i=0; i<bytes.length; i++){
            bytes[i] = (byte) (bytes[i] - key[i % 128]);
        }
        return new String(bytes);
    }

//    public static String decodeTest(byte[] bytes){ //for testing purposes
//        for(int i=0; i<bytes.length; i++){
//            bytes[i] = (byte) (bytes[i] - key[i % 128]);
//        }
//        return new String(bytes);
//    }



//    public static void main(String[] args){ //for testing purposes
//        Add128 newAdd = new Add128();
//        String testString = "This is a test string for the add128 encode/decode methods sldkfjsdkfjsldkjfls " +
//                "dflkjsdflsdfljsdlkfjsd fksldjf  dlskdjflsdjflsdkjf j" +
//                "\n sldkjflsdkjflskdjflskdjflskdjflskdjflksdjlkjlkjlkjlkjlkjlkjlkj";
//        byte[] encoded = encodeTest(testString);
//        System.out.println(testString);
//        System.out.println();
//        for(byte b: key){
//            System.out.print(b+ " ");
//        }
//
//        System.out.println();
//        for(byte b : encoded){
//            System.out.print(b+ " ");
//        }
//        System.out.println();
//        System.out.print(decodeTest(encoded));
//
//    }

}

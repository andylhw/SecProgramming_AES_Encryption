package FileEncryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class Utils {
	public static String toString(byte[] input) {
		return new String(input);
	}
	
	public static String toHexString(byte[] input) {
		String hexString = "";
		int length = input.length;
		
		for(int i = 0; i < length; i++) {
			if(i != length - 1) {
				hexString += String.format("%02X:", input[i]);
			} else {
				hexString += String.format("%02X", input[i]);
			}
		}
		
		return hexString;
	}
	
	public static String toHexString(byte[] input, int size) {
		String hexString = "";
		int length = size;
		
		for(int i = 0; i < length; i++) {
			if(i != length - 1) {
				hexString += String.format("%02X:", input[i]);
			} else {
				hexString += String.format("%02X", input[i]);
			}
		}
		
		return hexString;
	}
	
	
	
	public static byte[] toByteArray(String string)
    {
        byte[] bytes = new byte[string.length()];
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++)
        {
            bytes[i] = (byte)chars[i];
        }

        return bytes;
    }

    public static byte[] concatByteArray(byte[] first, byte[] second){
		byte[] result_concat = new byte[first.length + second.length];
		System.arraycopy(first, 0, result_concat, 0, first.length);
		System.arraycopy(second, 0, result_concat, first.length, second.length);

		return result_concat;
	}

	public static boolean checkSameFile(String filePath1, String filePath2) throws Exception{
		//전->후 파일 비교. 왠만하면 텍스트파일로 비교하는게 좋을 것 같긴한데, jpg파일 비교도 확인했습니다.
		FileInputStream fileInputStream = new FileInputStream(filePath1);
		FileInputStream fileInputStream2 = new FileInputStream(filePath2);
		MessageDigest md = MessageDigest.getInstance("MD5");
		MessageDigest md2 = MessageDigest.getInstance("MD5");
		byte[] dataBytes = new byte[1024];
		int nRead = 0;
		while((nRead = fileInputStream.read(dataBytes)) != -1){
			md.update(dataBytes, 0, nRead);
		}
		byte[] mdBytes = md.digest();

		byte[] dataBytes2 = new byte[1024];

		int nRead2 = 0;
		while((nRead2 = fileInputStream2.read(dataBytes2)) != -1){
			md2.update(dataBytes2, 0, nRead2);
		}
		byte[] mdBytes2 = md2.digest();

		fileInputStream.close();
		fileInputStream2.close();
		System.out.print("Same File Check: ");
		if(Utils.toHexString(mdBytes).equals(Utils.toHexString(mdBytes2))){
			System.out.println("true");
			return true;
		}
		else{
			System.out.println("false");
			return false;
		}
	}

	public static int getFileSize(String filePath) throws Exception{
		Path path = Paths.get(filePath);
		long bytes = 0;
		try{
			bytes = Files.size(path);
			System.out.println(String.format("Input file: %,d bytes", bytes));
		} catch(Exception e){
			e.printStackTrace();
		}
		return (int) bytes;
	}

}

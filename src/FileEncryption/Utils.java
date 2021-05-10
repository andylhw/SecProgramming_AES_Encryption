package FileEncryption;

import java.io.File;
import java.io.FileReader;

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

	public static boolean checkSameFile(String path, String filePath1, String filePath2) throws Exception{
		//전->후 파일 비교. 왠만하면 텍스트파일로 비교하는게 좋을 것 같긴한데, jpg파일 비교도 확인했습니다.
		String password_check_file = path + "password_check";
		String password_check_file2 = path + "password_check`";
		File file1 = new File(password_check_file);
		File file2 = new File(password_check_file2);

		FileReader file_reader1 = new FileReader(file1);
		FileReader file_reader2 = new FileReader(file2);
		String strFile1="";
		String strFile2="";
		int cur=0;

		while((cur = file_reader1.read()) != -1) {
			strFile1 += (char)cur;
		}
		while((cur = file_reader2.read()) != -1) {
			strFile2 += (char)cur;
		}

		if(strFile1.equals(strFile2)) {
			System.out.println("두 개의 파일 내용이 같습니다");
			file_reader1.close();
			file_reader2.close();
			return true;
		}
		else {
			System.out.println("두 개의 파일 내용이 다릅니다.");
			System.out.println(strFile1);
			System.out.println(strFile2);

			file_reader1.close();
			file_reader2.close();
			return false;
		}
	}

}

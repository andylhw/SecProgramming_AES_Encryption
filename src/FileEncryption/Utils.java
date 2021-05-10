package FileEncryption;

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

}

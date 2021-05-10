package FileEncryption;

public class MainClient {
    public static void main(String[] args) throws Exception {
        String password = "password";
        String path = System.getProperty("user.dir");
        System.out.println("Working Directory: "+path);
        String encrypt = "enc";
        String decrypt = "dec";
        String inputPath = "D:\\hwawon_github\\SecProgramming_FinalAssignment\\test\\input\\";
        String outputPath = "D:\\hwawon_github\\SecProgramming_FinalAssignment\\test\\output\\";
        String resultPath = "D:\\hwawon_github\\SecProgramming_FinalAssignment\\test\\result\\";
        byte[] derivedKey;
        byte[] salt = new byte[] {0x78, 0x57, (byte)0x8e, 0x5a, 0x5d, 0x63, (byte)0xcb, 0x06};

        derivedKey = KeyDerivation.run(password, salt);
        FileEncryption.run(encrypt, salt, derivedKey, inputPath, outputPath);
        FileEncryption.run(decrypt, salt, derivedKey, outputPath, resultPath);
    }
}

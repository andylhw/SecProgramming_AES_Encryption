package FileEncryption;

public class MainClient {
    public static void main(String[] args) throws Exception {
        String password = "password";
        String path = System.getProperty("user.dir");
//        System.out.println("Working Directory: "+path);
        String encrypt = "enc";
        String decrypt = "dec";
        //상대경로 사용
        String inputPath = path+"\\test\\input\\";
        String outputPath = path+"\\test\\output\\";
        //절대경로 사용
        String resultPath = "D:\\hwawon_github\\SecProgramming_FinalAssignment\\test\\result\\";
        byte[] derivedKey;
        byte[] salt = new byte[] {0x78, 0x57, (byte)0x8e, 0x5a, 0x5d, 0x63, (byte)0xcb, 0x06};

        derivedKey = KeyDerivation.run(password, salt);
        //inputPath에 있는 파일을 암호화한 뒤, outputPath에 저장함.
        FileEncryption.run(encrypt, salt, derivedKey, inputPath, outputPath);
        //outputPath에 있는 파일을 암호화한 뒤, resultPath에 저장함.
        FileEncryption.run(decrypt, salt, derivedKey, outputPath, resultPath);

        Utils.checkSameFile(inputPath + "installFile.exe", resultPath+"installFile.exe");

    }
}

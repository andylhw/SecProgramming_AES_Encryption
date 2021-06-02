package FileEncryption;

import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) throws Exception {
        String password;
        String encrypt = "enc";
        String decrypt = "dec";
        String selection;
        System.out.println("File encryption program made by Lee Hwa Won");

        //절대경로 사용
        String path1 = "";
        String path2 = "";
        String inputPath;
        String extension = "exe";
        //String outputPath = "C:\\test\\output\\";
        int loop=1;

        Scanner scanner = new Scanner(System.in);
        while(loop!=0) {
            System.out.println("Please select mode: enc | dec | both | check (exit-> enter exit)");
            selection = scanner.nextLine();
            if(selection.equals("exit")){
                break;
            }
            else if(selection.equals("check")){
                System.out.println("Please enter the first File path");
                path1 = scanner.nextLine();
                System.out.println("Please enter the second File path");
                path2 = scanner.nextLine();
                Utils.checkSameFile(path1, path2);
            }
            else if(selection.equals("enc") ||selection.equals("dec") || selection.equals("both")){
                System.out.println("Please enter the password: ");
                password = scanner.nextLine();
                System.out.println("Please enter the File path: ");
                inputPath = scanner.nextLine();
                if (selection.equals("enc")) {
                    extension = inputPath.substring(inputPath.lastIndexOf(".") + 1);
                }
                //상대경로 사용
//        String inputPath = path+"\\test\\input\\";
//        String outputPath = path+"\\test\\output\\";
//        String resultPath = path+"\\test\\result\\";
                //String resultPath = "D:\\hwawon_github\\SecProgramming_FinalAssignment\\test\\result\\";
                byte[] derivedKey;
                byte[] derivedKey2;
                byte[] salt = new byte[]{0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06};
                derivedKey = KeyDerivation.run(password, salt);
                derivedKey2 = KeyDerivation.run("SSUSW", salt);

                if (selection.equals("both")) {
                    FileEncryption.run(encrypt, salt, derivedKey, inputPath, extension);
                    FileEncryption.run(decrypt, salt, derivedKey, FileEncryption.getFileDir(inputPath) + "Encrypted.enc", extension);
                    Utils.checkSameFile(inputPath, FileEncryption.getFileDir(inputPath) + "Result.exe");
                }
                if (selection.equals("enc")) {
                    FileEncryption.run(encrypt, salt, derivedKey, inputPath, extension);
                }
                if (selection.equals("dec")) {
                    FileEncryption.run(decrypt, salt, derivedKey, inputPath, extension);

                }
            /*
            //inputPath에 있는 파일을 암호화한 뒤, 같은 폴더에 저장하게 함.
            FileEncryption.run(encrypt, salt, derivedKey, inputPath, extension);
            //inputPath에 있는 파일을 암호화한 뒤, outputPath에 저장함.
            //FileEncryption.run(encrypt, salt, derivedKey, inputPath, FileEncryption.getFileDir(inputPath));
            //outputPath에 있는 파일을 암호화한 뒤, resultPath에 저장함.
            FileEncryption.run(decrypt, salt, derivedKey, FileEncryption.getFileDir(inputPath) + "Encrypted.enc", extension);

            Utils.checkSameFile(inputPath, FileEncryption.getFileDir(inputPath) + "Result.exe");
            */
            }
            else{
                System.out.println("Wrong input!");
            }
        }

    }
}

package FileEncryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.Security;


public class FileEncryption {
    //path - Directory path for input, o_path - Directory path for output
    public static void fileEnc(String mode, byte[] salt, byte[] derivedKey, String path, String o_path, String extension) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        //암호화할 파일 이름을 입력해주세요.
        //암호화가 완료되고 파일이 저장될 이름.
        String encryptFileName = "Encrypted.enc";
        String resultFileName;
        byte[] ivBytes = new byte[]{
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00,
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
        SecretKeySpec key = new SecretKeySpec(derivedKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        int readSize_encrypted = 0;
        int[] progressBarCount = new int[20];
        for(int i=0;i<20;i++){
            progressBarCount[i] = 1;
        }
        //password_check 만들기.
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

        long startTime = System.currentTimeMillis();


        if(mode == "enc") {
            System.out.println("Encryption start!");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            String fileDir = getFileDir(path);
            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            int fileSize = Utils.getFileSize(path);
            double progressPercentage;
            FileOutputStream fos_pwCheck = new FileOutputStream(o_path + "password_check");
            FileOutputStream fos_saltCheck = new FileOutputStream(o_path + "Salt");
            FileInputStream fis = new FileInputStream(path);

//            System.out.println(fileDir);
            FileOutputStream fos = new FileOutputStream(o_path + encryptFileName);
            byte Salt[] = salt;
            fos_saltCheck.write(Salt);
            //password_check 구현.

            MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            md.update(derivedKey);
            //As a result of SHA-1, Password_check_big created which is 20 bytes.
            byte[] password_check_big;
            password_check_big = md.digest();
            //Only first 16 bytes of password_check_big will be stored.
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //Use fos_pwCheck to write password_check
            fos_pwCheck.write(password_check);
            fos_pwCheck.close();
            int read = -1;
            try {
                while ((read = fis.read(buffer)) != -1) {
                    readSize_encrypted+=read;
                    //to show the Progress, check how many files are encrypted.
                    progressPercentage = (double) readSize_encrypted/fileSize;
                    if(progressPercentage<=1) {
                        updateProgress(progressPercentage, progressBarCount);
                    }
                    fos.write(cipher.update(buffer, 0, read));
                }
                fos.write(cipher.doFinal());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
                if (fis != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
            System.out.println("Encryption Completed!");
            long endTime = System.currentTimeMillis();
            long takeTime = endTime - startTime;
            System.out.println("Time Elapsed: "+takeTime/1000.0+"sec");

        }
        if (mode == "dec"){
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            byte[] password_check2;
            double progressPercentage;
            int fileSize = Utils.getFileSize(path);

            String fileDirDec = getFileDir(path);

            FileInputStream fis_pwcheck = new FileInputStream(fileDirDec+"password_check");
            FileInputStream fis = new FileInputStream(fileDirDec + encryptFileName);
            FileOutputStream fos = new FileOutputStream(o_path + "Result."+extension);

            //password_check function
            MessageDigest md2 = MessageDigest.getInstance("SHA1", "BC");
            md2.update(derivedKey);
            //As a result of SHA-1, Password_check_big created which is 20 bytes.
            byte[] password_check_big;
            password_check_big = md2.digest();
            //Only first 16 bytes of password_check_big will be stored.
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //Use fos_pwCheck2 to write password_check
            password_check2 = fis_pwcheck.readAllBytes();
//            System.out.println("Original: " + Utils.toHexString(password_check));
//            System.out.println("New: " + Utils.toHexString(password_check2));
            //

            // checking password.
            if(Utils.toHexString(password_check).equals(Utils.toHexString(password_check2))) {
                System.out.println("password_check function passed. Decryption Start");
                int read = -1;
                try {
                    while ((read = fis.read(buffer)) != -1) {
                        readSize_encrypted += read;
                        progressPercentage = (double) readSize_encrypted/fileSize;
                        if(progressPercentage<=1) {
                            updateProgress(progressPercentage, progressBarCount);
                        }
                        //System.out.println(readSize_encrypted);
                        fos.write(cipher.update(buffer, 0, read));
                    }
                    fos.write(cipher.doFinal());
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fis != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
                //파일 사이즈 체크.
//                System.out.println("Origin Size: "+Utils.getFileSize(o_path, "../input/Project.pdf"));
//                System.out.println("Result Size: "+Utils.getFileSize(o_path, "Result.pdf"));
                System.out.println("Decryption Completed!");
                long endTime = System.currentTimeMillis();
                long takeTime = endTime - startTime;
                System.out.println("Time Elapsed: "+takeTime/1000.0+"sec");
            }else{
                System.out.println("Unavailable Key to decrypt.");
            }
        }
    }
    public static void run(String mode, byte[] salt, byte[] derivedKey, String path, String o_path, String extension) throws Exception {
        fileEnc(mode, salt, derivedKey, path, o_path, extension);
    }
    public static void run(String mode, byte[] salt, byte[] derivedKey, String path, String extension) throws Exception {
        run(mode, salt, derivedKey, path, FileEncryption.getFileDir(path), extension);
    }
    private static int updateProgress(double progressPercentage, int[] progressBarCount){
        final int width = 50;
        progressPercentage*=100;

        //20 types of data needed to show every 5%.
        for(int i=0;i<20;i++){
            if(progressPercentage>= 5*(i-1) && progressPercentage < 5*i && progressBarCount[i] == 1){
                System.out.print("Processing... |");
                for(int j=0;j<i;j++){
                    System.out.print("=");
                }
                for(int k=0;k<20-i;k++){
                    System.out.print(" ");
                }
                System.out.print("| "+5*i+"%\r");
                progressBarCount[i]=0;
                return 0;
            }
            //when Process Completed.
            if (progressPercentage >= (double)100) {
                System.out.println("Processing... |====================| 100%!");
                System.out.println("Process Completed!");
                return 0;
            }
        }
        return 0;
    }

    public static String getFileDir(String path){
        File f = new File(path);
        String fileDir = f.getParent()+"/";
        return fileDir;
    }

}

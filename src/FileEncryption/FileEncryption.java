package FileEncryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.Security;


public class FileEncryption {
    public static void fileEnc(String mode, byte[] salt, byte[] derivedKey, String path, String o_path) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        byte[] ivBytes = new byte[]{
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00,
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
        SecretKeySpec key = new SecretKeySpec(derivedKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        int readSize_encrypted = 0;
        //password_check 만들기.
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");



        if(mode == "enc") {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            int fileSize = Utils.getFileSize(path, "installFile.exe");
            double progressPercentage;

            FileOutputStream fos_pwCheck = new FileOutputStream(o_path+"password_check");
            FileOutputStream fos_saltCheck = new FileOutputStream(o_path+"Salt");
            FileInputStream fis = new FileInputStream(path + "installFile.exe");
            FileOutputStream fos = new FileOutputStream(o_path + "Encrypted");
            byte Salt[] = salt;
            fos_saltCheck.write(Salt);
            //password_check 구현.
            byte[] password_check_big;

            MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            md.update(Utils.concatByteArray(derivedKey, salt));
            //SHA-1의 결과로 20바이트짜리 패스워드_체크_빅이 생성됨. 이것을 줄여주자
            password_check_big = md.digest();
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //16바이트로 줄어든 password_check를 fos_pwcheck를 통해서 씀.
            fos_pwCheck.write(password_check);
            fos_pwCheck.close();
            int read = -1;
            try {
                while ((read = fis.read(buffer)) != -1) {
                    readSize_encrypted+=read;
                    progressPercentage = (double) readSize_encrypted/fileSize;
                    if(progressPercentage<=1) {
                        updateProgress(progressPercentage);
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
            System.out.println("암호화 완료!");
            System.out.println(password_check);
            System.out.println(Utils.toHexString(password_check));
        }
        if (mode == "dec"){
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            byte[] password_check2;
            FileInputStream fis_pwcheck = new FileInputStream(path+"password_check");
            FileInputStream fis = new FileInputStream(path + "Encrypted");
            FileOutputStream fos = new FileOutputStream(o_path + "intallFile.exe");

            //password_check 구현.

            MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            md.update(Utils.concatByteArray(derivedKey, salt));
            //SHA-1의 결과로 20바이트짜리 패스워드_체크_빅이 생성됨. 이것을 줄여주자
            byte[] password_check_big;
            password_check_big = md.digest();
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //16바이트로 줄어든 password_check를 fos_pwcheck를 통해서 씀.
            password_check2 = fis_pwcheck.readAllBytes();
            System.out.println("Original: " + Utils.toHexString(password_check));
            System.out.println("New: " + Utils.toHexString(password_check2));
            //

            // 중복체크 완료.
            if(password_check2 == password_check) {
                int read = -1;
                try {
                    while ((read = fis.read(buffer)) != -1) {
                        readSize_encrypted += read;
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
                System.out.println("복호화 완료!");
            }else{
                if(Utils.toHexString(password_check) == Utils.toHexString(password_check2)){
                    System.out.println("이상한데..?");
                }
                System.out.println(password_check);
                System.out.println(password_check);
                System.out.println(password_check2);
            }
        }
    }
    public static void run(String mode, byte[] salt, byte[] derivedKey, String path, String o_path) throws Exception {
        fileEnc(mode, salt, derivedKey, path, o_path);
    }
    public static int updateProgress(double progressPercentage){
        final int width = 50;
        progressPercentage*=100;
        int[] progressBarCount = new int[20];

        for(int i=0;i<20;i++){
            progressBarCount[i]=1;
        }

        //1~ 20개인데 이걸 하려면 percentage = 5% -> 0.05 ->
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
            if (progressPercentage >= (double)100) {
                System.out.println("Processing... |====================| 100%!");
                System.out.println("Process Completed!");
                return 0;
            }
        }
        return 0;



    }

}

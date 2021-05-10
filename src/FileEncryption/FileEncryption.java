package FileEncryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.Security;


public class FileEncryption {
    //path - input에 사용할 디렉토리 경로, o_path - output에 사용될 디렉토리 경로.
    public static void fileEnc(String mode, byte[] salt, byte[] derivedKey, String path, String o_path) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        //암호화할 파일 이름을 입력해주세요.
        String inputFileName = "installFile.exe";
        //암호화가 완료되고 파일이 저장될 이름.
        String encryptFileName = "Encrypted";
        String resultFileName = inputFileName;
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



        if(mode == "enc") {
            System.out.println("암호화 시작!");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            int fileSize = Utils.getFileSize(path, inputFileName);
            double progressPercentage;

            FileOutputStream fos_pwCheck = new FileOutputStream(o_path+"password_check");
            FileOutputStream fos_saltCheck = new FileOutputStream(o_path+"Salt");
            FileInputStream fis = new FileInputStream(path + inputFileName);
            FileOutputStream fos = new FileOutputStream(o_path + encryptFileName);
            byte Salt[] = salt;
            fos_saltCheck.write(Salt);
            //password_check 구현.

            MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            md.update(Utils.concatByteArray(derivedKey, salt));
            //SHA-1의 결과로 20바이트짜리 패스워드_체크_빅이 생성됨. 이것을 줄여주자
            byte[] password_check_big;
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
            System.out.println("암호화 완료!");

        }
        if (mode == "dec"){
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            byte[] password_check2;
            double progressPercentage;
            int fileSize = Utils.getFileSize(path, encryptFileName);

            FileInputStream fis_pwcheck = new FileInputStream(path+"password_check");
            FileInputStream fis = new FileInputStream(path + encryptFileName);
            FileOutputStream fos = new FileOutputStream(o_path + resultFileName);

            //password_check 구현.
            MessageDigest md2 = MessageDigest.getInstance("SHA1", "BC");
            md2.update(Utils.concatByteArray(derivedKey, salt));
            //SHA-1의 결과로 20바이트짜리 패스워드_체크_빅이 생성됨. 이것을 줄여주자
            byte[] password_check_big;
            password_check_big = md2.digest();
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //16바이트로 줄어든 password_check를 fos_pwcheck를 통해서 씀.
            password_check2 = fis_pwcheck.readAllBytes();
//            System.out.println("Original: " + Utils.toHexString(password_check));
//            System.out.println("New: " + Utils.toHexString(password_check2));
            //

            // 중복체크 완료.
            if(Utils.toHexString(password_check).equals(Utils.toHexString(password_check2))) {
                System.out.println("password_check검사 통과. 복호화 시작");
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
                System.out.println("복호화 완료!");
            }else{
                System.out.println("기존 파일의 Key와 맞지 않습니다.");
            }
        }
    }
    public static void run(String mode, byte[] salt, byte[] derivedKey, String path, String o_path) throws Exception {
        fileEnc(mode, salt, derivedKey, path, o_path);
    }
    public static int updateProgress(double progressPercentage, int[] progressBarCount){
        final int width = 50;
        progressPercentage*=100;

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

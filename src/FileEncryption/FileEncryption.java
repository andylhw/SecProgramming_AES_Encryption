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
        int readSize_now = 0;
        //password_check 만들기.
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");



        if(mode == "enc") {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];

            FileOutputStream fos_pwcheck = new FileOutputStream(o_path+"password_check");
            FileInputStream fis = new FileInputStream(path + "Project.pdf");
            FileOutputStream fos = new FileOutputStream(o_path + "Encrypted");

            //password_check 구현.
            byte[] password_check_big;

            MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            md.update(Utils.concatByteArray(derivedKey, salt));
            //SHA-1의 결과로 20바이트짜리 패스워드_체크_빅이 생성됨. 이것을 줄여주자
            password_check_big = md.digest();
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //16바이트로 줄어든 password_check를 fos_pwcheck를 통해서 씀.
            fos_pwcheck.write(password_check);
            fos_pwcheck.close();
            int read = -1;
            try {
                while ((read = fis.read(buffer)) != -1) {
                    readSize_now+=read;
                    System.out.println(readSize_now);
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
            boolean pw_check=false;
            int BUF_SIZE = 1024;
            byte[] buffer = new byte[BUF_SIZE];
            FileOutputStream fos_pwcheck = new FileOutputStream(path+"password_check`");
            FileInputStream fis = new FileInputStream(path + "Encrypted");
            FileOutputStream fos = new FileOutputStream(o_path + "Result.pdf");

            //password_check 구현.
            byte[] password_check_big;

            MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            md.update(Utils.concatByteArray(derivedKey, salt));
            //SHA-1의 결과로 20바이트짜리 패스워드_체크_빅이 생성됨. 이것을 줄여주자
            password_check_big = md.digest();
            byte[] password_check = new byte[16];
            System.arraycopy(password_check_big, 0, password_check, 0, 16);
            //16바이트로 줄어든 password_check를 fos_pwcheck를 통해서 씀.
            fos_pwcheck.write(password_check);
            fos_pwcheck.close();
            //
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
                pw_check=true;
            }
            else {
                System.out.println("두 개의 파일 내용이 다릅니다.");
                System.out.println(strFile1);
                System.out.println(strFile2);
            }
            file_reader1.close();
            file_reader2.close();
            // 중복체크 완료.
            if(pw_check==true) {
                int read = -1;
                try {
                    while ((read = fis.read(buffer)) != -1) {
                        readSize_now += read;
                        System.out.println(readSize_now);
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
                System.out.println("복호화 완료!");
            }
        }
    }
    public static void run(String mode, byte[] salt, byte[] derivedKey, String path, String o_path) throws Exception {
        fileEnc(mode, salt, derivedKey, path, o_path);
    }

}

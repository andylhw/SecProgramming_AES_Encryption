package FileEncryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;

public class FileEncryption {
    public static void fileEnc(int mode, byte[] salt, byte[] derivedKey, String path, String o_path) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        byte[] ivBytes = new byte[]{
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00,
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
        SecretKeySpec key = new SecretKeySpec(derivedKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        int BUF_SIZE = 1024;
        byte[] buffer = new byte[BUF_SIZE];

        FileInputStream fis = new FileInputStream(path);
        FileOutputStream fos = new FileOutputStream("Encrypted");
        int read = -1;
        try{
            while((read = fis.read(buffer)) != -1){
                System.out.println(read);
                fos.write(cipher.update(buffer, 0, read));
            }
            fos.write(cipher.doFinal());
        }finally{
            if(fos!=null){
                try{fos.close();}catch(IOException e){ }
            }
            if(fis!=null){
                try{fos.close();}catch(IOException e){ }
            }
        }
        System.out.println("암호화 완료!");
    }

}

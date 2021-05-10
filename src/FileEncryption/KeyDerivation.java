package FileEncryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeyDerivation {
    public static byte[] pbkdf1(String password, byte[] salt,  int dkLen, int iteration) throws Exception {

        byte[] passwordByte = Utils.toByteArray(password);

        Security.addProvider(new BouncyCastleProvider());

        byte[] input = Utils.concatByteArray(passwordByte, salt);
//        byte[] input = new byte[passwordByte.length + salt.length];
//        System.arraycopy(passwordByte, 0, input, 0, passwordByte.length);
//        System.arraycopy(salt, 0, input, passwordByte.length, salt.length);

        if(dkLen>20){
            System.out.println("dkLen이 20보다 큽니다.");
            return null;
        }
        byte[] dk = new byte[dkLen];
        MessageDigest md = MessageDigest.getInstance("SHA1", "BC");

        dk = md.digest(input);
        for(int i=0;i<iteration-1;i++){
            dk=md.digest(dk);
        }

        byte[] result = new byte[16];
        System.arraycopy(dk, 0, result, 0, dkLen);

        return result;
    }
    public static byte[] run(String password, byte[] salt) throws Exception {
        int count = 1000;
        int dkLen = 16;

        byte[] result = pbkdf1(password, salt, dkLen, count);

//        KeyDerivation 테스트 스크립트
        System.out.println("Result: " + Utils.toHexString(result));
        return result;
    }
}

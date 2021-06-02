# 보안 프로그래밍 기말고사 대체과제
## AES를 활용한 암호화/복호화 

### API 설명
* FileEncryption.java
  * public static void fileEnc
    * 파일을 암호화, 복호화 하는 프로그램이다.
      * input - mode(암호화 - enc, 복호화 - dec), salt(Salt를 통한 보안성 향상), derivedKey(KeyDerivation을 통해서 생성한 키), String path(원본파일 경로), String o_path(파일을 저장할 경로), String extension(파일을 복호화할 때, 원본 파일의 확장자명을 기억하게 하기 위한 문자열)
      * Output - void
      * Code Review
 ```java
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
            md.update(Utils.concatByteArray(derivedKey, salt));
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
```
*
*
*
+

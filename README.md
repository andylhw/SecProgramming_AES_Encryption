# 보안 프로그래밍 기말고사 대체과제
## AES를 활용한 암호화/복호화 

### API 설명
* FileEncryption.java
  * public static void fileEnc
    * 파일을 암호화, 복호화 하는 프로그램이다.
      * input - mode(암호화 - enc, 복호화 - dec), salt(Salt를 통한 보안성 향상), derivedKey(KeyDerivation을 통해서 생성한 키), String path(원본파일 경로), String o_path(파일을 저장할 경로), String extension(파일을 복호화할 때, 원본 파일의 확장자명을 기억하게 하기 위한 문자열)
      * Output - void
      * 메소드 실행원리(암호화) 
        * 입력된 salt값을 "Salt"파일에 저장을 함.
        * MessageDigest를 사용한 md객체 생성 후, BouncyCastle에서 제공한 SHA1함수를 사용한다고 명시.
        * md값에 derivedKey와 salt값을 Concat해서 update시킴
        * password_check를 생성하여, 해당 md객체 값을 digest해서 "password_check"값에 저장시킴(후에 확인용)
        * 후에, 암호화 작업이 시작됨. 암호화 작업 도중에 완료된 바이트를 읽어서, 원본 사이즈랑 비교하면서 percentage만듬
        * percentage가 5%마다, Progress Bar가 업데이트 되게함. \r을 사용하여 출력된 것을 지우면서 갱신시킴.
        * 완료되면, 완료되었다는 문구 출력과 암호화하는데 걸린시간을 출력시킴.
      * 메소드 실행원리(복호화) 
        * MessageDigest를 사용한 md객체 생성 후, BouncyCastle에서 제공한 SHA1함수를 사용한다고 명시.
        * md값에 derivedKey와 salt값을 Concat해서 update시킴
        * password_check2를 생성하여, 해당 md객체 값을 digest해서 "password_check"값에 대조시킴(확인용)
        * password_check2가 password_check가 같다고 하면, 복호화 과정을 진행시키고, 다르면, 종료됨.
        * 후에, 복호화 작업이 시작됨. 암호화 작업 도중에 완료된 바이트를 읽어서, 원본 사이즈랑 비교하면서 percentage만듬
        * percentage가 5%마다, Progress Bar가 업데이트 되게함. \r을 사용하여 출력된 것을 지우면서 갱신시킴.
        * 완료되면, 완료되었다는 문구 출력과 암호화하는데 걸린시간을 출력시킴.
  * public statid void run(mode, salt, derivedKey, path, o_path, extension)
    * FileEncryption을 시작하게 하는 메소드 
  * public static void run(mode, salt, derivedKey, path, extensions)
    * main프로그램과, run프로그램을 자연스럽게 연결시켜주는 메소드. 
      * o_path는 암호화&복호화 파일과 같은 디렉토리에 만들고 싶을때 씀.
        * getFileDir를 사용하여, 디렉토리 위에 쓸수있게함.
  * private static int updateProgress(progressPercentage, progressBarCount)
    * ProgressBar를 표현하기 위한 메소드이다.
      * 5% 마다 출력된 값을 갱신시키면서, 자연스럽게 ProgressBar가 증가하는 것을 보여주는 것을 나타냄.
      * 완료시, Process Complete출력하게함.
  * public static String getFileDir(path)
    * path에 있는 파일의 디렉토리를 구하기 위한 메소드.
---
* KeyDerivation.java
  * private static byte[] pbkdf1(password, salt, dkLen, iteration)
    * password를 byte배열로 바꾸고, salt와 합쳐서 암호화하는 메소드이다.
      * MessageDigest md객체 선언 후, BouncyCastle에서 제공하는 SHA1알고리즘을 사용한다고 명시.
      * md객체를 password를 byte배열로 바꾸고, salt와 합친 결과물을 사용하여 암호화를 한다.
      * 그 후 나온 result를 리턴함.
  * public static byte[] run(password, salt)
    * password와 salt를 받아와서, private로 선언된 pbkdf1메소드를 실행시키는 연결함수.  
---
* MainClient.java
  * public static void main(String[] args)
    * 프로그램이 구동하는 메인 메소드.
      * 시작시, mode를 입력받음 (enc, dec, both, check)
      * enc, dec, both는 암호화 프로그램을 동작시키게 함.
        * password와 파일경로를 받아서 enc는 암호화, dec는 복호화, both는 암호화와 복호화를 실행시켜준다.
          * dec주의사항: enc를 프로그램 실행해야 dec를 했을 때, 확장자가 제대로 출력됨 (default: exe)
      * check는 파일이 같은 여부를 확인시켜준다.
        * 첫번째 파일과, 두번째 파일의 경로를 입력받은 후, Utils.java에 있는 checkSameFile메소드 실행시킨다.
      * exit실행시 종료된다.
---
* Utils.java (기존에 제공된 파일에 추가된 내용만 서술함)
  * public static byte[] concatByteArray(first, second)
    * first와 second의 byte배열을 합치는 메소드.
  * public static boolean checkSameFile(filepath1, filepath2)
    * 첫번째 경로와 두번째 경로에 있는 파일을 비교해서 같은 프로그램인지 확인한다.
      * MessageDigest md와 md2객체를 생성하고, MD5 알고리즘을 사용한다.
      * 첫번째 파일과 두번째 파일을 같은 알고리즘으로 해시값을 찾아낸다.
      * 해시값을 비교하고, 같으면 true, 다르면 false를 리턴해서 같은 파일 여부 확인.
  * public static int getFileSize(filePath)
    * 파일 사이즈를 구하는 메소드
      * 파일 사이즈를 리턴함.

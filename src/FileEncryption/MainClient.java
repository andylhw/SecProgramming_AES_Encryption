package FileEncryption;

public class MainClient {
    public static void main(String[] args) throws Exception {
        String password = "password";
        String path = System.getProperty("user.dir");
        System.out.println("Working Directory: "+path);

        KeyDerivation.run(password);
    }
}

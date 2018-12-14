import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TMParser {
    private String[] TMDescriptor;

    TMParser(String fileName) {
        /* use a .tm file to initial parser */
        File file = new File(fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.compareTo("") != 0 && !line.startsWith(";")) {
                    System.out.println(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TMParser tmParser = new TMParser("./TM/palindrome_detector.tm");
    }
}

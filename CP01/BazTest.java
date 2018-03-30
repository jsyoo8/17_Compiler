import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class BazTest {
    public Vector<String> bazCode_ = new Vector<String>();

    public BazTest() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("test.baz"));
            String bazText;
            while ((bazText = br.readLine()) != null) {
                bazCode_.add(bazText);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(wrong());
            System.exit(1);
        }
    }

    public int vectorSize(){
        return bazCode_.size();
    }

    public String readLine(int num) {
        return bazCode_.get(num);
    }

    public Vector<String> Tokenize(String bazText) {
        Vector<String> Token = new Vector<String>();
        StringTokenizer st = new StringTokenizer(bazText, " ");
        while (st.hasMoreTokens()) {
            Token.add(st.nextToken());
        }
        return Token;
    }

    public String wrong() {
        return "\"YOU ARE WRONG!\"";
    }
}

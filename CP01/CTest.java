import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class CTest {
    BazTest bazTest_;
    Vector<String> Token_;

    public CTest(BazTest bazTest) {
        bazTest_ = bazTest;
        Token_ = new Vector<String>();
    }

    private String cShow(String x) {
        if (x.equals("true")) {
            return "printf(\"true\");";
        } else if (x.equals("false")) {
            return "printf(\"false\");";
        } else if (x.equals("baz")) {
            return showBaz() + ";";
        } else {
            return "if(" + x + "==TRUE) {printf(\"true\");} \nelse if(" + x + "==FALSE) {printf(\"false\");} \nelse if(" + x + "==BAZ) {" + showBaz() + ";} \nelse {printf(" + bazTest_.wrong() + ");}";
        }
    }

    private String cSet(String leftValue, String rightValue) {
        return "enum bazValue " + leftValue + "=" + rightValue.toUpperCase() + ";";
    }

    //char tempGetValue[30] = "\0"; 선언
    private String cGet(String x) {
        return "scanf_s(\"%s\",tempGetValue, sizeof(tempGetValue)); \nif(!strcmp(tempGetValue,\"true\")) {" + x + "=TRUE;} \nelse if(!strcmp(tempGetValue,\"false\")) {" + x + "=FALSE;} \nelse if(!strcmp(tempGetValue,\"baz\")){" + x + "=BAZ;} \nelse {printf(" + bazTest_.wrong() + ");}";
    }

    private String cIf(String x) {
        return "if(" + x + "==TRUE) { ";
    }

    private String cIfNot(String x) {
        return "if(" + x + "!=TRUE) {";
    }

    private String cEndif() {
        return "}";
    }

    private String cEnd() {
        return "return 1;}";
    }

    //int tempShowValue = 0; 선언
    private String showBaz() {
        return "tempShowValue=rand()%2; if(tempShowValue) {printf(\"false\");} else {printf(\"true\");}";
    }

    //stdio.h stdlib.h string.h time.h
    private String firstWrite() {
        return "#include <stdio.h> \n#include <stdlib.h> \n#include <string.h> \n#include <time.h> \nenum bazValue {TRUE,FALSE,BAZ}; \nint main(void){ \nsrand(time(NULL)); \nchar tempGetValue[30] = \"\\0\"; \nint tempShowValue = 0;";
    }

    public void bazToCCompile() {
        String str = "";
        String str0 = "";
        String str1 = "";
        String str2 = "";
        int i = 0;
        try {
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter("test.c"));
            bw.write(firstWrite());

            while (i < bazTest_.vectorSize()) {
                bw.newLine();
                str = bazTest_.readLine(i);
                Token_ = bazTest_.Tokenize(str);
                str0 = Token_.get(0);
                if (Token_.size() > 1) {
                    str1 = Token_.get(1);
                    if (Token_.size() > 2) {
                        str2 = Token_.get(2);
                    }
                }

                switch (str0) {
                    case "show":
                        bw.write(cShow(str1));
                        break;
                    case "get":
                        bw.write(cGet(str1));
                        break;
                    case "if":
                        bw.write(cIf(str1));
                        break;
                    case "if!":
                        bw.write(cIfNot(str1));
                        break;
                    case "endif":
                        bw.write(cEndif());
                        break;
                    case "end":
                        bw.write(cEnd());
                        break;
                    default:
                        bw.write(cSet(str0, str2));
                        break;
                }
                i++;
            }

            bw.close();
        } catch (IOException e) {
            System.out.println(bazTest_.wrong());
            System.exit(1);
        }
    }
}

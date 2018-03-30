public class TestProject {
    public static void main(String[] args){
        BazTest bazTest = new BazTest();
        CTest cTest = new CTest(bazTest);
        cTest.bazToCCompile();
    }
}

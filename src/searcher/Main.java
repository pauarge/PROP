package searcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String readLine;
        String outputLine;
        try {
            while ((readLine = bufferedReader.readLine()) != null) {
                readLine = readLine.toLowerCase();
                outputLine = controller.executeLine(readLine);
                if (outputLine != null) {
                    System.out.println(outputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

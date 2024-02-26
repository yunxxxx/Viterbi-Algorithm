import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class q1_Yuan_Bo {
    public static void main(String[] args) throws IOException {
        File file = new File("test_1.txt");
        BufferedReader input = new BufferedReader(new FileReader(file));
        String inputText = input.readLine();
        input.readLine();
        String symbol = input.readLine().replaceAll("\\s", "");
        input.readLine();
        String alphabet = input.readLine().replaceAll("\\s", "");
        input.readLine();
        //System.out.println(inputText + '\n' + symbol + '\n' + alphabet);
        String T[][] = new String[alphabet.length()][alphabet.length()];
        input.readLine();
        for (int i = 0; i < alphabet.length(); i++) {
            String newLine = input.readLine();
            String[] lineArray = newLine.split("\\s+");
            for (int j = 1; j < lineArray.length; j++) {
                T[i][j - 1] = lineArray[j];
            }
        }
        input.readLine();
        input.readLine();
        String E[][] = new String[alphabet.length()][symbol.length()];
        for (int i = 0; i < alphabet.length(); i++) {
            String newLine = input.readLine();
            String[] lineArray = newLine.split("\\s+");
            for (int j = 1; j < lineArray.length; j++) {
                E[i][j - 1] = lineArray[j];
            }
        }
        String result = viterbiAlgorithm(inputText, symbol, alphabet, T, E);
        FileWriter writer = new FileWriter("output_q1_Yuan_Bo.txt");
        writer.write(result);
        writer.close();
    }
    public static String viterbiAlgorithm(String inputText, String symbol, String alphabet, String[][] T, String[][] E) {
        String result = "";
        double[][] valueMap = new double[inputText.length()][alphabet.length()];
        char[][] traceMap = new char[inputText.length()][alphabet.length()];
        double initialProb = 1.0 / (alphabet.length());
        for (int i = 0; i < inputText.length(); i++) {
            for (int j = 0; j < alphabet.length(); j++) {
                double currentT = Double.NEGATIVE_INFINITY;
                char currentState = ' ';
                double currentE = 0.0;
                for (int k = 0; k < symbol.length(); k++) {
                    if (inputText.charAt(i) == symbol.charAt(k)) {
                        currentE = Double.parseDouble(E[j][k]);
                    }
                }
                double value;
                if (i == 0) {
                    valueMap[i][j] = Math.log(currentE) + Math.log(initialProb);
                }
                else {
                    for (int x = 0; x < alphabet.length(); x++) {
                        double myv = Double.parseDouble(T[x][j]);
                        value = valueMap[i - 1][x] + Math.log(Double.parseDouble(T[x][j]));
                        if (currentT < value) {
                            currentT = value;
                            currentState = alphabet.charAt(x);
                        }
                    }
                    valueMap[i][j] = currentT + Math.log(currentE);
                    traceMap[i][j] = currentState;
                }
            }
        }
        String bestPath = "";
        double bestValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < alphabet.length(); i++) {
            double currentValue = valueMap[inputText.length() - 1][i];
            char currentChar = alphabet.charAt(i);
            String currentPath = "" + currentChar;
            for (int j = 0; j < inputText.length() - 1; j++) {
                int next = alphabet.indexOf(currentChar);
                currentChar = traceMap[inputText.length() - 1 - j][next];
                double haha = valueMap[inputText.length() - 1 - j][alphabet.indexOf(currentChar)];
                currentValue += valueMap[inputText.length() - 2 - j][alphabet.indexOf(currentChar)];
                currentPath += currentChar;
            }
            if (bestValue < currentValue) {
                bestValue = currentValue;
                bestPath = currentPath;
            }
        }
        result = new StringBuilder(bestPath).reverse().toString();
        System.out.println(result);
        return result;
    }
}

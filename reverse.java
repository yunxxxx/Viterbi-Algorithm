import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class q2_Yuan_Bo {
    public static void main(String[] args) throws IOException {
        File file = new File("test_1.txt");
        BufferedReader input = new BufferedReader(new FileReader(file));
        int iterations = Integer.valueOf(input.readLine());
        input.readLine();
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
        String result = viterbiAlgorithm(iterations, inputText, symbol, alphabet, T, E);
        FileWriter writer = new FileWriter("output_q2_Yuan_Bo.txt");
        writer.write(result);
        writer.close();
    }
    public static String viterbiAlgorithm(int iterations, String inputText, String symbol, String alphabet, String[][] T, String[][] E) {
        String result = "";
        String outputTable = "";
        for (int myPuppy = 0; myPuppy < iterations; myPuppy++) {
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
                    int index = -1;
                    double value = 0.0;
                    if (i == 0) {
                        valueMap[i][j] = Math.log(currentE + 0.00000001) + Math.log(initialProb+ 0.00000001);
                    }
                    else {
                        for (int x = 0; x < alphabet.length(); x++) {
                            value = valueMap[i - 1][x] + Math.log(Double.parseDouble(T[x][j])+ 0.00000001);
                            if (currentT < value) {
                                currentT = value;
                                currentState = alphabet.charAt(x);
                            }
                        }
                        valueMap[i][j] = currentT + Math.log(currentE+ 0.00000001);
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
                    currentValue += valueMap[inputText.length() - 2 - j][alphabet.indexOf(currentChar)];
                    currentPath += currentChar;
                }
                if (bestValue < currentValue) {
                    bestValue = currentValue;
                    bestPath = currentPath;
                }

            }
            for (int i = 0; i < alphabet.length(); i++) {
                if (!bestPath.contains(alphabet.charAt(i) + "")) {
                    return outputTable;
                }
            }
            result = new StringBuilder(bestPath).reverse().toString();
            int[][] xyzTable = new int[symbol.length()][alphabet.length()];
            int[][] alphabetTable = new int[alphabet.length()][alphabet.length()];
            outputTable = "";
            for (int i = 0; i < alphabetTable.length; i++) {
                Arrays.fill(alphabetTable[i], 0);
            }
            for (int i = 0; i < inputText.length() - 1; i++) {
                int alphabetValue1 = alphabet.indexOf(result.charAt(i + 1));
                int alphabetValue2 = alphabet.indexOf(result.charAt(i));
                alphabetTable[alphabetValue1][alphabetValue2]++;
            }
            for (int i = 0; i < alphabet.length() - 1; i++) {
                outputTable += "\t" + alphabet.charAt(i);
            }
            outputTable += "\t" + alphabet.charAt(alphabet.length() - 1) + "\n";
            for (int i = 0; i < alphabet.length(); i++) {
                int sum = 0;
                outputTable += alphabet.charAt(i) + "\t";
                for (int j = 0; j < alphabet.length(); j++) {
                    sum += alphabetTable[j][i];
                }
                for (int j = 0; j < alphabet.length(); j++) {
                    DecimalFormat df = new DecimalFormat("#.###");
                    T[i][j] = df.format((double)alphabetTable[j][i] / sum);
                    outputTable += df.format((double)alphabetTable[j][i] / sum) + "\t";
                }
                outputTable += "\n";
            }

            outputTable += "--------\n";
            for (int i = 0; i < xyzTable.length; i++) {
                Arrays.fill(xyzTable[i], 0);
            }
            for (int i = 0; i < inputText.length(); i++) {
                int symbolValue = symbol.indexOf(inputText.charAt(i));
                int alphabetValue = alphabet.indexOf(result.charAt(i));
                xyzTable[symbolValue][alphabetValue]++;
            }
            for (int i = 0; i < symbol.length() - 1; i++) {
                outputTable += "\t" + symbol.charAt(i);
            }
            outputTable += "\t" + symbol.charAt(symbol.length() - 1) + "\n";
            for (int i = 0; i < alphabet.length(); i++) {
                int sum = 0;
                outputTable += alphabet.charAt(i) + "\t";
                for (int j = 0; j < symbol.length(); j++) {
                    sum += xyzTable[j][i];
                }
                for (int j = 0; j < symbol.length(); j++) {
                    DecimalFormat df = new DecimalFormat("#.###");

                    E[i][j] = df.format((double)xyzTable[j][i] / sum);
                    outputTable += df.format((double)xyzTable[j][i] / sum) + "\t";
                }
                outputTable += "\n";
            }
            boolean stop = true;
            for (int i = 0; i < T.length; i++) {
                for (int j = 0; j < T[i].length; j++) {
                    if (!T[i][j].equals("1") && !T[i][j].equals("0") ) {
                        stop = false;
                    }
                }
            }
            if (stop) {
                myPuppy = 100;
            }
        }
        System.out.println(outputTable);
        return outputTable;
    }
}

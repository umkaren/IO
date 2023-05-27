package IOAssignment;

import java.io.*;
import java.util.*;

public class IntegerScanner {
    String inputFilePath1 = "src/input1.txt";
    String inputFilePath2 = "src/input2.txt";

    String outputFilePath1 = "src/merged.txt"; //path of merged.txt
    String outputFilePath2 = "src/common.txt"; //path of common.txt

    List<Integer> mergeNumbers = new ArrayList<>(); //list to store the merged numbers

    public static void main(String[] args) {
        IntegerScanner scanner = new IntegerScanner();
        scanner.readMergeFiles();
        scanner.findCommonNumbers();
    }

    public void readMergeFiles() {
        try {
            List<Integer> input1 = readIntegersInputFiles(inputFilePath1); //reads numbers from input1
            List<Integer> input2 = readIntegersInputFiles(inputFilePath2); //reads numbers from input2

            mergeNumbers.addAll(input1); //adds numbers from input1 to mergeNumbers list
            mergeNumbers.addAll(input2); //adds numbers from input2 to mergeNumbers list

            writeIntegers(mergeNumbers, outputFilePath1); //write the merged numbers to merged.txt

            System.out.println("Merge successful. File created: " + outputFilePath1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + e.getMessage());
        }
    }

    private List<Integer> readIntegersInputFiles(String readLines) {
        List<Integer> numbers = new ArrayList<>(); //list to store the numbers

        try (BufferedReader reader = new BufferedReader(new FileReader(readLines))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int number = Integer.parseInt(line); //parse the line as an integer
                numbers.add(number); //add the number to the list
            }
        } catch (IOException e) {
            System.out.println("Error with the file: " + e.getMessage()); //handle IO Exception
        }
        return numbers; //return the list of numbers
    }

    public void findCommonNumbers() {
        try {
            List<Integer> numbers1 = readIntegersInputFiles(inputFilePath1); //read numbers from input1
            List<Integer> numbers2 = readIntegersInputFiles(inputFilePath2); //read numbers from input2

            List<Integer> commonNumbers = new ArrayList<>(); //list to store common numbers

            for (Integer number : numbers1) {
                if (numbers2.contains(number) && !commonNumbers.contains(number)) { //if the number is also in input2 and it has not been added to the common numbers list
                    commonNumbers.add(number); //add the common number to the list
                }
            }

            writeIntegers(commonNumbers, outputFilePath2); //write the common numbers to common.txt

            System.out.println("Common numbers found. File created: " + outputFilePath2);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + e.getMessage());
        }
    }

    private void writeIntegers(List<Integer> numbers, String convertNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(convertNumber))) {
            for (Integer number : numbers) {
                writer.write(number.toString()); //writes number as a string
                writer.newLine(); //writes new line
            }
        } catch (IOException e) {
            System.out.println("Error with file: " + e.getMessage());
        }
    }
}

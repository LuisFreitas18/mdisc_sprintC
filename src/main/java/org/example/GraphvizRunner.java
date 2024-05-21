package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphvizRunner {
    public static void graphGenerator(String inputDotFile, String outputPngFile) {
        // Command to convert .dot file to .png using Graphviz
        String command = "dot -Tpng " + inputDotFile + " -o " + outputPngFile;

        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Read any error messages from the command
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Successfully generated " + outputPngFile);
            } else {
                System.out.println("Failed to generate " + outputPngFile);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

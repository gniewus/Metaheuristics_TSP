package com.company;

import org.nlogo.headless.HeadlessWorkspace;

import java.awt.font.NumericShaper;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class NetLogoHeadless {

    private static final HeadlessWorkspace WORKSPACE = HeadlessWorkspace.newInstance();

    public static void main(String[] argv) {
        try {
            WORKSPACE.open(//"./Classic Traveling Salesman_2018_kommentiert.nlogo");
                    "src/models/Classic Traveling Salesman_2018_kommentiert.nlogo");
            WORKSPACE.command("tsp2018Map");

            executeAllParameterCombinations();

            /*int[] array = new int[100];
            Arrays.setAll(array, i -> i + 1);
            array = Arrays.stream(array).map(i -> i * 100).toArray();
            System.out.println(array);
            System.out.println("popSize;numOfCycles;crossover;tournament;mutation;counter;best;av;min;bestFitness;terminated;bestResult;");

            long startTime = System.currentTimeMillis();
            for(Integer pop : array) {
                execute(pop,2,1,2,100);
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = (stopTime - startTime)/1000;

            System.out.println(elapsedTime+" Sec.");*/

            WORKSPACE.dispose();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private static void executeAllParameterCombinations() throws InterruptedException {
        // build param arrays
        int[] populationSizes = createParamArray(1, 1000, 2);
        int[] tournamentSizes = createParamArray(2, 10, 2);
        int[] crossoverRates = createParamArray(1, 100, 2);
        int[] mutationsRates = createParamArray(0, 30, 2);
        int[] numbersOfCycles = createParamArray(1, 1000, 2);

        System.out.println("popSize;tournament;crossover;mutation;numOfCycles;counter;best;av;min;bestFitness;terminated;bestResult;");
        long startTime = System.currentTimeMillis();

        //execute all combinations
        for (int populationSize : populationSizes) {
            for (int tournamentSize : tournamentSizes) {
                for (int crossoverRate : crossoverRates) {
                    for (int mutationRate : mutationsRates) {
                        for (int numberOfCycles : numbersOfCycles) {
                            execute(populationSize, tournamentSize, crossoverRate, mutationRate, numberOfCycles);
                        }
                    }
                }
            }
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(elapsedTime / 1000 + " Sec.");
    }

    private static void execute(int popSize, int tournament, int crossover, double mutation, int numOfCycles) throws InterruptedException {

        WORKSPACE.command("set population-size " + popSize);
        WORKSPACE.command("set number-of-cycles " + numOfCycles);
        WORKSPACE.command("set crossover-rate " + crossover);
        WORKSPACE.command("set tournament-size " + tournament);
        WORKSPACE.command("set mutation-rate " + mutation);
        WORKSPACE.command("setup");

        String params = popSize + " ; " + tournament + " ; " + crossover + " ; " + mutation + " ; " + numOfCycles;
        String av, best, worst, bestResult, bestFitness = "";

        // I commented out everything with repeatStep, because I didn't understood it.
        //int repeat_step = numOfCycles/10; // Each 10 ticks check if its still working
        int counter = 0;
        Boolean terminated = false;
        String final_row = "";
        String last_row = "";
        //while (counter < repeat_step && !terminated) {
        while (counter < numOfCycles && !terminated) {
            av = String.valueOf(WORKSPACE.report("get-avg"));
            best = String.valueOf(WORKSPACE.report("get-best"));
            worst = String.valueOf(WORKSPACE.report("get-worst"));
            bestResult = String.valueOf(WORKSPACE.report("get-best-result"));
            bestFitness = String.valueOf(WORKSPACE.report("get-best-fitness"));
            //WORKSPACE.command("repeat " + repeat_step + " [go]");
            WORKSPACE.command("repeat " + numOfCycles + " [go]");

            if (av.equals("0.0")) {
                terminated = true;
                final_row = counter + ";" + best + ";" + av + ";" + worst +";" + bestFitness + ";" + terminated + ";" + bestResult;
                System.out.println(params + final_row);
            } else {
                last_row = counter + ";" + best + ";" + av + ";" + worst +";" + bestFitness + ";" + terminated + ";" + bestResult;
                counter++;
            }
        }

        if (!terminated) {
            System.out.println(params + last_row);
        }

        //

    }

    private static int[] createParamArray(int min, int max, int nrOfParams) {
        if (nrOfParams <= 1) {
            return new int[]{min};
        }

        int[] result = new int[nrOfParams];

        double stepSize = (double) (max-min) / (nrOfParams-1);

        double current = min;
        for (int i = 0; i < nrOfParams; i++) {
            result[i] = (int) Math.round(current);
            current += stepSize;
        }

        return result;
    }

}
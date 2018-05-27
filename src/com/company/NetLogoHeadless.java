package com.company;

import org.nlogo.headless.HeadlessWorkspace;

import java.io.IOException;

public class NetLogoHeadless {

    private static final HeadlessWorkspace WORKSPACE = HeadlessWorkspace.newInstance();

    public static void main(String[] argv) {
        try {
            WORKSPACE.open(//"./Classic Traveling Salesman_2018_kommentiert.nlogo");
                    "src/models/Classic Traveling Salesman_2018_kommentiert.nlogo");

            executeAllParameterCombinations();

            WORKSPACE.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeAllParameterCombinations() throws InterruptedException, IOException {
        // build param arrays
        int[] populationSizes = createParamArray(3, 1000, 3);
        int[] tournamentSizes = createParamArray(2, 10, 3);
        int[] mutationsRates = createParamArray(1, 30, 3);
        int[] crossoverRates = createParamArray(0, 100, 3);
        int[] numbersOfCycles = createParamArray(8, 1000, 6);

        System.out.println("popSize ; tournament ; mutation ; crossover ; numOfCycles ; "
                + "duration ; best ; av ; worst ; bestFitness ; bestResult ;");

        long startTime = System.currentTimeMillis();

        //execute all combinations
        for (int populationSize : populationSizes) {
            for (int tournamentSize : tournamentSizes) {
                    if (populationSize >= tournamentSize) {
                    for (int mutationRate : mutationsRates) {
                        for (int crossoverRate : crossoverRates) {
                            execute(populationSize, tournamentSize, mutationRate, crossoverRate, numbersOfCycles);
                        }
                    }
                }
            }
        }
        System.out.println("Gesamtdauer: " + (System.currentTimeMillis()-startTime) / 1000 + " Sec.");
    }

    private static void execute(int popSize, int tournament, double mutation, int crossover, int[] numbersOfCycles) throws InterruptedException {

        WORKSPACE.command("set population-size " + popSize);
        WORKSPACE.command("set tournament-size " + tournament);
        WORKSPACE.command("set mutation-rate " + mutation);
        WORKSPACE.command("set crossover-rate " + crossover);
        WORKSPACE.command("set number-of-cycles " + numbersOfCycles[numbersOfCycles.length-1]);
        WORKSPACE.command("tsp2018Map");
        WORKSPACE.command("setup");

        for (int i = 0; i < numbersOfCycles.length; i++) {
            String params = popSize + " ; " + tournament + " ; " + mutation + " ; " + crossover + " ; " + numbersOfCycles[i] + " ; ";
            String av, best, worst, bestResult, bestFitness = "";

            long startTime = System.currentTimeMillis();
            WORKSPACE.command("repeat " + (numbersOfCycles[i] - (i > 0 ? numbersOfCycles[i-1] : 0)) + " [go]");

            long duration = System.currentTimeMillis() - startTime;
            best = String.valueOf(WORKSPACE.report("get-best"));
            av = String.valueOf(WORKSPACE.report("get-avg"));
            worst = String.valueOf(WORKSPACE.report("get-worst"));
            bestFitness = String.valueOf(WORKSPACE.report("get-best-fitness"));
            bestResult = String.valueOf(WORKSPACE.report("get-best-result"));

            String row = duration + " ; " + best + " ; " + av + " ; " + worst + " ; " + bestFitness + " ; " + bestResult;

            System.out.println(params + row);
        }

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
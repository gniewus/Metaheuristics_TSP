package com.company;

import org.nlogo.headless.HeadlessWorkspace;

import java.io.IOException;
import java.util.HashMap;

public class NetLogoHeadless {

    private static final HeadlessWorkspace WORKSPACE = HeadlessWorkspace.newInstance();
    private static final HashMap<String, String> cityMap = new HashMap<>();

    public static void main(String[] argv) {
        setupCityMap();
        try {
            WORKSPACE.open("./Classic Traveling Salesman_2018_kommentiert.nlogo");

            executeAllParameterCombinations();

            WORKSPACE.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeAllParameterCombinations() throws InterruptedException, IOException {
        // FESTE PARAMTER
        int[] populationSizes = {350}; //650,100};
        int[] tournamentSizes = {2};    //
        int[] mutationsRates = {16};
        int[] crossoverRates = {100};  // ,50};
        int[] numbersOfCycles = {780};//,890};

        int[] numberOfElites = {1,4,7,10};//createParamArray(10, 1000, 10);

        String[] swapMutations = {
        //                            "false",
                                    "true"
        };
        String[] preserveCommonLinks = {
          //                              "false",
                                        "true"
                                        };
        String[] onePointCrossover = {
                                        "false",
                                        "true"
                                        };
        String[] environmentalSelection = {
                                  "false",
                                        "true"
                                        };
        String[] rouletteWheelSelection = {
                                        "false",
                                        "true"
                                        };




        System.out.println("id ; onePointCrossover ; environmentalSelection ; rouletteWheelSelection ; numberOfElites ; popSize ; tournament ; mutation ; crossover ; preserveCommonLink ; swapMutation ; numOfCycles ; "
                + "duration ; best ; av ; worst ; bestFitness ; bestResult");

        long startTime = System.currentTimeMillis();
        int idCounter = 0;

        //execute all combinations
        for (int populationSize : populationSizes) {
            for (int tournamentSize : tournamentSizes) {
                    if (populationSize >= tournamentSize) {
                    for (int mutationRate : mutationsRates) {
                        for (int crossoverRate : crossoverRates) {
                            for(String preserveCommonLink: preserveCommonLinks){
                                for(String swapMutation: swapMutations){
                                    for(String onePointC: onePointCrossover){
                                        for (String envSelection:environmentalSelection){
                                            for(String rouletteWheel: rouletteWheelSelection){
                                                for (int numOfElites: numberOfElites){
                                                    execute(idCounter,onePointC,envSelection,rouletteWheel,numOfElites,swapMutation, preserveCommonLink, populationSize, tournamentSize, mutationRate, crossoverRate, numbersOfCycles);
                                                    idCounter += numbersOfCycles.length;
                                                }
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Gesamtdauer: " + (System.currentTimeMillis()-startTime) / 1000 + " Sec.");
    }

    private static void execute(int id,String onePointCrossover, String envSelection ,String rouletteWheel ,int numOfElites,String swapMutations, String preserveCommonLinks, int popSize, int tournament, double mutation, int crossover, int[] numbersOfCycles) throws InterruptedException {


        //New Params
        WORKSPACE.command("set one-point-crossover? " + onePointCrossover);
        WORKSPACE.command("set environmental-selection? " + envSelection);
        WORKSPACE.command("set use-roulette-wheel-selection? " + rouletteWheel);
        WORKSPACE.command("set population-size " + numOfElites);


        WORKSPACE.command("set swap-mutation? " + swapMutations);
        WORKSPACE.command("set preserve-common-links? " + preserveCommonLinks);

        WORKSPACE.command("set population-size " + popSize);
        WORKSPACE.command("set tournament-size " + tournament);
        WORKSPACE.command("set mutation-rate " + mutation);
        WORKSPACE.command("set crossover-rate " + crossover);
        WORKSPACE.command("set number-of-cycles " + numbersOfCycles[numbersOfCycles.length-1]);
        WORKSPACE.command("tsp2018Map");
        WORKSPACE.command("setup");

        double lastBestFitness = 1000.0;
        for (int i = 0; i < numbersOfCycles.length; i++) {
            String params = (id+i) + " ; " + popSize + " ; " + tournament + " ; " + mutation + " ; " + crossover + " ; " + preserveCommonLinks + " ; " + swapMutations + " ; " + numbersOfCycles[i] + " ; " ;
            String av, best, worst, bestResult = "";
            double bestFitness;

            long startTime = System.currentTimeMillis();
            WORKSPACE.command("repeat " + (numbersOfCycles[i] - (i > 0 ? numbersOfCycles[i-1] : 0)) + " [go]");

            long duration = System.currentTimeMillis() - startTime;
            best = String.valueOf(WORKSPACE.report("get-best"));
            av = String.valueOf(WORKSPACE.report("get-avg"));
            worst = String.valueOf(WORKSPACE.report("get-worst"));
            bestFitness = Double.parseDouble(String.valueOf(WORKSPACE.report("get-best-fitness")));
            double improvement = lastBestFitness / bestFitness;
            lastBestFitness = bestFitness;
            bestResult = replaceNumbersWithCityNames(String.valueOf(WORKSPACE.report("get-best-result")));

            String row = duration + " ; " + best + " ; " + av + " ; " + worst + " ; " + bestFitness + " ; " + improvement + " ; " + bestResult;

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

    private static String replaceNumbersWithCityNames(String sequence) {
        String result = "";
        sequence = sequence.substring(1, sequence.length()-1).replaceAll(",","");
        String[] numbers = sequence.split("\\s+");
        for (String number : numbers) {
            result += cityMap.get(number) + ", ";
        }
        return "[" + result.substring(0, result.length()-2) + "]";
    }

    private static void setupCityMap() {
        cityMap.put("0.0", "Berlin");
        cityMap.put("1.0", "Prag");
        cityMap.put("2.0", "Wien");
        cityMap.put("3.0", "Budapest");
        cityMap.put("4.0", "Warschau");
        cityMap.put("5.0", "Riga");
        cityMap.put("6.0", "Vilnius");
        cityMap.put("7.0", "Mins");
        cityMap.put("8.0", "Kiew");
        cityMap.put("9.0", "Moskau");
        cityMap.put("10.0", "Jekaterinenburg");
        cityMap.put("11.0", "Rostow");
        cityMap.put("12.0", "Saransk");
        cityMap.put("13.0", "Sotschi");
        cityMap.put("14.0", "StPetersburg");
        cityMap.put("15.0", "Wolgograd");
        cityMap.put("16.0", "Samara");
        cityMap.put("17.0", "Nowgorod");
        cityMap.put("18.0", "Kasan");
        cityMap.put("19.0", "Kaliningrad");
        cityMap.put("20.0", "Kischinau");
    }

}
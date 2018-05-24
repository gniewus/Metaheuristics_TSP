package com.company;

import org.nlogo.headless.HeadlessWorkspace;

import java.awt.font.NumericShaper;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class NetLogoHeadless {
    public static void main(String[] argv) {
        HeadlessWorkspace workspace =
                HeadlessWorkspace.newInstance() ;
        try {
            workspace.open(
                    "./Classic Traveling Salesman_2018_kommentiert.nlogo");
            workspace.command("tsp2018Map");
            int[] array = new int[100];
            Arrays.setAll(array, i -> i + 1);
            array = Arrays.stream(array).map(i -> i * 100).toArray();
            System.out.println(array);
            System.out.println("popSize;numOfCycles;crossover;tournament;mutation;counter;best;av;min;bestFitness;terminated;bestResult;");
            long startTime = System.currentTimeMillis();
            for(Integer pop : array)
            {
                execute(workspace,pop,100,1.0,2,1);


            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = (stopTime - startTime)/1000;

            System.out.println(elapsedTime+" Sec.");

            workspace.dispose();
        }
        catch(Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    private static void execute(HeadlessWorkspace workspace, int popSize, int numOfCycles, double crossover, int tournament , double mutation) throws InterruptedException {

        workspace.command("set population-size "+popSize);
        workspace.command("set number-of-cycles "+numOfCycles);
        workspace.command("set crossover-rate "+crossover);
        workspace.command("set tournament-size "+tournament);
        workspace.command("set mutation-rate "+mutation);
        workspace.command("setup");

        String params = popSize+" ; "+numOfCycles+" ; "+crossover+" ; "+tournament+" ; "+mutation;
        String av = "";
        String best = "";
        String min = "";
        String bestResult = "";
        String bestFitness = "";


        int number_of_cycles = numOfCycles;
        int repeat_step = number_of_cycles/10; // Each 10 ticks check if its still working
        int counter=0;
        Boolean terminated = false;
        String final_row = "";
        String last_row = "";
        while (counter < repeat_step && !terminated) {
            av = String.valueOf(workspace.report("get-avg"));
            best = String.valueOf(workspace.report("get-best"));
            min = String.valueOf(workspace.report("get-worst"));
            bestResult = String.valueOf(workspace.report("get-best-result"));
            bestFitness = String.valueOf(workspace.report("get-best-fitness"));
            workspace.command("repeat "+repeat_step+" [go]");

            if(!String.valueOf(workspace.report("get-avg")).equals("0.0")){
                last_row = counter+";"+best + ";" + av + ";" + min +";" + bestFitness + ";"+ terminated+";"+ bestResult;

                counter++;
            } else {
                terminated = true;
                final_row = counter+";"+best + ";" + av + ";" + min +";" + bestFitness + ";"+ terminated +";"+bestResult;
                System.out.println(params+final_row);
            }
        }
        if(!terminated){
            System.out.println(params+last_row);
        }


        //

    }


}
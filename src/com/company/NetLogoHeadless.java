package com.company;

import org.nlogo.headless.HeadlessWorkspace;
public class NetLogoHeadless {
    public static void main(String[] argv) {
        HeadlessWorkspace workspace =
                HeadlessWorkspace.newInstance() ;
        try {
            workspace.open(
                    "./Classic Traveling Salesman_2018_kommentiert.nlogo");
            workspace.command("tsp2018Map");

            workspace.command("set population-size 100");
            workspace.command("set number-of-cycles 100");
            workspace.command("set crossover-rate 50.0");
            workspace.command("set tournament-size 15");
            workspace.command("set mutation-rate 15.0");
            workspace.command("setup");
            //workspace.command("repeat 9 [ go ]") ;

            String av = "";
            String best = "499";
            String min = "";
            String bestResult = "";
            String bestFitness = "";

            long startTime = System.currentTimeMillis();

            int counter=0;
            while (counter < 100 ) {
                av = String.valueOf(workspace.report("get-avg"));
                best = String.valueOf(workspace.report("get-best"));
                min = String.valueOf(workspace.report("get-worst"));
                bestResult = String.valueOf(workspace.report("get-best-result"));
                bestFitness = String.valueOf(workspace.report("get-best-fitness"));
                //System.out.println(counter+";"+best + ";" + av + ";" + min +";" + bestFitness + ";"+  bestResult);
                workspace.command("go");
                counter++;
                // workspace.command("update_stats");

            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            System.out.println(elapsedTime/(1000)+" Sec.");
            workspace.dispose();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
package com.company;

import org.nlogo.app.App;

public class NetLogoBenchmark {
    public static void main(String[] argv) {
        App.main(argv);
        try {
            java.awt.EventQueue.invokeAndWait(
                    new Runnable() {
                        public void run() {
                            try {
                                App.app().open(
                                        "./Classic Traveling Salesman_2018_kommentiert.nlogo");
                            } catch (java.io.IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

            App.app().command("tsp2018Map");
            App.app().command("set population-size 100");
            App.app().command("set number-of-cycles 100");
            App.app().command("set tournament-size 15");
            App.app().command("set crossover-rate 50.0");
            App.app().command("set mutation-rate 15.0");

            App.app().command("setup");
            App.app().command("go");
            //App.app().command("calculate-distance");
            //App.app().command("calculate-fitness");
            //App.app().command("update_stats");

            String av = "";
            String best = "499";
            String min = "";
            String bestResult = "";
            String bestFitness = "";

            int counter=0;
            while (counter < 500 ) {
                av = String.valueOf(App.app().report("get-avg"));
                best = String.valueOf(App.app().report("get-best"));
                min = String.valueOf(App.app().report("get-worst"));
                bestResult = String.valueOf(App.app().report("get-best-result"));
                bestFitness = String.valueOf(App.app().report("get-best-fitness"));
                System.out.println(best + " " + av + " " + min +" " + bestResult + " "+ bestFitness);
                App.app().command("go");
                counter++;
                // App.app().command("update_stats");

            }


            System.out.println("Ende");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
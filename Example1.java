import org.nlogo.app.App;

public class Example1 {
    public static void main(String[] argv) {
        App.main(argv);
        try {
            java.awt.EventQueue.invokeAndWait(
                    new Runnable() {
                        public void run() {
                            try {
                                App.app().open(
                                        "./Classic Traveling Salesman_2018_unkommentiert.nlogo");
                            } catch (java.io.IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

            App.app().command("tsp2018Map");
            App.app().command("set population-size 100");
            App.app().command("set number-of-cycles 100");

            App.app().command("setup");
            App.app().command("go");
            //App.app().command("calculate-distance");
            //App.app().command("calculate-fitness");
            //App.app().command("update_stats");

            String av = "";
            String max = "";
            String min = "";

            while (av == "") {
                av = String.valueOf(App.app().report("get-avg"));
                max = String.valueOf(App.app().report("get-best"));
                min = String.valueOf(App.app().report("get-worst"));
                System.out.println(max + " " + av + " " + min);
                App.app().command("repeat 50 [go]");
                // App.app().command("update_stats");
            }
            System.out.println("Ende");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
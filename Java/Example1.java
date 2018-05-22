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
                                        "./Classic Traveling Salesman_2018_kommentiert.nlogo");
                            }
                            catch(java.io.IOException ex) {
                                ex.printStackTrace();
                            }}});

           App.app().command("tsp2018Map");
            App.app().command("set population-size 100");
            App.app().command("setup");
            App.app().command("go");
            //App.app().command("calculate-fitness");

           // String msg = String.valueOf( App.app().report("min-fitness"));
          //  System.out.println(msg);
           // App.app().command("update_stats");

        }

        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
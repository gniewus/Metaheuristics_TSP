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
           // workspace.command("set number-of-cycles 10");
            workspace.command("setup");
            workspace.command("repeat 10 [ go ]") ;
            workspace.dispose();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
package com.dhbw.thesim.impexp;

import java.io.*;
public class JsonHandler {
    static String workingDirectory;

    //set the String workingDirectory based on the operating systems appdata specific folder and create a folder "TheSim" if needed
    private static void setDirectory() {
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN"))//Windows
        {
            workingDirectory = System.getenv("AppData");
            workingDirectory += "/TheSim";
        }
        else if(OS.contains("LINUX"))//Linux
        {
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/.local/share/TheSim";
        }else{//macOS
            workingDirectory = ".";
        }
        File file = new File(workingDirectory);
        if (file.mkdirs()) {
            System.out.println("Directory 'TheSim' was created successfully");
        }
        else {
            System.out.println("Directory 'TheSim' could not be created");
        }
    }
}

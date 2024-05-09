package com.example.ce316project;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;



public class Configuration {
    private String title;
    private String[] compiler;
    private String executable; // Will be empty if source file is compiled to an executable.
    private String mainFile; // File name to be executed
    private String[] arguments;
    private int timeOut = 10;

    public Configuration(String line) {

    }

    // TODO
    public void runConfiguration() throws IOException {
        /**
         * Çalıştırılacak komutları gir (dosyadan ve "project"ten al)
         * Çalıştırma konumunu belirle
         * Çalıştır
         * Programın çıktısını karşılaştır.
         */
        /*
        ProcessBuilder pb = new ProcessBuilder("cmd", "/C", "echo", "Hello!");
        pb.directory(new File("temp"));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null) System.out.println(s); // Programın çıktısı "Hello world!", hata vs.
        */
    }
}

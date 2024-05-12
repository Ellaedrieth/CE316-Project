package com.example.ce316project;
import java.io.File;
import java.util.HashSet;


public class FileScanner{
    public HashSet<String> scanConfigFiles(String directoryPath) throws IllegalAccessException {
        HashSet<String> configFilesList = new HashSet<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalAccessException("Directory does not exist or it is not a directory!");
        }
        File[] files = directory.listFiles();

        if (files == null) {
            throw new IllegalArgumentException("No files found in the specified directory.");
        }

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".dat")) {
                configFilesList.add(file.getAbsolutePath());
            }
        }
        return configFilesList;
    }

    public HashSet<String> scanProjectFiles(String directoryPath) throws IllegalAccessException {
        HashSet<String> projectFilesList = new HashSet<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalAccessException("Directory does not exist or it is not a directory!");
        }
        File[] files = directory.listFiles();

        if (files == null) {
            throw new IllegalArgumentException("No files found in the specified directory.");
        }

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                projectFilesList.add(file.getAbsolutePath());
            }
        }
        return projectFilesList;
    }
}


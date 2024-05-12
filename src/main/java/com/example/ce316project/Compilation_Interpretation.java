package com.example.ce316project;
import javafx.scene.control.Alert;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class Compilation_Interpretation {

    static String selectedCompilerParam = "";
    static String selectedExecParam = "";
    static int size = 0;

    public static final String Existing_Project_File_Path = "src/main/resources/com/example/ce316project/";
    //to extract files in the zip file
    public static ArrayList<String> zipFileExtraction(){
        MainController mainController = new MainController();
        //to keep the values that are read (entryName; related with the language, compiled language)
        ArrayList<String> compileFileSet = new ArrayList<>();
        //to access the content of the submission ZipFile
        try(ZipFile zipFile = new ZipFile(MainController.getZipFilePathItem(MainController.takenValue))){
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (!zipEntry.isDirectory()) {
                    String entryName = zipEntry.getName(); //a.java
                    String extension = entryName.substring(entryName.lastIndexOf(".") + 1);
                    mainController.getConfigExtensionItem();
                    if (extension.equalsIgnoreCase(mainController.getConfigExtensionItem())) {
                        int datIndex = entryName.indexOf('.');
                        String nameFile = entryName.substring(0, datIndex);
                        compileFileSet.add(nameFile);
                    }
                }
            }

        }
        catch (IOException e){
            System.err.println("Error while reading zip file: " + e.getMessage());
            Alert validationError = new Alert(Alert.AlertType.WARNING);
            validationError.setTitle("Invalid Extension Name");
            validationError.setHeaderText("Please enter the valid extension.");
            validationError.showAndWait();
        }
        return compileFileSet;
    }


private static void extractCommand() throws IOException, InterruptedException {
    String directoryPath = MainController.getDirectoryPathWithoutZip();
    String zipFileName = MainController.getOnlyZipName();

    // 1. Process Builder for cd command
    ProcessBuilder cdProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "cd", directoryPath);
    cdProcessBuilder.redirectErrorStream(true);
    Process cdProcess = cdProcessBuilder.start();
    cdProcess.waitFor(); // Wait to complete the cd command.

    // 2. Process Builder for mkdir command
    ProcessBuilder mkdirProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "mkdir", "extractedFiles");
    mkdirProcessBuilder.directory(new File(directoryPath));
    mkdirProcessBuilder.redirectErrorStream(true);
    Process mkdirProcess = mkdirProcessBuilder.start();
    mkdirProcess.waitFor(); // Wait to complete the mkdir command.

    unZipFiles(directoryPath + "\\"+zipFileName, MainController.getDirectoryPathWithoutZip()+"\\extractedFiles");

    Alert validationError = new Alert(Alert.AlertType.INFORMATION);
    validationError.setTitle("The Extraction Message");
    validationError.setHeaderText("The extraction of the zip file is successfully completed.");
    validationError.showAndWait();
}


    public static void unZipFiles(String zipFilePath, String zipDestinationDirectory) {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File file = new File(zipDestinationDirectory + File.separator + fileName);
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    new File(file.getParent()).mkdirs();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        zipEntry = zipInputStream.getNextEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static File compile(ArrayList<String> compSet, ArrayList<String> execSet) {
        String outputFilePath = MainController.getDirectoryPathWithoutZip() + "\\project_result_output.txt";
        File outputFile = new File(outputFilePath);
        try {
            if (outputFile.createNewFile()) {
                System.out.println("New file is created: " + outputFile.getAbsolutePath());
            } else {
                System.out.println("The file is already existed: " + outputFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("The creating file error: " + e.getMessage());
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            extractCommand();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        size = execSet.size();
        try{
            if (!compSet.isEmpty()) {
                int i = 0;
                int j = 0;
                while (i < compSet.size() && j < execSet.size()) {
                    String compCommand = compSet.get(i);
                    String execCommand = execSet.get(j);
                        File file = new File(MainController.getDirectoryPathWithoutZip()+"\\extractedFiles");
                        Process compileProcess = Runtime.getRuntime().exec(compCommand, null,file);
                        compileProcess.waitFor();
                        Process executeProcess = Runtime.getRuntime().exec(execCommand, null,file);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(executeProcess.getInputStream()));
                        String line;
                        long startTime = System.currentTimeMillis();
                        long timeout = 3000; // 3 saniye
                        while ((line = reader.readLine()) != null) {
                            writer.append(Compilation_Interpretation.zipFileExtraction().get(i)+ ": ").append(line).append("\n"); // Print to the file
                            writer.flush();
                        }
                        executeProcess.waitFor();
                        i++;
                        j++;
                    }
                writer.close();
            }
            else{
                for (int i=0; i<execSet.size();i++) {
                    String execCommand = execSet.get(i);
                    File file = new File(MainController.getDirectoryPathWithoutZip()+"\\extractedFiles");
                    Process executeProcess = Runtime.getRuntime().exec(execCommand, null,file);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(executeProcess.getInputStream()));
                    String line;
                    long startTime = System.currentTimeMillis();
                    long timeout = 3000; // 3 saniye
                    while ((line = reader.readLine()) != null) {
                        writer.append(Compilation_Interpretation.zipFileExtraction().get(i)+ ": ").append(line).append("\n"); // Print to the file
                        writer.flush();
                    }
                    executeProcess.waitFor();
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return outputFile;
    }

    public static ArrayList<String> compilationFileReg(ArrayList<String> compileSet){
        ArrayList<String> compSet = new ArrayList<>();
        //javac fileName.java
        String compilerParam = getCompilerParameter();
        for(String fileName : compileSet){
            String newLine = compilerParam.replace("fileName", fileName);
            compSet.add(newLine);
        }
        return compSet;
    }
    public static ArrayList<String> executionFileReg(ArrayList<String> executionSet){
        ArrayList<String> execSet = new ArrayList<>();
        String execParam = getExecParameter();
        for(String fileName : executionSet){
            String newLine = execParam.replace("fileName", fileName);
            execSet.add(newLine);
        }
        return execSet;
    }
    public static String getCompilerParameter(){
        String selectedConfigName = "";
        //to represent the current file path
        File file = new File(MainController.getProjectPath(MainController.takenValue));
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(MainController.getProjectPath(MainController.takenValue)))) {
                String line;
                while((line = reader.readLine()) != null){
                    if(line.startsWith("Configuration Name:")){
                        selectedConfigName = line.substring(line.indexOf(":") +2);
                        BufferedReader readerComp = new BufferedReader(new FileReader(Existing_Project_File_Path+selectedConfigName+".dat"));
                        String lineComp ;
                        try{
                            while ((lineComp = readerComp.readLine()) != null){
                                if(lineComp.startsWith("Compiler Parameters:")){
                                    selectedCompilerParam = lineComp.substring(lineComp.indexOf(":") + 2);
                                    if(!selectedCompilerParam.isEmpty()){
                                        break;
                                    }
                                }
                            }
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return selectedCompilerParam;
    }
    public static String getExecParameter(){
        String selectedConfigName = "";
        //to represent the current file path
        File file = new File(MainController.getProjectPath(MainController.takenValue));
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(MainController.getProjectPath(MainController.takenValue)))) {
                String line;
                while((line = reader.readLine()) != null){
                    if(line.startsWith("Configuration Name:")){
                        selectedConfigName = line.substring(line.indexOf(":") +2);
                        BufferedReader readerComp = new BufferedReader(new FileReader(Existing_Project_File_Path+selectedConfigName+".dat"));
                        String lineComp ;
                        try{
                            while ((lineComp = readerComp.readLine()) != null){
                                if(lineComp.startsWith("Execution Parameter:")){
                                    selectedExecParam = lineComp.substring(lineComp.indexOf(":") + 2);
                                    if(!selectedExecParam.isEmpty()){
                                        break;
                                    }
                                }
                            }
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return selectedExecParam;
    }


}

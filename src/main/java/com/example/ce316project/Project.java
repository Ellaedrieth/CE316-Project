package com.example.ce316project;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipEntry;

public class Project {

    private String title;
    private ArrayList<ZipEntry> submissionZipFiles;
    private Configuration config;
    private File expectedOutputFile;

    public Project(String title, ArrayList<ZipEntry> submissionZipFiles, Configuration config, File expectedOutputFile) {
        setTitle(title);
        setSubmissionZipFiles(submissionZipFiles);
        setConfig(config);
        setExpectedOutputFile(expectedOutputFile);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ZipEntry> getSubmissionZipFiles() {
        return submissionZipFiles;
    }

    public void setSubmissionZipFiles(ArrayList<ZipEntry> submissionZipFiles) {
        this.submissionZipFiles = submissionZipFiles;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public File getExpectedOutputFile() {
        return expectedOutputFile;
    }

    public void setExpectedOutputFile(File expectedOutputFile) {
        this.expectedOutputFile = expectedOutputFile;
    }
}

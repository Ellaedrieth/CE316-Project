package com.example.ce316project;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipEntry;

public class Handler  {

    public Project CreateProject(String title, ArrayList<ZipEntry> submissionZipFiles, Configuration config, File expectedOutputFile){
        return new Project(title,submissionZipFiles,config,expectedOutputFile);
    }

}

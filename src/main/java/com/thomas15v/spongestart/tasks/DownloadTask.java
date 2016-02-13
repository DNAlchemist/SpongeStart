package com.thomas15v.spongestart.tasks;

import com.thomas15v.spongestart.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class DownloadTask extends DefaultTask {

    private File location;
    private URL url;
    private static File cacheDir;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public File getLocation() {
        return location;
    }

    public static void setCacheDir(File cache) {
        //no messy messy, its already messy enough this way
        if (DownloadTask.cacheDir == null)
            DownloadTask.cacheDir = cache;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    @TaskAction
    public void doStuff(){
        getLogger().lifecycle("Downloading: " + getUrl());
        try {
            URLConnection connection = url.openConnection();
            File cacheLocation = new File(cacheDir, Util.getFileName(getUrl()));
            if (cacheLocation.exists() && cacheLocation.length() == connection.getContentLength()) {
                getLogger().lifecycle("Done! (cached)");
            }else{
                IOUtils.copy(connection.getInputStream() , FileUtils.openOutputStream(cacheLocation));
                getLogger().lifecycle("Done!");
            }
            FileUtils.copyFile(cacheLocation, location);
        } catch (IOException e) {
            throw new GradleException("Failed to download: " + url + " : " + e.getMessage());
        }
    }
}
package com.kafkafilter.filter;


import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters only .groovy files
 * @author Zongyang Li
 */
public class GroovyFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.endsWith(".groovy");
    }
}
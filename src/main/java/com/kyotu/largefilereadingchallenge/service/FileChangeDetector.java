package com.kyotu.largefilereadingchallenge.service;

import org.springframework.stereotype.Component;

@Component
public class FileChangeDetector {
    public boolean detect(final long fileSizeBefore, final long fileSizeAfter) {
        //This is very naive file change detector. It can be replaced with something more sophisticated like
        // monitoring of metadata of the file, checking version of file etc or just lock the file during calculation.
        return fileSizeAfter != fileSizeBefore;
    }
}
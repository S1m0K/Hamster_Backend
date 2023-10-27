package com.example.hamster_backend.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteFile {
    /**
     * Creates new File and will create a parent folder if not existing
     * It also writes the given program into the file
     *
     * @param path
     */
    public static File createNewFile(String path) {
        try {
            File file = new File(path);
            new File(path.substring(0, path.lastIndexOf("/"))).mkdirs();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean writeTextToFile(File file, String program) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(program.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }
}

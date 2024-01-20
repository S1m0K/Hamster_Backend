package at.ac.htlinn.hamsterbackend.run;

import com.sun.tools.javac.Main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Compiler {

    public static String getCompilingDirPath(String pathString, String userDir) {
        Path path = Paths.get(pathString, userDir);
        return path.toString();
    }

    public static boolean
    createCompilingDir(String path) {
        File f = new File(path);
        if (f.exists()) {
            if (!f.delete()) {
                return false;
            }
        }
        return f.mkdir();
    }

    public static boolean deleteCompilingDir(String path) {
        File f = new File(path);
        if (f.exists()) {
            return f.delete();
        }
        return true;
    }

    public static int compile(String sourceDirPath, String destDirPath, String filePath) {
        String classpath = System.getProperty("java.class.path");
        String[] args1 = new String[]{
                "-classpath", classpath,
                "-sourcepath", sourceDirPath,
                "-d", destDirPath,
                filePath
        };

        return Main.compile(args1);
    }

    public static boolean addFileToCompilingDir(String sourceFilePath, String destFilePath) {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);


        return false;
    }
}

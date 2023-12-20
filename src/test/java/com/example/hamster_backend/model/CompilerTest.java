package com.example.hamster_backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompilerTest {
    @Test
    public void compileTest() {
        String sourcePath = "C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources\\compilingTestFiles";
        String putPutPath = "C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources\\compilingTestFiles";
        String filePath = "C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources\\compilingTestFiles\\CompilingTestFile0.java";

        int result = Compiler.compile(sourcePath, putPutPath, filePath);

        assertEquals(0, result);
    }

    @Test
    public void createCompilingDirTest() {
        String dirPath = "C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources\\PeterPan";

        boolean result = Compiler.createCompilingDir(dirPath);

        assertTrue(result);
    }

    @Test
    public void getCompilingDirPath() {
        String sourcePath = "C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources";
        String dirName = "PeterPan";

        String path = Compiler.getCompilingDirPath(sourcePath, dirName);

        assertEquals("C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources\\PeterPan", path);
    }

    @Test
    public void deleteCompilingDirTest() {
        String dirPath = "C:\\Users\\simon\\IdeaProjects\\Hamster_Backend\\src\\test\\resources\\PeterPan";

        boolean result = Compiler.deleteCompilingDir(dirPath);

        assertTrue(result);
    }
}

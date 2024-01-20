package at.ac.htlinn.hamsterbackend.run;

import org.junit.jupiter.api.Test;
import at.ac.htlinn.hamsterbackend.run.Compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompilerTest {
    @Test
    public void compileTest() {
        String sourcePath = "src\\test\\resources\\compilingTestFiles";
        String putPutPath = "src\\test\\resources\\compilingTestFiles";
        String filePath = "src\\test\\resources\\compilingTestFiles\\CompilingTestFile0.java";

        int result = Compiler.compile(sourcePath, putPutPath, filePath);

        assertEquals(0, result);
    }

    @Test
    public void createCompilingDirTest() {
        String dirPath = "src\\test\\resources\\PeterPan";

        boolean result = Compiler.createCompilingDir(dirPath);

        assertTrue(result);
    }

    @Test
    public void getCompilingDirPath() {
        String sourcePath = "src\\test\\resources";
        String dirName = "PeterPan";

        String path = Compiler.getCompilingDirPath(sourcePath, dirName);

        assertEquals("src\\test\\resources\\PeterPan", path);
    }

    @Test
    public void deleteCompilingDirTest() {
        String dirPath = "src\\test\\resources\\PeterPan";

        boolean result = Compiler.deleteCompilingDir(dirPath);

        assertTrue(result);
    }
}

package COP4620;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BaseTest {
    protected File[] getResourceFilesInDirectory(String dir) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final String path = classLoader.getResource(dir).getPath();
        return new File(path).listFiles();
    }

    protected String getSource(String filename) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        return String.join("\n", lines);
    }
}

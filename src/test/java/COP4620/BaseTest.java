package COP4620;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BaseTest {
    protected File[] getResourceFilesInDirectory(String dir) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final String path = classLoader.getResource(dir).getPath();
        return new File(path).listFiles();
    }

    protected String getResourcePath(String filename) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResource(filename).getPath();
    }

    protected File getResourceFile(String filename) {
        return new File(getResourcePath(filename));
    }

    protected String readResourceFile(String filename) throws Exception {
        return readFile(getResourceFile(filename).getAbsolutePath());
    }

    protected String readFile(String filename) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        return String.join("\n", lines);
    }
}

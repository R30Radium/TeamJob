package telegram.teamjob.implementation;



import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.nio.charset.StandardCharsets;

public class TestUtils {
    public static String getFileContent(String from) {
        try {
            return FileUtils.readFileToString(ResourceUtils.getFile(from), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

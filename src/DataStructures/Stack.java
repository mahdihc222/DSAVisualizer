package DataStructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Stack {

    public static String getCode() {
        try {
            String code = Files.readString(Path.of("src/code/stackHeader.txt"));
            return code;
        } catch (IOException e) {
            return "Failed to load code.";
        }
    }

    
}

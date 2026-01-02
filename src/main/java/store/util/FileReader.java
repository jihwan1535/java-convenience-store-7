package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class FileReader {

    // 단순히 파일 읽기
    public static List<String> readAll(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // 파일 읽고 convert -> default delimiter가 ,인 경우
    public static <T> List<T> convertTo(String filePath, Function<String[], T> mapper) {
        try {
            return Files.readAllLines(Paths.get(filePath))
                    .stream()
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(mapper)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // delimiter가 필요한 경우
    public static <T> List<T> convertTo(String filePath, Function<String[], T> mapper, String delimiter) {
        try {
            return Files.readAllLines(Paths.get(filePath))
                    .stream()
                    .skip(1)
                    .map(line -> line.split(delimiter))
                    .map(mapper)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

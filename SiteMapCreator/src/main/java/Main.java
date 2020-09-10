import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Main {

    private static int cores = Runtime.getRuntime().availableProcessors();
    private static final String FILE_PATH = "sitemap.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название сайта (например - https://skillbox.ru) --->");
        String url = scanner.nextLine();
        System.out.println("Идет проверка сайта...");
        LinkChecker linkChecker = new LinkChecker(url);
        String links = new ForkJoinPool(cores).invoke(linkChecker);

        System.out.println("Проверка сайта завершена");

        writeMapToFile(links);
    }

    private static void writeMapToFile(String links){
        List<String> linkList = Arrays.asList(links.split("\n"));
        List<String> siteMap = linkList.stream()
            .sorted(Comparator.comparing(u -> u))
            .map(u -> StringUtils.repeat('\t', StringUtils.countMatches(u, "/") - 2) + u)
            .collect(Collectors.toList());
        System.out.println("Идет запись карты в файл...");
        try {
            Files.write(Paths.get(FILE_PATH), siteMap);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Карта создана!");
    }
}

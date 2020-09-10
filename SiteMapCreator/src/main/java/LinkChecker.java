import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkChecker extends RecursiveTask<String> {

    private String url;
    private static List<String> links = new ArrayList<>();

    public LinkChecker(String url) {
        this.url = url;
    }

    private void getChildren(Set<LinkChecker> childTask) {
        Document document;
        Elements elements;

        try {
            Thread.sleep(50);
            document = Jsoup.connect(url).get();
            elements = document.select("a");
            for (Element element : elements) {
                String link = element.attr("abs:href");
                if (!link.isEmpty() && link.startsWith(url) && !link.contains("#") && !links
                    .contains(link)) {
                    System.out.println(link);
                    LinkChecker linkChecker = new LinkChecker(link);
                    linkChecker.fork();
                    childTask.add(linkChecker);
                    links.add(link);
                }
            }
        } catch (InterruptedException | IOException ignored) {
        }
    }

    @Override
    protected String compute() {
        StringBuffer stringBuffer = new StringBuffer(url + "\n");
        Set<LinkChecker> childTask = new HashSet<>();

        getChildren(childTask);

        for (LinkChecker linkChecker : childTask) {
            stringBuffer.append(linkChecker.join());
        }
        return stringBuffer.toString();
    }
}

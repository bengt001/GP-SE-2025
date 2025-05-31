package parsingutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.template.parsingutils.HtmlParser;
import de.techfak.gse.template.parsingutils.JsonParser;
import de.techfak.gse.template.parsingutils.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HtmlParserTests {

    public JsonNode root;

    public String html = """
            <!doctype html>
            <html>
              <head>
                <title>This is the title of the webpage!</title>
              </head>
              <body>
                <p>This is an example paragraph. Anything in the <strong>body</strong> tag will appear on the page, just like this <strong>p</strong> tag and its contents.</p>
              </body>
            </html>""";

    @BeforeEach
    void setUp() {
        try (
                InputStream is = JsonParserTests.class.getClassLoader().
                        getResourceAsStream("schemasWithFoLTree.json")) {
            assert is != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonTxt = br.lines().collect(Collectors.joining());
                root = mapper.readTree(jsonTxt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getTableOfContentsAsTreeTest() {
        ArrayList<Object> recursionPath = new ArrayList<>();
        ArrayList<ArrayList<Object>> allPaths = new ArrayList<>();
        JsonParser.findPathWithFieldAndValue(root, "type", "schema", recursionPath, allPaths);
        System.out.println("allPathsSize: " + allPaths.size());

        TreeNode<String> node1 = HtmlParser.getTableOfContentsAsTree(html);
        System.out.println(node1.hasOtherData());

        TreeNode<String> node2 = HtmlParser.getTableOfContentsAsTree(html);
        System.out.println(node2.hasOtherData());

        TreeNode<String> node3 = HtmlParser.getTableOfContentsAsTree(html);
        System.out.println(node3.hasOtherData());

    }
}

package parsingutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.template.parsingutils.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonParserTests {


    public JsonNode root;


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
    void findPathWithFieldAndValueTest() {
        ArrayList<Object> recursionPath = new ArrayList<>();
        ArrayList<ArrayList<Object>> allPaths = new ArrayList<>();
        JsonParser.findPathWithFieldAndValue(root, "type", "schema", recursionPath, allPaths);
        System.out.println("allPathsSize: " + allPaths.size());
        assertThat(allPaths.size()).isEqualTo(164);

        ArrayList<Object> rP = new ArrayList<>();
        ArrayList<ArrayList<Object>> aP = new ArrayList<>();
        JsonParser.findPathWithFieldAndValue(root, "Skibidi", "toilet", rP, aP);
        System.out.println("allPathsSize2: " + aP.size());
        assertThat(aP.size()).isEqualTo(0);
    }

    @Test
    void extractContentAsStringFromPath() {
        ArrayList<Object> recursionPath = new ArrayList<>();
        ArrayList<ArrayList<Object>> allPaths = new ArrayList<>();
        JsonParser.findPathWithFieldAndValue(root, "type", "schema", recursionPath, allPaths);
        System.out.println("allPathsSize: " + allPaths.size());

        ArrayList<String> realContent = JsonParser.extractContentAsStringFromPath(root, allPaths.getFirst(), "text");
        //System.out.println(realContent.getFirst());
        assertThat(realContent.size()).isEqualTo(1);

        ArrayList<String> realPathContent = JsonParser.extractContentAsStringFromPath(root, allPaths.getFirst(), "name");
        System.out.println("realPathContent: " + realPathContent);
        assertThat(realPathContent.size()).isEqualTo(2);

        ArrayList<String> noContent1 = JsonParser.extractContentAsStringFromPath(root, allPaths.getFirst(), "Bobrito Bandito");
        System.out.println("noContent1: " + noContent1.size());
        assertThat(noContent1.size()).isEqualTo(0);

        ArrayList<Object> wrongPath = new ArrayList<>();
        wrongPath.add("Rizzler");
        ArrayList<String> noContent2 = JsonParser.extractContentAsStringFromPath(root, wrongPath, "text");
        System.out.println("noContent2: " + noContent2.size());
        assertThat(noContent2.size()).isEqualTo(0);

    }
}

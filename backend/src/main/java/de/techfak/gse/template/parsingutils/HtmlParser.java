package de.techfak.gse.template.parsingutils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Html parser.
 */
public class HtmlParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlParser.class);

    private static final String BACKSLASH_N = "\\\\n ";
    private static final String BACKSLASH_N_WITHOUT_SPACE = "\\\\n";
    private static final String DOUBLE_BACKSLASH_N = " \\\\n \\\\n ";
    private static final String TRIPLE_BACKSLASH_N = " \\\\n \\\\n \\\\n ";
    private static final String NEWLINE = "\n";

    /**
     * This should not be used
     */
    protected HtmlParser() {
    }

    /**
     * Gets problem boxes.
     *
     * @param html the html
     * @return the problem boxes
     */
    public static ArrayList<String[]> getProblemBoxes(String html) {
        Document doc = Jsoup.parse(html);
        doc.outputSettings().prettyPrint(false);
        Elements problemSections = doc.select("section[data-style=problem]");
        ArrayList<String[]> problemBoxes = new ArrayList<>();
        for (Element section : problemSections) {
            LOGGER.debug(section.text());
            String text = section.text();
            text = text.replaceFirst(BACKSLASH_N, "");
            String[] frontBack = text.split(TRIPLE_BACKSLASH_N);
            if (frontBack.length == 2) {
                frontBack[1] = frontBack[1].replaceAll(DOUBLE_BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N_WITHOUT_SPACE, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(NEWLINE + NEWLINE, "");
                frontBack[0] = frontBack[0].replaceAll(BACKSLASH_N, NEWLINE);
                logBoxes(frontBack[0], frontBack[1], "ProblemBox");
                problemBoxes.add(frontBack);
            } else {
                LOGGER.debug("getProblemBoxes: Broken Split | Text: {}", text);
            }

        }
        return problemBoxes;
    }


    /**
     * Gets definition boxes.
     *
     * @param html the html
     * @return the definition boxes
     */
    public static ArrayList<String[]> getDefinitionBoxes(String html) {
        Document doc = Jsoup.parse(html);
        Elements problemSections = doc.select("section[data-style=definition]");
        ArrayList<String[]> definitionBoxes = new ArrayList<>();
        for (Element section : problemSections) {
            LOGGER.debug(section.text());
            String text = section.text();
            text = text.replaceFirst(BACKSLASH_N, "");
            String[] frontBack = text.split(" = ");
            if (frontBack.length == 2) {
                frontBack[1] = frontBack[1].replaceAll(DOUBLE_BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(NEWLINE + NEWLINE, "");
                frontBack[0] = frontBack[0].replaceAll(BACKSLASH_N, NEWLINE);
                logBoxes(frontBack[0], frontBack[1], "DefinitionBox");
                definitionBoxes.add(frontBack);
            } else {
                LOGGER.debug("getDefinitionBoxes: Broken Split | Text: {}", text);
            }

        }
        return definitionBoxes;
    }

    /**
     * Gets table of contents as tree.
     *
     * @param html the html
     * @return the table of contents as tree
     */
    public static TreeNode<String> getTableOfContentsAsTree(String html) {
        Document doc = Jsoup.parse(html);
        Elements headerSections = doc.select("li[data-style^=heading]");
        TreeNode<String> headerTree = new TreeNode<>("root");
        Pattern pattern = Pattern.compile("data-style=\"heading(\\d+)\"");
        String[] stringContents = new String[headerSections.size()];
        int[] stringOrder = new int[headerSections.size()];
        for (int i = 0; i < headerSections.size(); i++) {
            Matcher matcher = pattern.matcher(headerSections.get(i).outerHtml());
            matcher.find();
            String number = matcher.group(1);
            stringContents[i] = headerSections.get(i).text();
            stringOrder[i] = Integer.parseInt(number);
            LOGGER.debug("{} | Found heading number: {}", headerSections.get(i).outerHtml(), number);
        }
        ArrayList<TreeNode<String>> allNonRootNodes = new ArrayList<>();
        for (int i = 0; i < stringContents.length; i++) {
            TreeNode<String> currentNode = new TreeNode<>(stringContents[i]);
            allNonRootNodes.add(i, currentNode);
            if (stringOrder[i] <= 1) {
                headerTree.addChild(currentNode);
            } else {
                int currentOrder = stringOrder[i];

                for (int j = i; j >= 0; j--) {
                    if (currentOrder > stringOrder[j]) {
                        LOGGER.debug("{} {} {}", currentOrder, j, stringOrder[j]);
                        allNonRootNodes.get(j).addChild(currentNode);
                        break;
                    }
                }
            }
        }
        headerTree.logTree("getTableOfContentsAsTree");
        return headerTree;
    }

    private static void logBoxes(String top, String bottom, String boxtype) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("boxtype: {}\nTop: {}\nBottom: {}", boxtype, top, bottom);
        }
    }

}

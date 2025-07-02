package de.techfak.gse.template.parsingutils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Range;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
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
     * This should not be used.
     */
    protected HtmlParser() {
    }

    /**
     * Gets problem boxes.
     *
     * @param html the html
     * @return the problem boxes
     */
    @SuppressWarnings({"checkstyle:ArrayTrailingComma", "checkstyle:MultipleStringLiterals", "checkstyle:LineLength"})
    public static ArrayList<CardHelper> getProblemBoxes(String html) {
        Parser parser = Parser.htmlParser().setTrackPosition(true);
        Document doc = Jsoup.parse(html, parser);
        //doc.outputSettings().prettyPrint(false);
        Elements headerSections = doc.select("[data-style^=heading]");
        Elements problemSections = doc.select("section[data-style=problem]");
        ArrayList<CardHelper> problemBoxes = new ArrayList<>();
        for (Element section : problemSections) {
            LOGGER.debug(section.text());
            String text = section.text();
            text = text.replaceFirst(BACKSLASH_N, "");
            String[] frontBackFirst = text.split(TRIPLE_BACKSLASH_N);
            //This gets ugly cards too
            String[] frontBack = {
                    frontBackFirst[0],
                    String.join(" ", Arrays.copyOfRange(frontBackFirst, 1, frontBackFirst.length))
            };
            if (frontBack.length == 2) {
                frontBack[1] = frontBack[1].replaceAll(DOUBLE_BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N_WITHOUT_SPACE, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(NEWLINE + NEWLINE, "");
                frontBack[0] = frontBack[0].replaceAll(BACKSLASH_N, NEWLINE);
                logBoxes(frontBack[0], frontBack[1], "ProblemBox");
                //new code wohoh
                Range.Position posOfBox = section.sourceRange().start();
                int boxOffset = posOfBox.pos();

                Integer distance = null;
                Element headerElement = null;
                for (Element li : headerSections) {
                    Range.Position pos = li.sourceRange().start();
                    int charOffsetHeader = pos.pos();
                    //System.out.println("boxOffset: " + boxOffset + " HeadeOffset " + li.text() + " : " + li.sourceRange().start());
                    if ((boxOffset - charOffsetHeader) > 0) {
                        if (distance == null) {
                            distance = (boxOffset - charOffsetHeader);
                            headerElement = li;
                        } else if ((distance > (boxOffset - charOffsetHeader))) {
                            distance = (boxOffset - charOffsetHeader);
                            headerElement = li;
                        }
                    }
                }
                if (headerElement != null) {
                    CardHelper cardHelper = new CardHelper(frontBack, headerElement.text());
                    problemBoxes.add(cardHelper);
                } else {
                    System.out.println("Skibidi Toilet");
                }
            } else {
                LOGGER.debug("getProblemBoxes: Broken Split | Text: {}", text);
                //System.out.println("Broken Split");
            }

        }
        return problemBoxes;
    }


    /**
     * Gets definition boxes.
     * Bis auf eine die mit : gesplited werden muss.
     *
     * @param html the html
     * @return the definition boxes
     */
    @SuppressWarnings("checkstyle:LineLength")
    public static ArrayList<CardHelper> getDefinitionBoxes(String html) {
        Parser parser = Parser.htmlParser().setTrackPosition(true);
        Document doc = Jsoup.parse(html, parser);
        Elements headerSections = doc.select("[data-style^=heading]");
        //lol actually wrong name down here
        Elements problemSections = doc.select("section[data-style=definition]");
        ArrayList<CardHelper> definitionBoxes = new ArrayList<>();
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
                //new code wohoh
                Range.Position posOfBox = section.sourceRange().start();
                int boxOffset = posOfBox.pos();

                Integer distance = null;
                Element headerElement = null;
                for (Element li : headerSections) {
                    Range.Position pos = li.sourceRange().start();
                    int charOffsetHeader = pos.pos();
                    //System.out.println("boxOffset: " + boxOffset + " HeadeOffset " + li.text() + " : " + li.sourceRange().start());
                    if ((boxOffset - charOffsetHeader) > 0) {
                        if (distance == null) {
                            distance = (boxOffset - charOffsetHeader);
                            headerElement = li;
                        } else if ((distance > (boxOffset - charOffsetHeader))) {
                            distance = (boxOffset - charOffsetHeader);
                            headerElement = li;
                        }
                    }
                }
                if (headerElement != null) {
                    CardHelper cardHelper = new CardHelper(frontBack, headerElement.text());
                    definitionBoxes.add(cardHelper);
                } else {
                    System.out.println("Skibidi");
                }

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

package de.techfak.gse.template.parsingUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Html parser.
 */
public class HtmlParser {


    private static final String BACKSLASH_N = "\\\\n ";
    private static final String BACKSLASH_N_WITHOUT_SPACE = "\\\\n";
    private static final String DOUBLE_BACKSLASH_N = " \\\\n \\\\n ";
    private static final String TRIPLE_BACKSLASH_N = " \\\\n \\\\n \\\\n ";
    private static final String NEWLINE = "\n";

    /**
     * Instantiates a new Html parser.
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

            String text = section.text();
            text = text.replaceFirst(BACKSLASH_N, "");
            String[] frontBack = text.split(TRIPLE_BACKSLASH_N);
            if (frontBack.length == 2) {
                frontBack[1] = frontBack[1].replaceAll(DOUBLE_BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N_WITHOUT_SPACE, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(NEWLINE + NEWLINE, "");
                frontBack[0] = frontBack[0].replaceAll(BACKSLASH_N, NEWLINE);
                //Needs to be replaced with robust loging
                //System.out.println("############################################################################");
                //System.out.println(frontBack[0]);
                //System.out.println("----------------------------------------------------------------------------");
                //System.out.println(frontBack[1]);
                //System.out.println("#############################################################################");
                problemBoxes.add(frontBack);
            } else {
                //System.out.print("AHHHHHHHHHHHHHH");
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
            //System.out.println(section.text());
            String text = section.text();
            text = text.replaceFirst(BACKSLASH_N, "");
            String[] frontBack = text.split(" = ");
            if (frontBack.length == 2) {
                frontBack[1] = frontBack[1].replaceAll(DOUBLE_BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(BACKSLASH_N, NEWLINE);
                frontBack[1] = frontBack[1].replaceAll(NEWLINE + NEWLINE, "");
                frontBack[0] = frontBack[0].replaceAll(BACKSLASH_N, NEWLINE);
                //Needs to be replaced with robust loging
                //System.out.println("############################################################################");
                //System.out.println(frontBack[0]);
                //System.out.println("----------------------------------------------------------------------------");
                //System.out.println(frontBack[1]);
                //System.out.println("#############################################################################");
                definitionBoxes.add(frontBack);
            } else {
                //System.out.print("AHHHHHHHHHHHHHH");
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
            //System.out.println(headerSections.get(i).outerHtml());
            //System.out.println("Found heading number: " + number);
        }
        ArrayList<TreeNode<String>> allNonRootNodes = new ArrayList<>();
        for (int i = 0; i < stringContents.length; i++) {
            TreeNode<String> currentNode = new TreeNode<String>(stringContents[i]);
            allNonRootNodes.add(i, currentNode);
            if (stringOrder[i] <= 1) {
                headerTree.addChild(currentNode);
            } else {
                int currentOrder = stringOrder[i];

                for (int j = i; j >= 0; j--) {
                    if (currentOrder > stringOrder[j]) {
                        //System.out.println(currentOrder + " " + j + " " + stringOrder[j]);
                        allNonRootNodes.get(j).addChild(currentNode);
                        break;
                    }
                }
            }
        }
        //headerTree.printTree();
        return headerTree;
    }


}

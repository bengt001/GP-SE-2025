package parsingutils;

import de.techfak.gse.template.parsingutils.TreeNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeNodeTests {

    @Test
    void testTreeNode(){
        TreeNode<String> badTree = new TreeNode<>("root");
        badTree.addChild(new TreeNode<>("Inhaltsverzeichnis"));
        assertThat(badTree.hasOtherData()).isFalse();

        TreeNode<String> goodTree = new TreeNode<>("root");
        goodTree.addChild(new TreeNode<>("Inhaltsverzeichnis"));
        goodTree.addChild(new TreeNode<>("Other Data"));
        assertThat(goodTree.hasOtherData()).isTrue();
    }
}

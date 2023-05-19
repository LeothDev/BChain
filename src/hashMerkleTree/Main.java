package hashMerkleTree;

import java.sql.Array;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MerkleTree tree = new MerkleTree();
        ArrayList<String> transBlocks = new ArrayList<>();
        transBlocks.add("Test1");
        transBlocks.add("Test2");
        transBlocks.add("Test3");
        transBlocks.add("Test4");
        transBlocks.add("Test5");
        transBlocks.add("Test6");
        transBlocks.add("Test7");
        transBlocks.add("Test8");
        transBlocks.add("Test9");
        System.out.println(tree.getRoot(transBlocks));
    }
}

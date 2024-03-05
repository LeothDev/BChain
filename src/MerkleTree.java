import java.sql.Array;
import java.util.ArrayList;
public class MerkleTree {
    private static Node generateTree(ArrayList<String>  transBlock){
        ArrayList<Node> childNodes = new ArrayList<>();

        for (String transaction : transBlock) {
            childNodes.add(new Node(null, null, HashFunction.applySha256(transaction)));
        }

        return buildTree(childNodes);
    }

    public static Node buildTree(ArrayList<Node> children){
        ArrayList<Node> parents = new ArrayList<>();

        while (children.size() != 1){
            int idx = 0;
            int len = children.size();

            while (idx < len){
                Node leftChild = children.get(idx);
                Node rightChild = null;

                if ((idx + 1) < len) {
                    rightChild = children.get(idx + 1);
                }else {
                    rightChild = new Node(null, null, leftChild.hash);
                }

                String parentHash = HashFunction.applySha256(leftChild.hash + rightChild.hash);
                parents.add(new Node(leftChild, rightChild, parentHash));
                idx += 2;
            }

            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }

    public String getRoot(ArrayList<Transaction> transactions){
        ArrayList<String> L = new ArrayList<>();
        for (Transaction trans : transactions){
            L.add(trans.toString());
        }
        return generateTree(L).hash;
    }
}

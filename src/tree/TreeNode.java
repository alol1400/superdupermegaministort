
package tree;
import data.Attribute;
import data.Instance;
import java.util.LinkedHashMap;
import util.Constants.*;
import static util.Constants.NodeType.*;

/**
 *
 * @author alexander
 */
public class TreeNode {
    
    private Attribute attribute;
    private String value;
    private LinkedHashMap<String, TreeNode> children;
    private NodeType type;
    private String targetLabel;
    
    public TreeNode(Attribute attribute){
        this.attribute = attribute;
        children = new LinkedHashMap<String, TreeNode>();
        type = ROOT;
    }
    
    public TreeNode(String targetLabel){
        this.targetLabel = targetLabel;
        type = LEAF;
    }
    
    public void pushChild(String value, TreeNode child){
        children.put(value, child);
    }
    
     /**
     * @return the attribute
     */
    public Attribute getAttribute() {
        return attribute;
    }
    
    /**
     * @return the children
     */
    public LinkedHashMap<String, TreeNode> getChildren() {
        return children;
    }

    /**
     * @return the type
     */
    public NodeType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(NodeType type) {
        this.type = type;
    }

    /**
     * @return the targetLabel
     */
    public String getTargetLabel() {
        return targetLabel;
    }
    
    public void print(){
        if(type.equals(ROOT)){
            System.out.println("Root attribute: " + attribute.getName());
            //System.out.println("Children: " + children);
        }
        
        System.out.println("Root attribute: " + attribute.getName());
    }
}

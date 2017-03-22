
package tree;
import calc.Entropy;
import calc.SplitAttribute;
import data.Attribute;
import data.DataSet;
import data.Instance;
import data.NominalSubset;
import data.NumericSubset;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import static util.Constants.ValueType.*;

/**
 *
 * @author alexander
 */
public class DecisionTree {
    private ArrayList<Attribute> attributes;
    private ArrayList<Instance> instances;
    private Attribute target;
    private TreeNode root;
    private int leafIndex;
    
    public DecisionTree(DataSet d) throws IOException{
        attributes = d.getAttributesNoTarget();
        instances = d.getInstances();
        target = d.getTarget();
        root = treeBuilder(target, attributes, instances);
    }
    
    public TreeNode getRoot(){
        return root;
    }
    
    private TreeNode treeBuilder(Attribute target, 
            ArrayList<Attribute> attributes, ArrayList<Instance> instances)
            throws IOException{
        
        double entropy = Entropy.calculate(target, instances);
        
        /* 
        * Either stop when entropy is 0 (pure set) or when
        * there are no attributes left (we have exhausted the tree)
        */ 
        if(entropy == 0 || attributes.size() == 0){
            String leafLabel = "";
            if(entropy == 0){
                leafLabel = instances.get(0).getLabel();
            } else{
                leafLabel = getMajorityLabel(instances);
            }
            TreeNode leaf = new TreeNode(leafLabel);
            return leaf;
        }
        
        // Choose best attribute to split on
        SplitAttribute split = new SplitAttribute(target, attributes, instances);
        Attribute rootAttribute = split.getSplitAttribute();
        
        // Pop the root attribute from set and create the root
        attributes.remove(rootAttribute);
        TreeNode root = new TreeNode(rootAttribute);
        
        // Create the branches    
        if(rootAttribute.getType().equals(NUMERIC)){
            NumericSubset valueSubset = new NumericSubset(rootAttribute, instances);
            ArrayList<Instance> subset = valueSubset.getInstances();
            String str = rootAttribute.getValues().get(0);
            if(subset.size() == 0){
                    String leafLabel = getMajorityLabel(instances);
                    TreeNode leaf = new TreeNode(rootAttribute, str, leafLabel);
                    root.pushChild(str, leaf);
                } else{
                    TreeNode child = treeBuilder(target, attributes, subset);
                    root.pushChild(str, child);
            }
        } else{
            NominalSubset valueSubsets = new NominalSubset(rootAttribute, instances);
            for(String str : rootAttribute.getValues()){
                ArrayList<Instance> subset = valueSubsets.getSubsetOf(str);
                if(subset.size() == 0){
                    String leafLabel = getMajorityLabel(instances);
                    TreeNode leaf = new TreeNode(rootAttribute, str, leafLabel);
                    root.pushChild(str, leaf);
                } else{
                    TreeNode child = treeBuilder(target, attributes, subset);
                    root.pushChild(str, child);
                }
            }
        }
        attributes.add(rootAttribute);
        return root;
    }
    
    private String getMajorityLabel(ArrayList<Instance> instances){
        LinkedHashMap<String, Integer> valueCount = new LinkedHashMap<>();
        ArrayList<String> targetValues = target.getValues();
        for(String value : targetValues){
            valueCount.put(value, 0);
        }
        for(Instance i : instances){
            String value = i.getLabel();
            int count = valueCount.get(value);
            valueCount.put(value, ++count);
        }
        
        String maxLabel = "";
        int maxCount = 0;
        
        for(String str : valueCount.keySet()){
            int currCount = valueCount.get(str);
            if(currCount > maxCount){
                maxLabel = str;
                maxCount = currCount;
            }
        }
        
        return maxLabel;
    }
}

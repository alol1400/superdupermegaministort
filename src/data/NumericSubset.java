
package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import jdk.nashorn.internal.objects.NativeArray;

/**
 *
 * @author alexander
 */
public class NumericSubset {
    
    private Attribute filterAttribute;
    private ArrayList<Instance> instances;
    private ArrayList<Double> subset;
    private double median;
    private double mean;
    
    public NumericSubset(Attribute filter, ArrayList<Instance> instances){
        this.filterAttribute = filter;
        this.instances = instances;
        subset = new ArrayList<>();
        String filterName = filter.getName();
        
        Comparator<Instance> comp = new Comparator<Instance>() {
            @Override
            public int compare(Instance i1, Instance i2) {
                String valueStr_i1 = i1.getValueMap().get(filterName);
                String valueStr_i2 = i2.getValueMap().get(filterName);
                double value_i1 = Double.parseDouble(valueStr_i1);
                double value_i2 = Double.parseDouble(valueStr_i2);
                
                if(value_i1 < value_i2){
                    return -1;
                } else if (value_i1 > value_i2){
                    return 1;
                } else{
                    return 0;
                }
            }
        };
        
        // now create the subset
        for(Instance i : instances){
            String valueStr_i = i.getValueMap().get(filterName);
            double value_i = Double.parseDouble(valueStr_i);
            subset.add(value_i);
        }
        
        // sort and add to subset
        Collections.sort(subset);
        Collections.sort(instances, comp);
        
        // set median
        int size = subset.size();
        System.out.println("subset.size() = " + size);
        if(size % 2 == 0){
            median = (subset.get(size/2) + subset.get(size/2 - 1)) / 2;
            System.out.println("median = (" + (subset.get(size/2) + 
                    " + " + subset.get(size/2 - 1) + ")/" + 2));
        } else{
            median = subset.get((size-1)/2);
        }
        
        // set mean
        double sum = 0.0;
        for(double val : subset){
            sum += val;
        }
        mean = sum/size;
    }
    
    public double getMean(){
        return mean;
    }
    
    /**
     * @return the filterAttribute
     */
    public Attribute getFilterAttribute() {
        return filterAttribute;
    }

    /**
     * @return the instances
     */
    public ArrayList<Instance> getInstances() {
        return instances;
    }

    /**
     * @return the subset
     */
    public ArrayList<Double> getSubset() {
        return subset;
    }

    /**
     * @return the median
     */
    public double getMedian() {
        return median;
    }
    
    public void print(){
        System.out.println(filterAttribute.getName() + ": " + subset 
                + "\nMedian value = " + median 
                + "\nMean value = " + mean);
    }
}

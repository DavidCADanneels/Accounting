package be.dafke.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 8/03/13
 * Time: 13:16
 */
public class MultiValueMap<K,V> {
    private TreeMap<K,List<V>> data;

    public MultiValueMap (){
        data = new TreeMap<>();
    }

    public V addValue(K key, V value){
        if(!data.containsKey(key)){
            data.put(key, new ArrayList<>());
        }
        List<V> list = data.get(key);
        list.add(value);
        return value;
    }

    public V removeValue(K key, V value){
        List<V> list = data.get(key);
        list.remove(value);
        if(list.isEmpty()){
            data.remove(key);
        }
        return value;
    }

    public ArrayList<V> values(){
        ArrayList<V> result = new ArrayList<>();
        for (List<V> list:data.values()){
            result.addAll(list);
        }
        return result;
    }

    public ArrayList<V> tailList(K key, boolean inclusive){
        ArrayList<V> result = new ArrayList<>();
        SortedMap<K,List<V>> tailMap = data.tailMap(key, inclusive);
        for (List<V> list:tailMap.values()){
            result.addAll(list);
        }
        return result;
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public ArrayList<V> get(K key){
        return new ArrayList<>(data.get(key));
    }
}

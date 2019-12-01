package be.dafke.Utils

class MultiValueMap<K,V> {
    TreeMap<K,List<V>> data

    MultiValueMap (){
        data = new TreeMap<>()
    }

    V addValue(K key, V value){
        if(!data.containsKey(key)){
            data.put(key, new ArrayList<>())
        }
        List<V> list = data.get(key)
        list.add(value)
        value
    }

    V removeValue(K key, V value){
        List<V> list = data.get(key)
        list.remove(value)
        if(list.empty){
            data.remove(key)
        }
        value
    }

    ArrayList<V> values(){
        ArrayList<V> result = new ArrayList<>()
        for (List<V> list:data.values()){
            result.addAll(list)
        }
        result
    }

    ArrayList<V> tailList(K key, boolean inclusive){
        ArrayList<V> result = new ArrayList<>()
        SortedMap<K,List<V>> tailMap = data.tailMap(key, inclusive)
        for (List<V> list:tailMap.values()){
            result.addAll(list)
        }
        result
    }

    boolean isEmpty(){
        data.empty
    }

    ArrayList<V> get(K key){
        data.get(key)==null?null:new ArrayList<>(data.get(key))
    }
}

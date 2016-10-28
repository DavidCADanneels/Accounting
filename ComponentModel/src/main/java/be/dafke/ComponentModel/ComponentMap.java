package be.dafke.ComponentModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * User: david
 * Date: 28-12-13
 * Time: 0:01
 */
public class ComponentMap {
    private static final ArrayList<RefreshableComponent> refreshableComponents = new ArrayList<>();
    private static final HashMap<String, DisposableComponent> disposableComponents = new HashMap<>();

    public static void closeAllFrames(){
        Collection<DisposableComponent> collection = disposableComponents.values();
        for(DisposableComponent frame: collection){
            frame.dispose();
        }
    }

    public static DisposableComponent getDisposableComponent(String name){
        return disposableComponents.get(name);
    }

    public static void refreshAllFrames(){
        for(RefreshableComponent frame: refreshableComponents){
            frame.refresh();
        }
    }

    public static void addRefreshableComponent(RefreshableComponent frame) {
        refreshableComponents.add(frame);
    }

    public static void addDisposableComponent(String key, DisposableComponent frame) {
        refreshableComponents.add(frame);
        disposableComponents.put(key, frame);
    }
}

package be.dafke.ComponentModel;

import java.util.Collection;
import java.util.HashMap;

/**
 * User: david
 * Date: 28-12-13
 * Time: 0:01
 */
public class ComponentMap {
    private static final HashMap<String, RefreshableComponent> refreshableComponents = new HashMap<String, RefreshableComponent>();
    private static final HashMap<String, DisposableComponent> disposableComponents = new HashMap<String, DisposableComponent>();

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
        Collection<RefreshableComponent> collection = refreshableComponents.values();
        for(RefreshableComponent frame: collection){
            frame.refresh();
        }
    }

    public static void addRefreshableComponent(String key, RefreshableComponent frame) {
        refreshableComponents.put(key, frame);
    }

    public static void addDisposableComponent(String key, DisposableComponent frame) {
        refreshableComponents.put(key, frame);
        disposableComponents.put(key, frame);
    }
}

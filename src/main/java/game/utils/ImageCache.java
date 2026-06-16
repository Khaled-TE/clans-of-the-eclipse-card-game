package game.utils;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageCache {

    private static final Map<String, Image> cache = new HashMap<>();
    
    private static final String BASE_PATH = "/images"; 

    public static Image getImage(String textureName) {
        if (!cache.containsKey(textureName)) {
            try {
                String fullPath = BASE_PATH + textureName + ".jpg";
                
                Image img = new Image(ImageCache.class.getResourceAsStream(fullPath),280,400,true,true);
                cache.put(textureName, img);
            } catch (Exception e) {
                System.err.println("Critical Error: Missing asset -> " + BASE_PATH + textureName + ".jpg");
                e.printStackTrace();
            }
        }
        return cache.get(textureName);
    }

    public static Image getFullResImage(String textureName) {
        String key = textureName + "_full";
        if (!cache.containsKey(key)) {
            try {
                String fullPath = BASE_PATH + textureName + ".jpg";
                Image img = new Image(ImageCache.class.getResourceAsStream(fullPath),
                                      0, 0, true, true);   // 0,0 = load at native size
                cache.put(key, img);
            } catch (Exception e) {
                System.err.println("Critical Error: Missing asset -> " + BASE_PATH + textureName + ".jpg");
                e.printStackTrace();
            }
        }
        return cache.get(key);
    }
}

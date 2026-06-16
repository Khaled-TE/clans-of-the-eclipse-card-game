package game.utils;

import game.model.SaveData;
import java.io.*;

public class SaveManager {

    private static final String SAVE_DIRECTORY = "src/main/resources/saves";
    private static final String SAVE_FILE = SAVE_DIRECTORY + "/clans_save.ser";

    public static void saveGame(SaveData data) {
        try {
            File folder = new File(SAVE_DIRECTORY);
            if (!folder.exists()) {
                folder.mkdirs(); 
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
                oos.writeObject(data);
                System.out.println("Game saved successfully in resources/saves.");
            }
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static SaveData loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No save file found.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            SaveData data = (SaveData) ois.readObject();
            System.out.println("Game loaded successfully from resources/saves.");
            return data;
        } catch (Exception e) {
            System.out.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    public static boolean saveExists() {
        return new File(SAVE_FILE).exists();
    }

    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
            System.out.println("Save file deleted.");
        }
    }
}
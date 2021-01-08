package de.tzimon.ranksystem.utils;

import de.tzimon.ranksystem.Permission;
import de.tzimon.ranksystem.RankSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PermissionLoader {

    private static final String FILE_NAME = "default_permissions.txt";

    public static void load() {
        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = PermissionLoader.class.getClassLoader().getResourceAsStream(PermissionLoader.FILE_NAME);
        assert inputStream != null : "Unable to find " + PermissionLoader.FILE_NAME;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String input;

            while ((input = bufferedReader.readLine()) != null)
                stringBuilder.append(input).append("\n");
        } catch (IOException ignored) {
            RankSystem.log("§cUnable to load default permissions");
        }

        String[] permissions = stringBuilder.toString().split(",");

        for (String permission : permissions) {
            Permission.get(permission);
        }
    }

}

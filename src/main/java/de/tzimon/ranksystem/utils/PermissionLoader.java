package de.tzimon.ranksystem.utils;

import de.tzimon.ranksystem.Permission;
import de.tzimon.ranksystem.RankSystem;

import java.io.*;
import java.util.*;

public class PermissionLoader {

    private static final String INTERNAL_FILE_NAME = "default_permissions.txt";
    private static final String EXTERNAL_FILE_NAME = "permission_map.txt";

    public static void load() {
        final Set<String> permissions = new HashSet<>();
        permissions.addAll(PermissionLoader.loadInternalFile(PermissionLoader.INTERNAL_FILE_NAME));
        permissions.addAll(PermissionLoader.loadExternalFile(RankSystem.getPlugin().getDataFolder() + "/" + PermissionLoader.EXTERNAL_FILE_NAME));

        for (String permission : permissions) {
            try {
                Permission.get(permission);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static Collection<String> loadInternalFile(String filePath) {
        final StringBuilder stringBuilder = new StringBuilder();

        final InputStream inputStream = PermissionLoader.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            RankSystem.log("Unable to find " + filePath);
            return new ArrayList<>();
        }

        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try (final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String input;

            while ((input = bufferedReader.readLine()) != null)
                stringBuilder.append(input);
        } catch (IOException ignored) {
            RankSystem.log("§cUnable to load default permissions");
        }

        final String[] permissions = stringBuilder.toString().split(",");
        return Arrays.asList(permissions);
    }

    private static Collection<String> loadExternalFile(String filePath) {
        final File file = new File(filePath);

        try {
            if (!file.exists())
                file.createNewFile();

            final FileReader fileReader = new FileReader(file);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            final StringBuilder stringBuilder = new StringBuilder();
            String input;

            while ((input = bufferedReader.readLine()) != null) {
                if (!input.startsWith("#"))
                    stringBuilder.append(input);
            }

            bufferedReader.close();

            final String[] permissions = stringBuilder.toString().split(",");
            return Arrays.asList(permissions);
        } catch (IOException ignored) {
            RankSystem.log("§cUnable to load default permissions");
            return new ArrayList<>();
        }
    }


}

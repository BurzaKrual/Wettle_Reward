package com.github.burzakrual.minecraft;


import java.util.HashMap;
import java.util.Map;

public class AccountLinkDatabase {
    private static final Map<String, String> accountMapping = new HashMap<>();

    public static void linkAccounts(String minecraftUsername, String twitchUsername) {
        accountMapping.put(minecraftUsername, twitchUsername);
    }

    public static String getTwitchUsername(String minecraftUsername) {
        return accountMapping.get(minecraftUsername);
    }
}

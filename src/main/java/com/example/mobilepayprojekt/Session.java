package com.example.mobilepayprojekt;

public class Session {
    private static Bruger currentUser;

    public static Bruger getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Bruger bruger) {
        currentUser = bruger;
    }

    public static void clear() {
        currentUser = null;
    }
}

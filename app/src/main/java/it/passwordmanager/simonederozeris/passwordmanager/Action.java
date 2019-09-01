package it.passwordmanager.simonederozeris.passwordmanager;

public enum Action {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");

    private String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static Action getEnumStatoGestione(String actionString) {
        Action action = null;
        switch (actionString) {
            case "I":
                action= Action.INSERT;
                break;
            case "U":
                action= Action.UPDATE;
                break;
            case "D":
                action= Action.DELETE;
                break;
        }
        return action;
    }
}


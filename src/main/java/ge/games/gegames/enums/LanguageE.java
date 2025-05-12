package ge.games.gegames.enums;

public enum LanguageE {
    EN("en"),
    KA("ka");

    private final String code;

    LanguageE(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

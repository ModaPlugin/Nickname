package cx.mia.moda.nickname.storage;

public enum DataType {
    NICKNAME("nickname");


    private final String identifier;

    DataType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}

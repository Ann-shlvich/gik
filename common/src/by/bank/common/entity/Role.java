package by.bank.common.entity;

public enum Role {

    NONE("Никто"),
    ADMIN("Администратор"),
    EXPERT("Эксперт"),
    USER("Пользователь");

    private final String text;

    Role(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}

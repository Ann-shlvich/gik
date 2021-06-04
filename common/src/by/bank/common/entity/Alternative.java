package by.bank.common.entity;

public class Alternative extends Entity {

    private int ordinal;
    private String description;
    private int examinationId;

    public Alternative() {

    }

    public Alternative(int id) {
        super(id);
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(int examinationId) {
        this.examinationId = examinationId;
    }

    public String toString() {
        return description;
    }
}

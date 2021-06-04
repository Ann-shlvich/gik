package by.bank.common.entity;

public class Opinion extends Entity {

    private byte [] data;
    private int examinationId;
    private int userId;

    public Opinion() {

    }

    public Opinion(int id) {
        super(id);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(int examinationId) {
        this.examinationId = examinationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

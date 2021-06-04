package by.bank.common.entity;

import java.time.LocalDate;
import java.util.List;

public class Examination extends Entity {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<Alternative> alternatives;
    private int opinionCount;

    public Examination() {

    }

    public Examination(int id) {
        super(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    public int getOpinionCount() {
        return opinionCount;
    }

    public void setOpinionCount(int opinionCount) {
        this.opinionCount = opinionCount;
    }

    public boolean hasOpinions() {
        return opinionCount != 0;
    }
}

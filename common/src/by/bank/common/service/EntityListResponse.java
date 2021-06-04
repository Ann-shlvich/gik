package by.bank.common.service;

import by.bank.common.entity.Entity;

import java.util.List;

public class EntityListResponse extends Response {

    private List<? extends Entity> entities;

    public EntityListResponse() {
        super(ResponseType.SUCCESS);
    }

    public List<? extends Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<? extends Entity> entities) {
        this.entities = entities;
    }
}

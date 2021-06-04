package by.bank.common.service;

import by.bank.common.entity.Entity;

public class EntityResponse extends Response {

    private Entity entity;

    public EntityResponse() {
        super(ResponseType.SUCCESS);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}

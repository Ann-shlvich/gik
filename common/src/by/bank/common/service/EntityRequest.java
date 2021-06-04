package by.bank.common.service;

import by.bank.common.entity.Entity;

public class EntityRequest extends Request {

    private Entity entity;

    public EntityRequest(RequestType requestType) {
        super(requestType);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}

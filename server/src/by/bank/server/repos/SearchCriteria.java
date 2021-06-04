package by.bank.server.repos;

import by.bank.common.entity.Entity;

public interface SearchCriteria<T extends Entity> {

    boolean accepted(T value);
}

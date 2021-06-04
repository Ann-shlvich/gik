package by.bank.server.core;

import by.bank.common.entity.Entity;
import by.bank.common.entity.User;
import by.bank.common.service.*;
import by.bank.server.repos.UserRepository;

import java.util.List;

public class UserProcessor implements Processor {

    private final UserRepository userRepos;

    public UserProcessor() {
        userRepos = new UserRepository();
    }

    @Override
    public Response process(Request request) {
        switch (request.getRequestType()) {
            case USER_ALL:
                return getAll();
            case USER_GET:
                return get((EntityRequest) request);
            case USER_CREATE:
                return create((EntityRequest) request);
            case USER_UPDATE:
                return update((EntityRequest) request);
            case USER_DELETE:
                return delete((EntityRequest) request);
        }
        return new SimpleResponse(ResponseType.ERROR, "Неверный запрос");
    }

    private Response getAll() {
        List<User> users = userRepos.query(null);
        if (users == null) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка чтения из БД!");
        }
        users.forEach(u -> u.setPassword(""));
        EntityListResponse response = new EntityListResponse();
        response.setEntities(users);
        return response;
    }

    private Response get(EntityRequest request) {
        final Entity entity = request.getEntity();
        if (entity == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<User> users = userRepos.query(u -> u.getId() == entity.getId());
        if (users.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Пользователь с указанным ID не найден!");
        }
        EntityResponse response = new EntityResponse();
        response.setEntity(users.get(0));
        return response;
    }

    private Response create(EntityRequest request) {
        User user = (User) request.getEntity();
        if (user == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<User> experts = userRepos.query(v -> v.getLogin().equals(user.getLogin()));
        if (experts.size() > 0) {
            return new SimpleResponse(ResponseType.ERROR, "Пользователь с указанным логином уже существует!");
        }
        if (!userRepos.create(user)) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка создания записи!");
        }
        return new SimpleResponse(ResponseType.SUCCESS);
    }

    private Response update(EntityRequest request) {
        User user = (User) request.getEntity();
        if (user == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<User> users = userRepos.query(u -> u.getId() == user.getId());
        if (users.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Пользователь с указанным ID не найден!");
        }
        User oldValue = users.get(0);
        users = userRepos.query(value -> value.getLogin().equals(user.getLogin()));
        if (users.size() == 1 && users.get(0).getId() != user.getId() ) {
            return new SimpleResponse(ResponseType.ERROR, "Пользователь с указанным логином уже существует!");
        }
        if (user.getPassword().isEmpty()) {
            user.setPassword(oldValue.getPassword());
        }
        if (!userRepos.update(user)) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка изменения записи!");
        }
        return new SimpleResponse(ResponseType.SUCCESS);

    }

    private Response delete(EntityRequest request) {
        User user = (User) request.getEntity();
        if (user == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        if (userRepos.isUsed(user)) {
            return new SimpleResponse(ResponseType.ERROR, "Нельзя удалять используемую запись!");
        }
        if (!userRepos.remove(user)) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка удаления записи!");
        }
        return new SimpleResponse(ResponseType.SUCCESS);
    }
}

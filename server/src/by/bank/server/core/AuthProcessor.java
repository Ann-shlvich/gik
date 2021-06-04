package by.bank.server.core;

import by.bank.common.entity.User;
import by.bank.common.service.*;
import by.bank.server.repos.UserRepository;

import java.util.List;

public class AuthProcessor implements Processor {

    private final UserRepository userRepos;

    public AuthProcessor() {
        userRepos = new UserRepository();
    }

    @Override
    public Response process(Request request) {
        switch (request.getRequestType()) {
            case LOGIN:
                return login((AuthRequest) request);
            case LOGOUT:
                return logout(request);
            case PASSWORD:
                return password((AuthRequest) request);
        }
        return null;
    }

    private Response login(AuthRequest request) {
        List<User> users = userRepos.query(v -> v.getLogin().equals(request.getLogin())
                && v.getPassword().equalsIgnoreCase(request.getPassword()));
        if (users.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Неверные имя пользователя или пароль!");
        }
        EntityResponse response = new EntityResponse();
        User user = users.get(0);
        user.setPassword("");
        response.setEntity(user);
        return response;
    }

    private Response logout(Request request) {
        return new SimpleResponse(ResponseType.SUCCESS);
    }

    private Response password(AuthRequest request) {
        List<User> users = userRepos.query(v -> v.getLogin().equals(request.getLogin())
                && v.getPassword().equalsIgnoreCase(request.getPassword()));
        if (users.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка изенения пароля!");
        }
        User user = users.get(0);
        user.setPassword(request.getNewPassword());
        userRepos.update(user);
        return new SimpleResponse(ResponseType.SUCCESS, "Пароль был успешно изменен!");
    }
}

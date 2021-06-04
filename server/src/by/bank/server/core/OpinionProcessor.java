package by.bank.server.core;

import by.bank.common.entity.Opinion;
import by.bank.common.service.*;
import by.bank.server.repos.OpinionRepository;

import java.util.List;

public class OpinionProcessor implements Processor {

    private OpinionRepository opinionRepos;

    public OpinionProcessor() {
        opinionRepos = new OpinionRepository();
    }

    @Override
    public Response process(Request request) {
        switch (request.getRequestType()) {
            case OPINION_GET:
                return get((EntityRequest) request);
            case OPINION_UPDATE:
                return save((EntityRequest) request);
            case OPINION_DELETE:
                return remove((EntityRequest) request);
        }
        return new SimpleResponse(ResponseType.ERROR, "Неверный запрос!");
    }

    private Response get(EntityRequest request) {
        final Opinion opinion = (Opinion) request.getEntity();
        if (opinion == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<Opinion> opinions = opinionRepos.query(v -> v.getExaminationId() == opinion.getExaminationId()
                && v.getUserId() == opinion.getUserId());
        EntityResponse response = new EntityResponse();
        if (opinions.size() != 1) {
            response.setEntity(opinion);
        } else {
            response.setEntity(opinions.get(0));
        }
        return response;
    }

    private Response save(EntityRequest request) {
        final Opinion opinion = (Opinion) request.getEntity();
        if (opinion == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        boolean result = opinion.getId() == 0 ? opinionRepos.create(opinion) : opinionRepos.update(opinion);
        return result ? new SimpleResponse(ResponseType.SUCCESS) :
                new SimpleResponse(ResponseType.ERROR, "Ошибка сохранения данных!");
    }

    private Response remove(EntityRequest request) {
        final Opinion opinion = (Opinion) request.getEntity();
        if (opinion == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        if (!opinionRepos.remove(opinion)) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка удаления данных!");
        }
        return new SimpleResponse(ResponseType.SUCCESS);
    }
}

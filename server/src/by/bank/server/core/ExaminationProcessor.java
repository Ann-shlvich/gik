package by.bank.server.core;

import by.bank.common.entity.Alternative;
import by.bank.common.entity.Examination;
import by.bank.common.entity.Opinion;
import by.bank.common.service.*;
import by.bank.server.repos.AlternativeRepository;
import by.bank.server.repos.ExaminationRepository;
import by.bank.server.repos.OpinionRepository;

import java.util.List;

public class ExaminationProcessor implements Processor {

    private final ExaminationRepository examinationRepos;
    private final AlternativeRepository alternativeRepos;
    private final OpinionRepository opinionRepos;

    public ExaminationProcessor() {
        this.examinationRepos = new ExaminationRepository();
        this.alternativeRepos = new AlternativeRepository();
        this.opinionRepos = new OpinionRepository();
    }

    @Override
    public Response process(Request request) {
        switch (request.getRequestType()) {
            case EXAMINATION_ALL:
                return getAll();
            case EXAMINATION_GET:
                return get((EntityRequest) request);
            case EXAMINATION_CREATE:
                return create((EntityRequest) request);
            case EXAMINATION_UPDATE:
                return update((EntityRequest) request);
            case EXAMINATION_DELETE:
                return remove((EntityRequest) request);
        }
        return new SimpleResponse(ResponseType.ERROR, "Неверный запрос!");
    }

    private Response getAll() {
        List<Examination> examinations = examinationRepos.query(null);
        if (examinations == null) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка БД!");
        }
        examinations.forEach(this::fillOpinionsNumber);
        EntityListResponse response = new EntityListResponse();
        response.setEntities(examinations);
        return response;
    }

    private Response get(EntityRequest request) {
        final Examination value = (Examination) request.getEntity();
        if (value == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<Examination> examinations = examinationRepos.query(v -> v.getId() == value.getId());
        if (examinations.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Экспертиза с указанным ID не найдена");
        }
        final Examination examination = examinations.get(0);
        List<Alternative> alternatives = alternativeRepos.query(v -> v.getExaminationId() == examination.getId());
        examination.setAlternatives(alternatives);
        fillOpinionsNumber(examination);
        EntityResponse response = new EntityResponse();
        response.setEntity(examination);
        return response;
    }

    private Response create(EntityRequest request) {
        final Examination value = (Examination) request.getEntity();
        if (value == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<Alternative> alternatives = value.getAlternatives();
        if (!examinationRepos.create(value)) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка создания записи!");
        }
        List<Examination> list = examinationRepos.query(v ->  v.getTitle().equals(value.getTitle()) &&
                v.getStartDate().isEqual(value.getStartDate()) && v.getEndDate().isEqual(value.getEndDate()));
        if (list.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка создания записи!");
        }
        final int examinationId = list.get(0).getId();
        for (Alternative alt : alternatives) {
            alt.setExaminationId(examinationId);
            if (!alternativeRepos.create(alt)) {
                alternativeRepos.query(v -> v.getExaminationId() == examinationId).forEach(alternativeRepos::remove);
                examinationRepos.remove(list.get(0));
                return new SimpleResponse(ResponseType.ERROR, "Ошибка создания альтернативы экспертизы!");
            }
        }
        return new SimpleResponse(ResponseType.SUCCESS);
    }

    private Response update(EntityRequest request) {
        final Examination value = (Examination) request.getEntity();
        if (value == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<Examination> list = examinationRepos.query(v -> v.getId() == value.getId());
        if (list.size() != 1) {
            return new SimpleResponse(ResponseType.ERROR, "Экспертиза с указанным ID не найдена!");
        }
        Examination oldValue = list.get(0);
        fillOpinionsNumber(oldValue);
        if (oldValue.hasOpinions()) {
            return new SimpleResponse(ResponseType.ERROR, "Нельзя изменять экспертизу с оценками!");
        }
        if (!examinationRepos.update(value)) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка изменения экспертизы!");
        }
        alternativeRepos.query(v -> v.getExaminationId() == value.getId()).forEach(alternativeRepos::remove);
        for (Alternative alt : value.getAlternatives()) {
            alt.setExaminationId(value.getId());
            alternativeRepos.create(alt);
        }
        return new SimpleResponse(ResponseType.SUCCESS);
    }

    private Response remove(EntityRequest request) {
        final Examination value = (Examination) request.getEntity();
        if (value == null) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        opinionRepos.query(v -> v.getExaminationId() == value.getId()).forEach(opinionRepos::remove);
        alternativeRepos.query(v -> v.getExaminationId() == value.getId()).forEach(alternativeRepos::remove);
        examinationRepos.remove(value);
        return new SimpleResponse(ResponseType.SUCCESS);
    }

    private void fillOpinionsNumber(Examination examination) {
        List<Opinion> opinions = opinionRepos.query(v -> v.getExaminationId() == examination.getId());
        examination.setOpinionCount(opinions.size());
    }

}

package by.bank.server.core;

import by.bank.common.entity.Alternative;
import by.bank.common.entity.Examination;
import by.bank.common.entity.Opinion;
import by.bank.common.entity.Solution;
import by.bank.common.service.*;
import by.bank.server.repos.AlternativeRepository;
import by.bank.server.repos.ExaminationRepository;
import by.bank.server.repos.OpinionRepository;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolutionProcessor implements Processor {

    private final ExaminationRepository examinationRepos;
    private final AlternativeRepository alternativeRepos;
    private final OpinionRepository opinionRepos;

    public SolutionProcessor() {
        this.alternativeRepos = new AlternativeRepository();
        this.examinationRepos = new ExaminationRepository();
        this.opinionRepos = new OpinionRepository();
    }

    @Override
    public Response process(Request request) {
        if (request.getRequestType() != RequestType.SOLUTION) {
            return new SimpleResponse(ResponseType.ERROR, "Неверный запрос!");
        }
        final Examination examination = (Examination) ((EntityRequest) request).getEntity();
        if (examination == null || examination.isNew()) {
            return new SimpleResponse(ResponseType.ERROR, "В запросе отсутствуют данные!");
        }
        List<Alternative> alternatives = alternativeRepos.query(v -> v.getExaminationId() == examination.getId());
        List<Opinion> opinions = opinionRepos.query(v -> v.getExaminationId() == examination.getId());
        if (opinions.size() < 2) {
            return new SimpleResponse(ResponseType.ERROR, "Расчет невозможен из-за малого количества оценок!");
        }
        Map<Integer, Alternative> alts = new HashMap<>();
        alternatives.forEach(a -> alts.put(a.getOrdinal(), a));

        int altCount = alternatives.size();
        int opiCount = opinions.size();
        int [][] matrix = new int[opiCount][altCount];
        for (int i = 0; i < opiCount; i++) {
            Opinion opinion = opinions.get(i);
            try {
                DataInputStream ins = new DataInputStream(new ByteArrayInputStream(opinion.getData()));
                for (int j = 0; j < altCount; j++) {
                    int alt = ins.readInt();
                    matrix[i][alt - 1] = j + 1;
                }
                ins.close();
            } catch (IOException ignored) {
                return new SimpleResponse(ResponseType.ERROR, "Ошибка в данных для расчета!");
            }
        }

        for (int i = 0; i < opiCount; i++) {
            for (int j = 0; j < altCount; j++) {
                matrix[i][j] = altCount - matrix[i][j];
            }
        }

        int [] sums = new int[altCount];
        int total = 0;
        for (int i = 0; i < opiCount; i++) {
            for (int j = 0; j < altCount; j++) {
                sums[j] += matrix[i][j];
                total += matrix[i][j];
            }
        }

        Solution solution = new Solution();
        Set<Solution.Item> items = solution.getItems();
        solution.setTitle(examination.getTitle());
        solution.setDescription(examination.getDescription());
        for (int i = 0; i < altCount; i++) {
            Alternative alt = alts.get(i+1);
            double w = (double) sums[i] / total;
            items.add(new Solution.Item(w, alt.getDescription(), alt.getOrdinal()));
        }

        EntityResponse response = new EntityResponse();
        response.setEntity(solution);
        return response;
    }
}

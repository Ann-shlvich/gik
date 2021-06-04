package by.bank.server.core;

import by.bank.common.service.RequestType;

import java.util.EnumMap;

public class ProcessorManager {

    private final EnumMap<RequestType, Processor> procs = new EnumMap<>(RequestType.class);

    public Processor getRequestProcessor(RequestType requestType) {
        if (procs.containsKey(requestType)) {
            return procs.get(requestType);
        }
        return createProcessor(requestType);
    }

    public void shutdown() {
        procs.clear();
    }

    private Processor createProcessor(RequestType requestType) {
        Processor processor = null;
        switch (requestType) {
            case LOGIN:
            case LOGOUT:
            case PASSWORD:
                processor = new AuthProcessor();
                procs.put(RequestType.LOGIN, processor);
                procs.put(RequestType.LOGOUT, processor);
                procs.put(RequestType.PASSWORD, processor);
                break;
            case USER_ALL:
            case USER_GET:
            case USER_CREATE:
            case USER_UPDATE:
            case USER_DELETE:
                processor = new UserProcessor();
                procs.put(RequestType.USER_ALL, processor);
                procs.put(RequestType.USER_GET, processor);
                procs.put(RequestType.USER_CREATE, processor);
                procs.put(RequestType.USER_UPDATE, processor);
                procs.put(RequestType.USER_DELETE, processor);
                break;
            case EXAMINATION_ALL:
            case EXAMINATION_GET:
            case EXAMINATION_CREATE:
            case EXAMINATION_UPDATE:
            case EXAMINATION_DELETE:
                processor = new ExaminationProcessor();
                procs.put(RequestType.EXAMINATION_ALL, processor);
                procs.put(RequestType.EXAMINATION_GET, processor);
                procs.put(RequestType.EXAMINATION_CREATE, processor);
                procs.put(RequestType.EXAMINATION_UPDATE, processor);
                procs.put(RequestType.EXAMINATION_DELETE, processor);
                break;
            case OPINION_GET:
            case OPINION_UPDATE:
            case OPINION_DELETE:
                processor = new OpinionProcessor();
                procs.put(RequestType.OPINION_GET, processor);
                procs.put(RequestType.OPINION_UPDATE, processor);
                procs.put(RequestType.OPINION_DELETE, processor);
                break;
            case SOLUTION:
                processor = new SolutionProcessor();
                break;
            default:
                return null;
        }
        procs.put(requestType, processor);
        return processor;
    }
}

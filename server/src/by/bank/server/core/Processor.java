package by.bank.server.core;

import by.bank.common.service.Request;
import by.bank.common.service.Response;

public interface Processor {

    Response process(Request request);
}

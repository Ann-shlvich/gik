package by.bank.client.service;

import by.bank.common.entity.*;
import by.bank.common.service.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Service {

    private Socket socket;
    private ObjectInputStream ins;
    private ObjectOutputStream outs;

    private User currentUser;

    public Service() {

    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            outs = new ObjectOutputStream(socket.getOutputStream());
            ins = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ignored) {
            socket = null;
            ins = null;
            outs = null;
            return false;
        }
        return socket.isConnected();
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException ignored) {
            }
            socket = null;
            ins = null;
            outs = null;
            currentUser = null;
        }
    }

    public boolean login(String login, String password) throws ServiceException {
        currentUser = null;
        if (!isConnected()) {
            return false;
        }
        AuthRequest request = new AuthRequest(login, encodePassword(password));
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
        currentUser = (User) ((EntityResponse) response).getEntity();
        return true;
    }

    public void logout() throws ServiceException {
        if (!isConnected()) {
            return;
        }
        Response response = doRequest(new AuthRequest());
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse)response).getMessage());
        }
        currentUser = null;
    }

    public void changePassword(String oldPassword, String newPassword) throws ServiceException {
        if (!isLogged()) {
            throw new ServiceException("Вход не выполнен!");
        }
        final String oldPwd = encodePassword(oldPassword);
        final String newPwd = encodePassword(newPassword);
        Response response = doRequest(new AuthRequest(currentUser.getLogin(), oldPwd, newPwd));
        throw new ServiceException(((SimpleResponse) response).getMessage());
    }

    public List<User> getUsers() throws ServiceException{
        Response response = doRequest(new EmptyRequest(RequestType.USER_ALL));
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
        EntityListResponse listResponse = (EntityListResponse) response;
        return (List<User>) listResponse.getEntities();
    }

    public void createUser(User user) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.USER_CREATE);
        user.setPassword(encodePassword(user.getLogin()));
        request.setEntity(user);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public void updateUser(User user) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.USER_UPDATE);
        user.setPassword(user.getPassword().isEmpty() ? "" : encodePassword(user.getPassword()));
        request.setEntity(user);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public void deleteUser(User user) throws ServiceException {
        if (user.equals(currentUser)) {
            throw new ServiceException("Невозможно удалить самого себя!");
        }
        EntityRequest request = new EntityRequest(RequestType.USER_DELETE);
        request.setEntity(user);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public List<Examination> getExaminations() throws ServiceException{
        Response response = doRequest(new EmptyRequest(RequestType.EXAMINATION_ALL));
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
        EntityListResponse listResponse = (EntityListResponse) response;
        return (List<Examination>) listResponse.getEntities();
    }

    public Examination getExamination(Examination examination) throws ServiceException{
        EntityRequest request = new EntityRequest(RequestType.EXAMINATION_GET);
        request.setEntity(examination);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
        return (Examination) ((EntityResponse) response).getEntity();
    }

    public void createExamination(Examination examination) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.EXAMINATION_CREATE);
        request.setEntity(examination);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public void updateExamination(Examination examination) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.EXAMINATION_UPDATE);
        request.setEntity(examination);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public void deleteExamination(Examination examination) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.EXAMINATION_DELETE);
        request.setEntity(examination);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public Opinion getOpinion(Opinion opinion) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.OPINION_GET);
        request.setEntity(opinion);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
        return (Opinion) ((EntityResponse) response).getEntity();
    }

    public void saveOpinion(Opinion opinion) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.OPINION_UPDATE);
        request.setEntity(opinion);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public void deleteOpinion(Opinion opinion) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.OPINION_DELETE);
        request.setEntity(opinion);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse) response).getMessage());
        }
    }

    public Solution calculate(Examination expertise) throws ServiceException {
        EntityRequest request = new EntityRequest(RequestType.SOLUTION);
        request.setEntity(expertise);
        Response response = doRequest(request);
        if (response.getResponseType() == ResponseType.ERROR) {
            throw new ServiceException(((SimpleResponse)response).getMessage());
        }
        return (Solution) ((EntityResponse) response).getEntity();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public boolean isLogged() {
        return currentUser != null;
    }

    public String getUserFullName() {
        if (!isLogged()) {
            return "";
        }
        return currentUser.getFullName();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Role getRole() {
        return currentUser != null ? currentUser.getRole() : Role.NONE;
    }

    private String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException ignored) {
            return password;
        }
    }

    private Response doRequest(Request request) {
        try {
            outs.writeObject(request);
            return (Response) ins.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
            return new SimpleResponse(ResponseType.ERROR, "Ошибка отправки/получения данных");
        }
    }
}

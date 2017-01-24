package net.codepick.mindlink.dao.exception;

public class ObjectNotExistException extends Exception {
    private String objectName;

    public ObjectNotExistException(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }
}

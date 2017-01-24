package net.codepick.mindlink.dao.exception;

public class ObjectAlreadyExistException extends Exception {
    private Object existingObject;

    public ObjectAlreadyExistException(Object existingObject) {
        this.existingObject = existingObject;
    }

    public Object getExistingObject() {
        return existingObject;
    }
}

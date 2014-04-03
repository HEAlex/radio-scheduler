package ua.edu.sumdu.model;

public class SimpleResponseImpl implements SimpleResponse {

    boolean success = false;

    public SimpleResponseImpl() {
    }

    public SimpleResponseImpl(boolean success) {
        this.success = success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }
}

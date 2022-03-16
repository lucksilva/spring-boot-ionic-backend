package com.lcorreia.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{
    private static final long serialVersionUID = 1L;

    private List<FieldMassage> list = new ArrayList<>();

    public ValidationError(Integer status, String msg, long timeStamp) {
        super(status, msg, timeStamp);
    }

    public List<FieldMassage> getErrors() {
        return list;
    }

    public void addError(String fielName, String massage) {
        list.add(new FieldMassage(fielName, massage));
    }
}

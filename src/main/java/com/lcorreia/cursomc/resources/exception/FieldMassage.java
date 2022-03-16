package com.lcorreia.cursomc.resources.exception;

import java.io.Serializable;

public class FieldMassage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName;
    private String massage;

    public FieldMassage(){

    }

    public FieldMassage(String fieldName, String massage) {
        this.fieldName = fieldName;
        this.massage = massage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}

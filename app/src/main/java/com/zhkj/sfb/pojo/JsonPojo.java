package com.zhkj.sfb.pojo;

/**
 * Created by Administrator on 2017/3/23.
 */

public class JsonPojo {
    private String displayFieldName;
    private Object fieldAliases;
    private String geometryType;
    private Object spatialReference;
    private Object fields;
    private Object features;

    public String getDisplayFieldName() {
        return displayFieldName;
    }

    public void setDisplayFieldName(String displayFieldName) {
        this.displayFieldName = displayFieldName;
    }

    public Object getFieldAliases() {
        return fieldAliases;
    }

    public void setFieldAliases(Object fieldAliases) {
        this.fieldAliases = fieldAliases;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public Object getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(Object spatialReference) {
        this.spatialReference = spatialReference;
    }

    public Object getFields() {
        return fields;
    }

    public void setFields(Object fields) {
        this.fields = fields;
    }

    public Object getFeatures() {
        return features;
    }

    public void setFeatures(Object features) {
        this.features = features;
    }
}

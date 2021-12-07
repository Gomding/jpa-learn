package com.learn.jpa.chapter14;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// 전체에 적용되되는 컨버터
// @Converter(autoApply = true)
public class GlobalConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData);
    }
}

package com.mongs.play.core.utils;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static Object getField(Object obj, String fieldName) {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                boolean isAccessible = field.canAccess(obj);
                field.setAccessible(true);

                try {
                    return field.get(obj);
                } catch (IllegalAccessException ignored) {
                } finally {
                    field.setAccessible(isAccessible);
                }
            }
        }

        return null;
    }

    public static Object setField(Object obj, String fieldName, Object value) {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                boolean isAccessible = field.canAccess(obj);
                field.setAccessible(true);

                try {
                    field.set(obj, value);
                } catch (IllegalAccessException ignored) {
                } finally {
                    field.setAccessible(isAccessible);
                }
            }
        }

        return obj;
    }

    public static Object setFields(Object obj, Object targetObj) {
        Field[] targetFields = targetObj.getClass().getDeclaredFields();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field targetField : targetFields) {
            for (Field field : fields) {

                String targetFieldName = targetField.getName();
                String fieldName = field.getName();

                if (targetFieldName.equals(fieldName)) {
                    boolean isFieldAccessible = field.canAccess(obj);
                    boolean isTargetFieldAccessible = targetField.canAccess(targetObj);
                    field.setAccessible(true);
                    targetField.setAccessible(true);

                    try {
                        Object fieldValue = field.get(obj);
                        targetField.set(targetObj, fieldValue);
                    } catch (IllegalAccessException ignored) {
                    } finally {
                        field.setAccessible(isFieldAccessible);
                        targetField.setAccessible(isTargetFieldAccessible);
                    }

                    break;
                }
            }
        }

        return targetObj;
    }
}

package com.mebitech.robe.persistence.api.converter;

import com.mebitech.robe.common.converter.ValueConverter;
import com.mebitech.robe.persistence.api.criteria.Transformer;
import com.mebitech.robe.persistence.api.criteria.cache.EntityMeta;
import com.mebitech.robe.persistence.api.criteria.cache.EntityMetaFinder;
import com.mebitech.robe.persistence.api.criteria.cache.FieldMeta;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Converter {

    /**
     *
     * @param eClass
     * @param tClass
     * @param finder
     * @param fieldNames
     * @param datum
     * @param <T>
     * @return
     */
    public static <T> List<T> convert(Class<?> eClass, Class<T> tClass, EntityMetaFinder finder, List<String> fieldNames, List<Object[]> datum) {
        if(tClass.getName().equals(Map.class.getName())) { // Map Transform
            return (List<T>)convertMap(eClass, finder, fieldNames, datum);
        } else { // Dto Transform
            return convertDto(tClass, finder, fieldNames, datum);
        }
    }

    /**
     *
     * @param eClass
     * @param tClass
     * @param finder
     * @param fieldNames
     * @param data
     * @param <T>
     * @return
     */
    public static <T> T convert(Class<?> eClass, Class<T> tClass, EntityMetaFinder finder, List<String> fieldNames, Object[] data) {
        if(tClass.getName().equals(Map.class.getName())) { // Map Transform
            return (T) convertMap(finder.getEntityMeta(eClass), fieldNames, data);
        } else { // Dto Transform
            return convertDto(tClass, finder.getEntityMeta(eClass), fieldNames, data);
        }
    }

    /**
     *
     * @param transformClass
     * @param finder
     * @param fieldNames
     * @param datum
     * @return
     */
    public static List<Map<String, Object>> convertMap(Class<?> transformClass, EntityMetaFinder finder, List<String> fieldNames, List<Object[]> datum) {
        EntityMeta entityMeta = Transformer.CachedEntity.getEntityMeta(transformClass, finder, false);
        return convertMap(entityMeta, fieldNames, datum);
    }

    /**
     *
     * @param transformClass
     * @param finder
     * @param fieldNames
     * @param datum
     * @param <T>
     * @return
     */
    public static <T> List<T> convertDto(Class<T> transformClass, EntityMetaFinder finder, List<String> fieldNames,  List<Object[]> datum) {
        EntityMeta entityMeta = Transformer.CachedEntity.getEntityMeta(transformClass, finder, true);
        return convertDto(transformClass, entityMeta, fieldNames, datum);
    }

    /**
     * @param entityMeta
     * @param fieldNames
     * @param datum
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> convertMap(EntityMeta entityMeta, List<String> fieldNames,  List<Object[]> datum) {
        List<Map<String, Object>> entityList = new LinkedList<>();
        for (Object[] row: datum) {
            entityList.add(convertMap(entityMeta, fieldNames, row));
        }
        return entityList;
    }

    /**
     *
     * @param transformClass
     * @param entityMeta
     * @param fieldNames
     * @param datum
     * @param <T>
     * @return
     */
    public static <T> List<T> convertDto(Class<T> transformClass, EntityMeta entityMeta, List<String> fieldNames,  List<Object[]> datum) {
        List<T> entityList = new LinkedList<>();
        for (Object[] row: datum) {
            entityList.add(convertDto(transformClass, entityMeta, fieldNames, row));
        }
        return entityList;
    }

    /**
     *
     * @param transformClass
     * @param transformMeta
     * @param fieldNames
     * @param row
     * @param <T>
     * @return
     */
    public static <T> T convertDto(Class<T> transformClass, EntityMeta transformMeta, List<String> fieldNames, Object[] row){
        try {
            T dto = transformClass.newInstance();
            for (int i = 0; i < row.length; i++) {
                FieldMeta transformFieldMeta = transformMeta.getFieldMap().get(fieldNames.get(i));
                Object value = row[i];
                value = ValueConverter.convert(transformFieldMeta.getField().getType(), value);
                transformFieldMeta.getField().set(dto, value);
            }
            return dto;
        }catch (IllegalAccessException e) {
            // TODO change it with core exception.
            throw new RuntimeException(transformClass.getName() + " constructor is private ! ", e);
        } catch (InstantiationException e) {
            // TODO change it with core exception.
            throw new RuntimeException("Couldn't create an instanc from " + transformClass.getName() + "  class ! ", e);
        }
    }

    /**
     * @param entityMeta
     * @param fieldNames
     * @param row
     * @return
     */
    public static Map<String, Object> convertMap(EntityMeta entityMeta, List<String> fieldNames, Object[] row){
            Map<String, Object> objectMap = new LinkedHashMap<>();
            for (int i = 0; i < row.length; i++) {
                Object resultValue = row[i];
                FieldMeta field = entityMeta.getFieldMap().get(fieldNames.get(i));
                Object value = ValueConverter.convert(field.getField().getType(), resultValue);
                objectMap.put(fieldNames.get(i), value);
            }
            return objectMap;
    }
}

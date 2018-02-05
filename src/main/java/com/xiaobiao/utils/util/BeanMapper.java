package com.xiaobiao.utils.util;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author wuxiaobiao
 * @create 2017-12-08 15:37
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
public class BeanMapper {

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    private BeanMapper() {
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        ArrayList destinationList = new ArrayList();
        Iterator var3 = sourceList.iterator();

        while(var3.hasNext()) {
            Object sourceObject = var3.next();
            Object destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }

        return destinationList;
    }

    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }
}

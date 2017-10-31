package com.oxchains.bitcoin.util;

import java.util.AbstractList;
import java.util.List;
import java.util.Map;

/**
 * @Author oxchains
 * @Time 2017-10-30 9:49
 * @Name ListMapWrapper
 * @Desc:
 */
public abstract class ListMapWrapper<T> extends AbstractList<T> {
    public final List<Map> list;

    public ListMapWrapper(List<Map> list) {
        this.list = list;
    }

    protected abstract T wrap(Map m);

    @Override
    public T get(int index) {
        return wrap(list.get(index));
    }

    @Override
    public int size() {
        return list.size();
    }
}

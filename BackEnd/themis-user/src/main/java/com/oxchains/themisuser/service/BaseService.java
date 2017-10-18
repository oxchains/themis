package com.oxchains.themisuser.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @Author oxchains
 * @Time 2017-10-12 17:31
 * @Name BaseService
 * @Desc:
 */
public abstract class BaseService {
    protected final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    protected final Gson simpleGson = new Gson();
}

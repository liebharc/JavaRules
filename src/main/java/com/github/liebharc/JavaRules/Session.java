package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;

import java.util.List;
import java.util.function.Predicate;

public interface Session {
    <T> List<T> findAll(Class<T> clazz);

    <T> List<T> findAll(Class<T> clazz, Predicate<? super T> predicate);

    boolean notExistsToken(Predicate<Token> predicate);

    void insert(Object obj);

    Logger getLogger();

    void insertToken(String type, long id);

    void insertDebugToken(String missedClassesAggregation);

    void insertDebugToken(String missedClassesAggregation, long id);

    DataAccess getDataAccess();

    ReportStore getReportStore();
}

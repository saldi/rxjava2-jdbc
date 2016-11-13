package org.davidmoten.rx.jdbc;

import java.util.ArrayList;
import java.util.List;

import com.github.davidmoten.guavamini.Lists;
import com.github.davidmoten.guavamini.Preconditions;

import io.reactivex.Flowable;

public class SelectBuilder {

    private final String sql;
    private final SqlInfo sqlInfo;
    private List<Object> list = null;

    private Flowable<List<Object>> parameters = null;

    public SelectBuilder(String sql) {
        this.sql = sql;
        this.sqlInfo = SqlInfo.parse(sql);
    }

    public SelectBuilder parameters(Flowable<List<Object>> parameters) {
        Preconditions.checkArgument(list ==null);
        if (this.parameters == null)
            this.parameters = parameters;
        else
            this.parameters = this.parameters.concatWith(parameters);
        return this;
    }

    public SelectBuilder parameterList(List<Object> values) {
        Preconditions.checkArgument(list ==null);
        if (this.parameters == null)
            this.parameters = Flowable.just(values);
        else
            this.parameters = this.parameters.concatWith(Flowable.just(values));
        return this;
    }

    public SelectBuilder parameterList(Object... values) {
        Preconditions.checkArgument(list ==null);
        if (this.parameters == null)
            this.parameters = Flowable.just(Lists.newArrayList(values));
        else
            this.parameters = this.parameters.concatWith(Flowable.just(Lists.newArrayList(values)));
        return this;
    }

    public SelectBuilder parameter(String name, Object value) {
        Preconditions.checkArgument(parameters == null);
        if (list == null){
            list = new ArrayList<>();
        }
        this.list.add(new Parameter(name, value));
        return this;
    }

    public SelectBuilder parameters(Object... values) {
        return parameters(Flowable.fromArray(values));
    }

}

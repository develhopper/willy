package ir.code4life.willy.http.models;

import java.util.List;

public class DataResponse<T>{
    public List<T> items;
    public String bookmark;
}

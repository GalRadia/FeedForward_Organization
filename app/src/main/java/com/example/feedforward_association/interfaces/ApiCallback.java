package com.example.feedforward_association.interfaces;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String error);

}

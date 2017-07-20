package ru.yamblz.weather.ui.base;

public interface MvpPresenter<V extends MvpView> {
    void onAttach(V view);
    void onDetach();
}

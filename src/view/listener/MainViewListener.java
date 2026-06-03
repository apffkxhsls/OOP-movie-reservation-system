package view.listener;

import model.Movie;

public interface MainViewListener {
    void onMovieSelected(Movie movie);
    void onHistoryButtonClicked();
} 
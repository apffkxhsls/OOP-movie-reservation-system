package model;

import model.theater.ImaxTheater;
import model.theater.NormalTheater;
import model.theater.Theater;
import java.util.ArrayList;

public class DummyData {

    public static ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("인터스텔라", "SF",   14000, 169, "12세 이상"));
        movies.add(new Movie("국제시장",   "드라마", 13000, 169, "12세 이상"));
        movies.add(new Movie("군체",       "공포",  12000, 169, "12세 이상"));
        movies.add(new Movie("범죄도시4",  "액션",  13000, 169, "12세 이상"));
        return movies;
    }

    public static ArrayList<Theater> getTheaters() {
        ArrayList<Theater> theaters = new ArrayList<>();
        theaters.add(new NormalTheater("1관", 5, 8));
        theaters.add(new NormalTheater("2관", 5, 8));
        theaters.add(new ImaxTheater("IMAX관", 6, 10));
        return theaters;
    }

    public static ArrayList<ShowInfo> getShowInfos() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<Theater> theaters = getTheaters();

        ArrayList<ShowInfo> showInfos = new ArrayList<>();

        // 인터스텔라
        showInfos.add(new ShowInfo(movies.get(0), theaters.get(0), "2026.06.01(일) 14:30", 40));
        showInfos.add(new ShowInfo(movies.get(0), theaters.get(2), "2026.06.01(일) 18:00", 60));

        // 국제시장
        showInfos.add(new ShowInfo(movies.get(1), theaters.get(0), "2026.06.01(일) 11:00", 40));
        showInfos.add(new ShowInfo(movies.get(1), theaters.get(1), "2026.06.01(일) 16:00", 40));

        // 군체
        showInfos.add(new ShowInfo(movies.get(2), theaters.get(1), "2026.06.01(일) 13:00", 40));
        showInfos.add(new ShowInfo(movies.get(2), theaters.get(2), "2026.06.01(일) 20:00", 60));

        // 범죄도시4
        showInfos.add(new ShowInfo(movies.get(3), theaters.get(0), "2026.06.01(일) 19:00", 40));
        showInfos.add(new ShowInfo(movies.get(3), theaters.get(2), "2026.06.01(일) 15:00", 60));

        return showInfos;
    }
}
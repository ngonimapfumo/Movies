package zw.co.nm.movies.api.responses;

import java.util.ArrayList;

public class GetMoviesResponse {
    String status;
    String status_message;
    Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        public int movie_count;
        public int limit;
        public int page_number;
        public ArrayList<Movie> movies;
    }

    public class Meta {
        public int server_time;
        public String server_timezone;
        public int api_version;
        public String execution_time;
    }

    public class Movie {
        public int id;
        public String url;
        public String imdb_code;
        public String title;
        public String title_english;
        public String title_long;
        public String slug;
        public int year;
        public double rating;
        public int runtime;
        public ArrayList<String> genres;
        public String summary;
        public String description_full;
        public String synopsis;
        public String yt_trailer_code;
        public String language;
        public String mpa_rating;
        public String background_image;
        public String background_image_original;
        public String small_cover_image;
        public String medium_cover_image;
        public String large_cover_image;
        public String state;
        public ArrayList<Torrent> torrents;
        public String date_uploaded;
        public int date_uploaded_unix;
    }

    public class Root {
        public String status;
        public String status_message;
        public Data data;
        public Meta meta;
    }

    public class Torrent {
        public String url;
        public String hash;
        public String quality;
        public String type;
        public int seeds;
        public int peers;
        public String size;
        public int size_bytes;
        public String date_uploaded;
        public int date_uploaded_unix;
    }

}

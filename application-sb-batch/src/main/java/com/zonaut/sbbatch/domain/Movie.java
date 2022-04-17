package com.zonaut.sbbatch.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Movie {

    private String title;
    private long year;
    private List<String> cast;
    private List<String> genres;

}
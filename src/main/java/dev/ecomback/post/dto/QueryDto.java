package dev.ecomback.post.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class QueryDto {
    String category;
    String creator;
    LocalDate dateCreated;
    LocalDate dateFrom;
    LocalDate dateTo;

    List<String> materials;
    String provenance;
    String query;
    String title;

}



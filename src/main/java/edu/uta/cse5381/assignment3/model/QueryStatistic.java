package edu.uta.cse5381.assignment3.model;

import lombok.Data;

@Data
public class QueryStatistic {
    String query;
    long executionTime;
    long executionCount;
}

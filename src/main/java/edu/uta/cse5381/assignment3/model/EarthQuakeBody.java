package edu.uta.cse5381.assignment3.model;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class EarthQuakeBody {

    Page<EarthQuake> earthQuakes;
    List<QueryStatistic> queryStatistics;

}


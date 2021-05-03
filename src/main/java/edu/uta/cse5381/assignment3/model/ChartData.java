package edu.uta.cse5381.assignment3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartData {
    private String name;
    private int value;
}

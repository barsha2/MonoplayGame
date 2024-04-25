package com.barsha.monopolygame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTable {
    private int         placeValue;
    private String      place;
    private double      buyPrice;
    private double      rentPrice;
}

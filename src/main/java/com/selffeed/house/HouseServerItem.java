package com.selffeed.house;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HouseServerItem {
    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private String lastStatusName;

}

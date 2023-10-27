package com.selffeed.house;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HouseServerData {
    @Setter
    @Getter
    private int floor;

    @Setter
    @Getter
    private HouseServerItem[] list;

}

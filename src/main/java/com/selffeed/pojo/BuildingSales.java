package com.selffeed.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BuildingSales {
    int bs_id;
    int sold;
    int unsold;
    double percent;
    int b_id;
    Date ts;
}

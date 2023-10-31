package com.selffeed.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Building {
    int b_id;
    String title;
    int preSellId;
    int ysProjectId;
    int fybId;
    String buildBranch;
    BuildingSales[] buildingSales;
    Date ts;

}

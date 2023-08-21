package com.selffeed.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Feed {
    int f_id;
    String title;
    String url;
    Date date;


}

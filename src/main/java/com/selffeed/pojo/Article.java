package com.selffeed.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Article {
    int a_id;
    int f_id;
    String title;
    String auther;
    String url;
    String content;
    int likes;
    Date ts;


}

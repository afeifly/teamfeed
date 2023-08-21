package com.selffeed.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ArticleComment {
    int id;
    String nickname;
    int iconIndex;
    String content;
    Date ts;
    int fk_a_id;
    int fk_u_id;


}

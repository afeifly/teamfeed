package com.selffeed.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    int u_id;
    String username;
    String nickname;
    int iconIndex;
    int attack;
    String psw;


}

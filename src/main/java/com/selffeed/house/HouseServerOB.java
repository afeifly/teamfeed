package com.selffeed.house;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class HouseServerOB {
    @Setter
    @Getter
    private int status;

    @Setter
    @Getter
    private HouseServerData[] data;

    public int checkSellStatus(){
        int result = -1;
        //TODO return 88 when 88%
        int wholeSize = 0;
        int virginSize = 0;
        if(data.length>0){
            for(HouseServerData data : data){
                HouseServerItem[] list = data.getList();
                wholeSize += list.length;
                for(HouseServerItem item : list){
                    if(item.getLastStatusName().equals("期房待售")){
                        virginSize++;
                    }
                }
            }
        }
        log.info("sell statu: "+virginSize + " / "+ wholeSize);
        return result;
    }

}

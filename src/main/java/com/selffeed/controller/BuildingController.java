package com.selffeed.controller;

import com.selffeed.house.HouseServerOB;
import com.selffeed.house.PostUtil;
import com.selffeed.model.dao.BuildingDao;
import com.selffeed.model.dao.FeedDao;
import com.selffeed.pojo.Building;
import com.selffeed.pojo.BuildingSales;
import com.selffeed.pojo.Feed;
import com.selffeed.util.FeedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@Slf4j
@ComponentScan("com.selffeed.model.dao")
@ComponentScan("com.selffeed.house")
public class BuildingController {

    @Autowired
    BuildingDao buildingDao;

    @Autowired
    PostUtil postUtil;


    @PostMapping("/api/buildingcheck")
    public String checkBuilding(@RequestBody Building building) {
        int result = 0;
        //TODO try get sales info
        HouseServerOB tmp = postUtil.checkUrl(building.getPreSellId(),
                building.getYsProjectId(),
                String.valueOf(building.getFybId()),
                building.getBuildBranch());
        double percent = tmp.checkSellStatus();
        BuildingSales bs = BuildingSales.builder()
                .b_id(building.getB_id())
                .sold(tmp.getWholeSize() - tmp.getVirginSize())
                .unsold(tmp.getVirginSize())
                .percent(percent).build();

        try {
            result = buildingDao.addSale(bs);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return "result = " + bs.toString();
    }

    @PostMapping("/api/building")
    public String addBuiding(@RequestBody Building building) {

        int result = 0;
        try {
            result = buildingDao.add(
                    building.getTitle(),
                    building.getPreSellId(),
                    building.getYsProjectId(),
                    building.getFybId(),
                    building.getBuildBranch());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info("Save building " + building.toString() + " result: " + result);
        return "name = " + building.toString();
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "URL not available");

    }
    @PostMapping("/api/buildings")
    public String allBuilding(){
        //TODO

        return "";
    }

}

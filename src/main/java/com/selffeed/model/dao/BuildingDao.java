package com.selffeed.model.dao;

import com.selffeed.pojo.Building;
import com.selffeed.pojo.BuildingSales;
import com.selffeed.pojo.Feed;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public interface BuildingDao {
    public int add(String title, int preSellId, int ysProjectId, int fybId, String buildBranch)
            throws SQLException;


    public int addSale(BuildingSales bs)
            throws SQLException;

    public void delete(int id)
            throws SQLException;
    public Building getBuilding(int id)
            throws SQLException;
    public List<Building> getBuildings()
            throws SQLException;

//    public int updateDate(int id,Date date) throws SQLException ;
}

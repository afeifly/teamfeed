package com.selffeed.model.dao;

import com.selffeed.pojo.Building;
import com.selffeed.pojo.BuildingSales;
import com.selffeed.pojo.Feed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class BuildingDaoImp implements BuildingDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int add(String title,
                   int preSellId,
                   int ysProjectId,
                   int fybId,
                   String buildBranch) throws SQLException {

        log.debug("Add Builiding");
        String sql = "INSERT INTO buildings " +
                "(title, pre_sell_id,ys_project_id,fyb_id,build_branch) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setString(1, title.trim());
            ps.setInt(2, preSellId);
            ps.setInt(3, ysProjectId);
            ps.setInt(4, fybId);
            ps.setString(5, buildBranch.trim());

            return ps;
        });

    }

    @Override
    public int addSale(BuildingSales bs)
            throws SQLException{
        log.debug("Add Sales");
        String sql = "INSERT INTO building_sales " +
                "(sold, unsold, percent, fk_b_id, ts) " +
                "VALUES (?, ?, ?, ?, now()) where ";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setInt(1, bs.getSold());
            ps.setInt(2, bs.getUnsold());
            ps.setDouble(3, bs.getPercent());
            ps.setInt(4, bs.getB_id());
            return ps;
        });
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Building getBuilding(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Building> getBuildings() throws SQLException {
        List<Building> list = jdbcTemplate.query("select * from buildings",
                new RowMapper<Building>() {
                    @Override
                    public Building mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return Building.builder()
                                .b_id(rs.getInt(1))
                                .title(rs.getString(2).trim())
                                .preSellId(rs.getInt(3))
                                .ysProjectId(rs.getInt(4))
                                .fybId(rs.getInt(5))
                                .buildBranch(rs.getString(6).trim())
                                .build();
                    }
                });
        log.debug("Get building count = "+list.size());
        return list;
    }



}

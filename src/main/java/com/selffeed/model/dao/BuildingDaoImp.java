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

    public int update(Building building) throws SQLException{

        log.debug("Update Building");
        String sql = "update buildings set sold = ?, " +
                "unsold = ?, percent = ?, ts=now() where id = ? ";
        return jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql);
            ps.setInt(1,building.getSold());
            ps.setInt(2,building.getUnsold());
            ps.setDouble(3,building.getPercent());
            ps.setInt(4, building.getB_id());
            return ps;
        });
    }
    @Override
    public int addSale(BuildingSales bs)
            throws SQLException{
        log.debug("Add Sales");
        String sql = "INSERT INTO building_sales " +
                "(sold, unsold, percent, fk_b_id, ts) " +
                "VALUES (?, ?, ?, ?, now())";
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

        List<BuildingSales> list = jdbcTemplate.query("select sold,unsold,percent,ts " +
                        "from building_sales where fk_b_id="+id+
                        " order by id asc "

                ,
            new RowMapper<BuildingSales>(){
                @Override
                public BuildingSales mapRow(ResultSet rs, int rowNum) throws SQLException {
                    BuildingSales bs = BuildingSales.builder()
                            .sold(rs.getInt(1))
                            .unsold(rs.getInt(2))
                            .percent(rs.getDouble(3))
                            .ts(rs.getTimestamp(4))
                            .build();
                    return bs;
                }
            });
        list.size();
        BuildingSales[] array = new BuildingSales[list.size()];
        for(int i=0;i<list.size();i++){
            array[i] = list.get(i);
        }
        Building building = Building.builder()
                .buildingSales(array).build();
        log.info("Get building: "+ building.toString());
        return building;
    }

    @Override
    public List<Building> getBuildings() throws SQLException {
        List<Building> list = jdbcTemplate.query("select * from buildings",
                new RowMapper<Building>() {
                    @Override
                    public Building mapRow(ResultSet rs, int rowNum) throws SQLException {

                        //try fillAllSales

                        Building building = Building.builder()
                                .b_id(rs.getInt(1))
                                .title(rs.getString(2).trim())
                                .preSellId(rs.getInt(3))
                                .ysProjectId(rs.getInt(4))
                                .fybId(rs.getInt(5))
                                .buildBranch(rs.getString(6).trim())
                                .sold(rs.getInt(7))
                                .unsold(rs.getInt(8))
                                .percent(rs.getDouble(9))
                                .ts(rs.getTimestamp(10))
                                .build();
                        return building;
                    }
                });
        log.debug("Get building count = "+list.size());
        return list;
    }



}

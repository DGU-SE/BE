package com.twofullmoon.howmuchmarket;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {
    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void initializeDatabase() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            String dropFunction = "DROP FUNCTION IF EXISTS calculate_distance";
            statement.execute(dropFunction);

            String createFunction = "CREATE FUNCTION calculate_distance(" +
                    "lat1 DOUBLE, lon1 DOUBLE, lat2 DOUBLE, lon2 DOUBLE) " +
                    "RETURNS DOUBLE DETERMINISTIC BEGIN RETURN 6371 * ACOS(" +
                    "COS(RADIANS(lat2)) * COS(RADIANS(lat1)) * " +
                    "COS(RADIANS(lon1) - RADIANS(lon2)) + SIN(RADIANS(lat2)) * " +
                    "SIN(RADIANS(lat1))); END";
            statement.execute(createFunction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

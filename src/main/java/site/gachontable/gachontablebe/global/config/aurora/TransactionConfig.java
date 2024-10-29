package site.gachontable.gachontablebe.global.config.aurora;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class TransactionConfig {

    private static final String MASTER_DATASOURCE = "masterDataSource";
    private static final String SLAVE_DATASOURCE = "slaveDataSource";

    @Bean(name = MASTER_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public HikariDataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = SLAVE_DATASOURCE)
    @ConfigurationProperties("spring.datasource.slave.hikari")
    public HikariDataSource slaveDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean
    public CustomRoutingDataSource routingDataSource() {
        CustomRoutingDataSource routingDataSource = new CustomRoutingDataSource();
        HashMap<Object, Object> dataSources = new HashMap<>();
        dataSources.put("master", masterDataSource());
        dataSources.put("slave", slaveDataSource());
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        return routingDataSource;
    }

    @Bean
    @Primary
    @DependsOn("routingDataSource")
    public DataSource dataSource(CustomRoutingDataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}

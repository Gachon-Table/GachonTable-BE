package site.gachontable.infra.database.aurora

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
class TransactionConfig {
    @Bean(name = [MASTER_DATASOURCE])
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    fun masterDataSource(): HikariDataSource {
        return DataSourceBuilder.create()
            .type<HikariDataSource>(HikariDataSource::class.java)
            .build()
    }

    @Bean(name = [SLAVE_DATASOURCE])
    @ConfigurationProperties("spring.datasource.slave.hikari")
    fun slaveDataSource(): HikariDataSource {
        val dataSource = DataSourceBuilder.create()
            .type<HikariDataSource>(HikariDataSource::class.java)
            .build()
        dataSource.isReadOnly = true
        return dataSource
    }

    @Bean
    fun routingDataSource(): CustomRoutingDataSource {
        val dataSources = HashMap<Any, Any>().apply {
            put("master", masterDataSource())
            put("slave", slaveDataSource())
        }
        val routingDataSource = CustomRoutingDataSource().apply {
            setTargetDataSources(dataSources)
            setDefaultTargetDataSource(masterDataSource())
        }
        return routingDataSource
    }

    @Bean
    @Primary
    @DependsOn("routingDataSource")
    fun dataSource(routingDataSource: CustomRoutingDataSource): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    companion object {
        private const val MASTER_DATASOURCE = "masterDataSource"
        private const val SLAVE_DATASOURCE = "slaveDataSource"
    }
}

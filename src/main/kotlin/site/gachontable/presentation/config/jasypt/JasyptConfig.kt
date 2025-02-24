package site.gachontable.presentation.config.jasypt

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig {
    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig().apply {
            password = System.getProperty("jasypt.encryptor.password")
            algorithm = "PBEWithMD5AndDES"
            poolSize = 1
        }
        encryptor.setConfig(config)

        return encryptor
    }
}

package cn.sen.lucky.gateway;

import cn.sen.lucky.gateway.model.SysParameterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @Author caosen
 * @data ${DATE}--${TIME}
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(value = {SysParameterConfig.class})
public class OAuthGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthGateWayApplication.class);
    }
}

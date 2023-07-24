package online.devwiki.common.oauth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CommonOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonOauthApplication.class, args);
    }
}

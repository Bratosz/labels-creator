package pl.bratosz.labelscreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.bratosz.labelscreator.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class LabelsCreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabelsCreatorApplication.class, args);
    }

}

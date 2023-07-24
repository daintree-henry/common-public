package online.devwiki.sampleresourceserver.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
public class SampleController {

    @GetMapping()
    public String get() {
        return "{ \"response\": \"Hello I'm Sample Server\" }";
    }
}

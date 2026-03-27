package dockertp1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "TP2 - Faire les actions + nouvelles branches de commit. Victor BONNIN";
    }
}
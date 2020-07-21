package vo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationResponse {
    String token;

    public RegistrationResponse(String token) {
        this.token = token;

    }
}
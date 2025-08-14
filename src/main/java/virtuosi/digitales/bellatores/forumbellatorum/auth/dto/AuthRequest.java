package virtuosi.digitales.bellatores.forumbellatorum.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String usernameOrEmail;
    private String password;
}


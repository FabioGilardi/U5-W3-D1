package FabioGilardi.U5W3D1.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @NotEmpty(message = "email is mandatory")
        @Email
        String email,

        @NotEmpty(message = "password is mandatory")
        String password
) {
}

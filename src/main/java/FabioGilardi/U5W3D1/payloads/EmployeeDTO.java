package FabioGilardi.U5W3D1.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EmployeeDTO(
        @NotEmpty(message = "username is mandatory")
        @Size(min = 3, message = "username length must be over 3 chars")
        String username,

        @NotEmpty(message = "name is mandatory")
        @Size(min = 3, message = "name length must be over 3 chars")
        String name,

        @NotEmpty(message = "surname is mandatory")
        @Size(min = 3, message = "surname length must be over 3 chars")
        String surname,

        @NotEmpty(message = "email is mandatory")
        @Email(message = "you must insert a valid email")
        String email,

        @NotEmpty(message = "password is mandatory")
        String password
) {
}

package FabioGilardi.U5W3D1.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewDeciveDTO(
        @NotEmpty(message = "type is mandatory")
        String type
) {
}

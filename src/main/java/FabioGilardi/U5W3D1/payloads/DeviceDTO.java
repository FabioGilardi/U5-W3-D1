package FabioGilardi.U5W3D1.payloads;

import jakarta.validation.constraints.NotEmpty;

public record DeviceDTO(
        @NotEmpty(message = "status is mandatory")
        String status
) {
}

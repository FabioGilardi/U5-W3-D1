package FabioGilardi.U5W3D1.payloads;

import java.time.LocalDateTime;

public record ErrorDTO(
        String message,
        LocalDateTime timestamp) {
}

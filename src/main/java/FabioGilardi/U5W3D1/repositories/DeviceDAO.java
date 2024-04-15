package FabioGilardi.U5W3D1.repositories;

import FabioGilardi.U5W3D1.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDAO extends JpaRepository<Device, Long> {
}

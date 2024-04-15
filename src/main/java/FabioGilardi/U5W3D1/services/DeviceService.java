package FabioGilardi.U5W3D1.services;

import FabioGilardi.U5W3D1.entities.Device;
import FabioGilardi.U5W3D1.exceptions.BadRequestException;
import FabioGilardi.U5W3D1.exceptions.NotFoundException;
import FabioGilardi.U5W3D1.payloads.DeviceAssignementDTO;
import FabioGilardi.U5W3D1.payloads.DeviceDTO;
import FabioGilardi.U5W3D1.payloads.NewDeciveDTO;
import FabioGilardi.U5W3D1.repositories.DeviceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    @Autowired
    private DeviceDAO deviceDAO;

    @Autowired
    private EmployeeService employeeService;

    public Page<Device> findAll(int number, int size, String sortBY) {
        Pageable pageable = PageRequest.of(number, size, Sort.by(sortBY));
        return deviceDAO.findAll(pageable);
    }

    public Device save(NewDeciveDTO payload) {
        Device newDevice = new Device(payload.type());
        newDevice.setStatus("Available");
        return deviceDAO.save(newDevice);
    }

    public Device findById(long id) {
        return deviceDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    //    DO SOLO LA POSSIBILITA' DI MODIFICARE LO STATO IN QUANTO UN PC NON POTRA' MAI ESSERE RICONVERTITO IN UNO
    // SMARTPHONE E VICEVERSA(QUALSIASI ALTRO DISPOSITIVO IN GENERALE)
    public Device findByIdAndUpdateStatus(long id, DeviceDTO payload) {
        if (!payload.status().toLowerCase().equals("assigned")) {
            Device found = this.findById(id);
            found.setStatus(payload.status());
            found.setEmployee(null);
            deviceDAO.save(found);
            return found;
        } else throw new BadRequestException("Device cannot be updated as assigned in type");
    }

    public void findByIdAndDelete(long id) {
        Device found = this.findById(id);
        deviceDAO.delete(found);
    }

    // DO LA POSSIBILITA' DI ASSEGNARE UN DISPOSITIVO SONO NEL CASO QUESTO SIA AVAILABLE O ANCHE GIA' ASSEGNATO AD UN ALTRO UTENTE, PERCHE' HO PENSATO CHE IN CASO DI UNA RAPIDA RIASSEGNAZIONE FOSSE COMODO IL PASSAGGIO DA UN UTENTE AD UN ALTRO ALTRO SENZA PASSARE PER LO STATO AVAILABLE (SEMPRE PER POSSIBILI IMPREVISTI VARI)
    public Device findByIdAndAssign(long id, DeviceAssignementDTO payload) {
        Device found = this.findById(id);
        if (found.getStatus().toLowerCase().equals("available") || found.getStatus().toLowerCase().equals("assigned")) {
            found.setStatus("Assigned");
            found.setEmployee(employeeService.findById(payload.employeeId()));
            deviceDAO.save(found);
            return found;
        } else throw new BadRequestException("Device is currently " + found.getStatus());
    }
}

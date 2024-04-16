package FabioGilardi.U5W3D1.controllers;

import FabioGilardi.U5W3D1.entities.Device;
import FabioGilardi.U5W3D1.exceptions.BadRequestException;
import FabioGilardi.U5W3D1.payloads.DeviceAssignementDTO;
import FabioGilardi.U5W3D1.payloads.DeviceDTO;
import FabioGilardi.U5W3D1.payloads.NewDeciveDTO;
import FabioGilardi.U5W3D1.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Device> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return deviceService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device save(@RequestBody @Validated NewDeciveDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return deviceService.save(payload);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findById(@PathVariable long id) {
        return deviceService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated DeviceDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return deviceService.findByIdAndUpdateStatus(id, payload);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long id) {
        deviceService.findByIdAndDelete(id);
    }

    @PutMapping("/assignement/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndAssign(@PathVariable long id, @RequestBody DeviceAssignementDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return deviceService.findByIdAndAssign(id, payload);
    }
}

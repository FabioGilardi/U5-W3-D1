package FabioGilardi.U5W3D1.controllers;

import FabioGilardi.U5W3D1.entities.Employee;
import FabioGilardi.U5W3D1.exceptions.BadRequestException;
import FabioGilardi.U5W3D1.payloads.EmployeeDTO;
import FabioGilardi.U5W3D1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping
    private Page<Employee> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return employeeService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    private Employee findById(@PathVariable long id) {
        return employeeService.findById(id);
    }

    @PutMapping("/{id}")
    private Employee findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated EmployeeDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return employeeService.findByIdAndUpdate(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void findByIdAndDelete(@PathVariable long id) {
        employeeService.findByIdAndDelete(id);
    }

    @PostMapping("/upload/{id}")
    public Employee uploadAvatar(@RequestParam("avatar") MultipartFile image, @PathVariable long id) throws IOException {
        return employeeService.uploadImage(image, id);
    }
}

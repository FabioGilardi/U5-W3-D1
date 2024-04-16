package FabioGilardi.U5W3D1.controllers;

import FabioGilardi.U5W3D1.entities.Employee;
import FabioGilardi.U5W3D1.exceptions.BadRequestException;
import FabioGilardi.U5W3D1.payloads.EmployeeDTO;
import FabioGilardi.U5W3D1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/me")
    public Employee getProfile(@AuthenticationPrincipal Employee currentUser) {
        return currentUser;
    }

    @PutMapping("/me")
    public Employee modifyProfile(@AuthenticationPrincipal Employee currentUser,
                                  @RequestBody @Validated EmployeeDTO payload,
                                  BindingResult validation) {
        if (validation.hasErrors())
            throw new BadRequestException(validation.getAllErrors());
        else
            return employeeService.findByIdAndUpdate(currentUser.getId(), payload);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Employee currentUser) {
        employeeService.findByIdAndDelete(currentUser.getId());
    }

    @PostMapping("/me/upload")
    public Employee uploadAvatar(@RequestParam("avatar") MultipartFile image,
                                 @AuthenticationPrincipal Employee currentUser) throws IOException {
        return employeeService.uploadImage(image, currentUser.getId());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Employee> findAll(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return this.employeeService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee findById(@PathVariable long id) {
        return employeeService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated EmployeeDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return employeeService.findByIdAndUpdate(id, payload);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long id) {
        employeeService.findByIdAndDelete(id);
    }

    @PostMapping("/upload/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee uploadAvatarForced(@RequestParam("avatar") MultipartFile image, @PathVariable long id) throws IOException {
        return employeeService.uploadImage(image, id);
    }
}

package FabioGilardi.U5W3D1.controllers;

import FabioGilardi.U5W3D1.entities.Employee;
import FabioGilardi.U5W3D1.exceptions.BadRequestException;
import FabioGilardi.U5W3D1.payloads.EmployeeDTO;
import FabioGilardi.U5W3D1.payloads.UserLoginDTO;
import FabioGilardi.U5W3D1.payloads.UserLoginResponseDTO;
import FabioGilardi.U5W3D1.services.AuthService;
import FabioGilardi.U5W3D1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO payload) {
        return new UserLoginResponseDTO(authService.authUserAndCreateToken(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee save(@RequestBody @Validated EmployeeDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return employeeService.save(payload);
    }
}

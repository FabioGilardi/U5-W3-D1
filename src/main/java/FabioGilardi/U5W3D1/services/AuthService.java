package FabioGilardi.U5W3D1.services;

import FabioGilardi.U5W3D1.entities.Employee;
import FabioGilardi.U5W3D1.exceptions.UnauthorizedException;
import FabioGilardi.U5W3D1.payloads.UserLoginDTO;
import FabioGilardi.U5W3D1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JWTTools jwtTools;

    @Autowired
    PasswordEncoder encoder;

    public String authUserAndCreateToken(UserLoginDTO payload) {
        Employee user = employeeService.findByEmail(payload.email());
        if (encoder.matches(payload.password(), user.getPassword()))
            return jwtTools.createToken(user);
        else
            throw new UnauthorizedException("Password is wrong");
    }

}

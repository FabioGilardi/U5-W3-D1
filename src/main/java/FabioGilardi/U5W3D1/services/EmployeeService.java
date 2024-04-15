package FabioGilardi.U5W3D1.services;

import FabioGilardi.U5W3D1.entities.Employee;
import FabioGilardi.U5W3D1.exceptions.BadRequestException;
import FabioGilardi.U5W3D1.exceptions.NotFoundException;
import FabioGilardi.U5W3D1.payloads.EmployeeDTO;
import FabioGilardi.U5W3D1.repositories.EmployeeDAO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmployeeService {

    @Autowired
    private Cloudinary cloudinaryUploader;
    @Autowired
    private EmployeeDAO employeeDAO;

    public Page<Employee> findAll(int number, int size, String sortBY) {
        Pageable pageable = PageRequest.of(number, size, Sort.by(sortBY));
        return employeeDAO.findAll(pageable);
    }

    public Employee save(EmployeeDTO payload) {
        if (!employeeDAO.existsByEmail(payload.email()) && !employeeDAO.existsByUsername(payload.username())) {
            Employee newEmployee = new Employee(payload.username(), payload.name(), payload.surname(), payload.email(), payload.password());
            return employeeDAO.save(newEmployee);
        } else throw new BadRequestException("email/username has been already taken");
    }

    public Employee findById(long id) {
        return employeeDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    //    HO DATO ALL'UTENTE LA POSSIBILITA' DI POTER CAMBIARE TUTTO TRANNE LA MAIL. IN CASO LO USERNAME DEL PAYLOAD SIA UGUALE A QUELLO DEL FOUND BYPASSA I CONTROLLI DELLA QUERY ALTRIMENTI MI BLOCCHEREBBE L'OPERAZIONE PER IL FATTO CHE IL FOUND è UGUALE A SE STESSO
    public Employee findByIdAndUpdate(long id, EmployeeDTO payload) {
        Employee found = this.findById(id);
        if (found.getEmail().equals(payload.email())) {
            if (found.getUsername().equals(payload.username())) {
                found.setName(payload.name());
                found.setSurname(payload.surname());
                if (!found.getAvatar().contains("cloudinary")) found.setDeafaultAvatar();
            } else if (!employeeDAO.existsByUsername(payload.username())) {
                found.setUsername(payload.username());
                found.setName(payload.name());
                found.setSurname(payload.surname());
                if (!found.getAvatar().contains("cloudinary")) found.setDeafaultAvatar();
            } else throw new BadRequestException("Username " + payload.username() + " is already taken");
            employeeDAO.save(found);
            return found;
        } else throw new BadRequestException("You are not allowed to change the email without permission");
    }

    public void findByIdAndDelete(long id) {
        Employee found = this.findById(id);
        employeeDAO.delete(found);
    }

    public Employee uploadImage(MultipartFile img, long blogPostId) throws IOException {
        Employee found = findById(blogPostId);
        String url = (String) cloudinaryUploader.uploader().upload(img.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(url);
        employeeDAO.save(found);
        return found;
    }

    public Employee findByEmail(String email) {
        return employeeDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("This email: " + email + "has not been found"));
    }
}

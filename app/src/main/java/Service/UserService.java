package Service;


import java.util.List;

import Entity.Student;

/**
 * Created by CharlesGao on 15-08-16.
 */
public interface UserService {

    public List<Student> getStudents() throws Exception;

}

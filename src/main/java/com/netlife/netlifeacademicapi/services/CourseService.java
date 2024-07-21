// package com.netlife.netlifeacademicapi.services;

// import java.util.Set;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.netlife.netlifeacademicapi.models.Course;
// import com.netlife.netlifeacademicapi.models.Course_Student;
// import com.netlife.netlifeacademicapi.models.ErrorResponse;
// import com.netlife.netlifeacademicapi.models.Role;
// import com.netlife.netlifeacademicapi.models.User;
// import com.netlife.netlifeacademicapi.repositories.ICourseRepository;
// import com.netlife.netlifeacademicapi.repositories.IUserRepository;

// @Service
// public class CourseService {

//     @Autowired
//     private IUserRepository userRepository;

//     @Autowired
//     private ICourseRepository courseRepository;

//     public Object createCourse(Course course) {
//         return courseRepository.save(course);
//     }

//     public Object addStudentToCourse(Course_Student courseStudent) {
//         User user = userRepository.findById(courseStudent.getStudent_id()).get();

//         if (user.getRole() != Role.STUDENT) {
//             return ErrorResponse.builder()
//                     .message("User is not a student")
//                     .status(400)
//                     .error("Bad Request")
//                     .path("/courses")
//                     .build();
//         }

//         Course course = courseRepository.findById(courseStudent.getCourse_id()).get();

//         course.setStudents(Set.of(user));

//         return courseRepository.save(course);
//     }
// }

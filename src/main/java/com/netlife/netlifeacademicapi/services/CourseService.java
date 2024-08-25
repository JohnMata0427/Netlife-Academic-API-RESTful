package com.netlife.netlifeacademicapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netlife.netlifeacademicapi.models.Course;
import com.netlife.netlifeacademicapi.models.Course_Student;
import com.netlife.netlifeacademicapi.models.ErrorResponse;
import com.netlife.netlifeacademicapi.models.Role;
import com.netlife.netlifeacademicapi.models.User;
import com.netlife.netlifeacademicapi.repositories.ICourseRepository;
import com.netlife.netlifeacademicapi.repositories.IUserRepository;

@Service
public class CourseService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Transactional
    public Object createCourse(Course course) {

        if (courseRepository.existsById(course.getId())) {
            return ErrorResponse.builder()
                    .message("El curso ya existe")
                    .status(400)
                    .error("Bad Request")
                    .path("/courses")
                    .build();
        }

        if (course.getName().isEmpty() || course.getCategory().isEmpty() || course.getDescription().isEmpty()
                || course.getDuration() == 0 || course.getImageUrl().isEmpty() || course.getModules().isEmpty()
                || course.getObjectives().isEmpty() || course.getSkills().isEmpty() || course.getAttitudes().isEmpty()
                || course.getPresentation().isEmpty() || course.getFinishAt() == null) {
            return ErrorResponse.builder()
                    .message("Todos los campos son requeridos")
                    .status(400)
                    .error("Bad Request")
                    .path("/courses")
                    .build();
        }

        if (course.getTeacher_id().getRole() != Role.TEACHER) {
            return ErrorResponse.builder()
                    .message("El usuario no es un profesor")
                    .status(400)
                    .error("Bad Request")
                    .path("/courses")
                    .build();
        }

        if (!userRepository.existsById(course.getTeacher_id().getId())) {
            return ErrorResponse.builder()
                    .message("El profesor no existe")
                    .status(404)
                    .error("Not Found")
                    .path("/courses")
                    .build();
        }

        if (course.getStudents() != null) {
            for (User student : course.getStudents()) {
                if (!userRepository.existsById(student.getId())) {
                    return ErrorResponse.builder()
                            .message("El estudiante " + student.getEmail() + " no existe")
                            .status(404)
                            .error("Not Found")
                            .path("/courses")
                            .build();
                }
            }
        }

        return courseRepository.save(course);
    }

    @Transactional
    public Object getCourse(String id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Object getCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public Object updateCourse(String id, Course course) {
        Course courseToUpdate;

        try {
            courseToUpdate = courseRepository.findById(id).get();
        } catch (Exception e) {
            return ErrorResponse.builder()
                    .message("Curso no encontrado")
                    .status(404)
                    .error("Not Found")
                    .path("/courses/" + id)
                    .build();
        }

        if (course.getName() != null)
            courseToUpdate.setName(course.getName());
        if (course.getCategory() != null)
            courseToUpdate.setCategory(course.getCategory());
        if (course.getDescription() != null)
            courseToUpdate.setDescription(course.getDescription());
        if (course.getDuration() != 0)
            courseToUpdate.setDuration(course.getDuration());
        if (course.getImageUrl() != null)
            courseToUpdate.setImageUrl(course.getImageUrl());
        if (course.getModules() != null)
            courseToUpdate.setModules(course.getModules());
        if (course.getObjectives() != null)
            courseToUpdate.setObjectives(course.getObjectives());
        if (course.getSkills() != null)
            courseToUpdate.setSkills(course.getSkills());
        if (course.getAttitudes() != null)
            courseToUpdate.setAttitudes(course.getAttitudes());
        if (course.getPresentation() != null)
            courseToUpdate.setPresentation(course.getPresentation());
        if (course.getTeacher_id() != null)
            courseToUpdate.setTeacher_id(course.getTeacher_id());
        if (course.getFinishAt() != null)
            courseToUpdate.setFinishAt(course.getFinishAt());

        return courseRepository.save(courseToUpdate);
    }

    @Transactional
    public boolean deleteCourse(String id) {
        if (!courseRepository.existsById(id))
            return false;
        courseRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Object addStudentToCourse(Course_Student courseStudent) {
        User user = userRepository.findById(courseStudent.getStudent_id()).get();

        if (user.getRole() != Role.STUDENT) {
            return ErrorResponse.builder()
                    .message("El usuario no es un estudiante")
                    .status(400)
                    .error("Bad Request")
                    .path("/courses")
                    .build();
        }

        Course course = courseRepository.findById(courseStudent.getCourse_id()).get();

        if (course.getStudents().contains(user)) {
            return ErrorResponse.builder()
                    .message("El usuario ya está inscrito en el curso")
                    .status(400)
                    .error("Bad Request")
                    .path("/courses")
                    .build();
        }

        course.getStudents().add(user);

        return courseRepository.save(course);
    }
}

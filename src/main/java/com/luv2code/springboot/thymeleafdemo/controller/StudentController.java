package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Student;
import com.luv2code.springboot.thymeleafdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService theStudentService){
        studentService = theStudentService;
    }

    //add mapping for "/list"

    @GetMapping("/")
    public String listEmployee(Model theModel){

        // get the employees from db
        List<Student> theStudents = studentService.findAll();

        //add to spring model
        theModel.addAttribute("students", theStudents);

        return  "list-students";  //html
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel){

        //create model attribute to bind form data
        Student theStudent = new Student();

        theModel.addAttribute("student", theStudent);

        return "student-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("studentId") int theId, Model theModel){

        //get the employee from the service
        Student theStudent = studentService.findById(theId);

        //set employee in the model to prepopulate the form
        theModel.addAttribute("student", theStudent);

        // send over to our form

        return "student-form";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student, Model model) {
        try {
            studentService.save(student);
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("warningMessage", "Ya existe un estudiante con el mismo correo electrónico.");
            return "student-form"; // Reemplaza "your_page" con el nombre real de tu plantilla Thymeleaf
        }
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("studentId") int theId){
        // delete the employee
        studentService.deleteById(theId);
        // redirect to the /employees/list
        return "redirect:/";

    }

}

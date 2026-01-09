package com.example.studentapp.controller;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.studentapp.model.Student;
import com.example.studentapp.repository.StudentRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentRepository studentRepository;

	// List all students with pagination and search
	@GetMapping
	public String listStudents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "name") String sortBy, @RequestParam(required = false) String search,
			@RequestParam(required = false) String course, Model model) {

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
			Page<Student> studentPage;

			// Apply search and filter
			if ((search != null && !search.isEmpty()) || (course != null && !course.isEmpty())) {
				studentPage = studentRepository.searchStudents(search != null && !search.isEmpty() ? search : null,
						course != null && !course.isEmpty() ? course : null, pageable);
				logger.info("Searching students with name: {} and course: {}", search, course);
			} else {
				studentPage = studentRepository.findAll(pageable);
				logger.info("Listing all students - Page: {}", page);
			}

			// Get all courses for filter dropdown
			List<String> courses = studentRepository.findAllDistinctCourses();

			model.addAttribute("students", studentPage.getContent());
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", studentPage.getTotalPages());
			model.addAttribute("totalItems", studentPage.getTotalElements());
			model.addAttribute("sortBy", sortBy);
			model.addAttribute("search", search);
			model.addAttribute("selectedCourse", course);
			model.addAttribute("courses", courses);

			return "students/list";
		} catch (Exception e) {
			logger.error("Error listing students", e);
			model.addAttribute("errorMessage", "Error loading students: " + e.getMessage());
			return "error";
		}
	}

	// Show create form
	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("student", new Student());
		logger.info("Showing create student form");
		return "students/form";
	}

	// Create student with validation
	@PostMapping
	public String createStudent(@Valid @ModelAttribute Student student, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Validation errors while creating student: {}", result.getAllErrors());
			return "students/form";
		}

		try {
			studentRepository.save(student);
			logger.info("Student created successfully: {}", student.getName());
			redirectAttributes.addFlashAttribute("successMessage",
					"Student '" + student.getName() + "' added successfully!");
			return "redirect:/students";
		} catch (Exception e) {
			logger.error("Error creating student", e);
			redirectAttributes.addFlashAttribute("errorMessage", "Error adding student: " + e.getMessage());
			return "redirect:/students";
		}
	}

	// Show edit form
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Student student = studentRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
			model.addAttribute("student", student);
			logger.info("Showing edit form for student ID: {}", id);
			return "students/form";
		} catch (Exception e) {
			logger.error("Error loading student for edit", e);
			redirectAttributes.addFlashAttribute("errorMessage", "Student not found!");
			return "redirect:/students";
		}
	}

	// Update student with validation
	@PostMapping("/update/{id}")
	public String updateStudent(@PathVariable Long id, @Valid @ModelAttribute Student student, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Validation errors while updating student ID {}: {}", id, result.getAllErrors());
			student.setId(id);
			return "students/form";
		}

		try {
			student.setId(id);
			studentRepository.save(student);
			logger.info("Student updated successfully: {} (ID: {})", student.getName(), id);
			redirectAttributes.addFlashAttribute("successMessage",
					"Student '" + student.getName() + "' updated successfully!");
			return "redirect:/students";
		} catch (Exception e) {
			logger.error("Error updating student", e);
			redirectAttributes.addFlashAttribute("errorMessage", "Error updating student: " + e.getMessage());
			return "redirect:/students";
		}
	}

	// Delete student
	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			Student student = studentRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
			String studentName = student.getName();
			studentRepository.deleteById(id);
			logger.info("Student deleted: {} (ID: {})", studentName, id);
			redirectAttributes.addFlashAttribute("successMessage",
					"Student '" + studentName + "' deleted successfully!");
			return "redirect:/students";
		} catch (Exception e) {
			logger.error("Error deleting student", e);
			redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
			return "redirect:/students";
		}
	}
}
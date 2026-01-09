package com.example.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	@Column(nullable = false)
	private String name;

	@NotNull(message = "Age is required")
	@Min(value = 16, message = "Age must be at least 16")
	@Max(value = 100, message = "Age must not exceed 100")
	@Column(nullable = false)
	private Integer age;

	@NotBlank(message = "Course is required")
	@Size(min = 2, max = 100, message = "Course must be between 2 and 100 characters")
	@Column(nullable = false)
	private String course;

	@Email(message = "Please provide a valid email address")
	@Column(unique = true)
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phone;

	// Constructors
	public Student() {
	}

	public Student(String name, Integer age, String course) {
		this.name = name;
		this.age = age;
		this.course = course;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
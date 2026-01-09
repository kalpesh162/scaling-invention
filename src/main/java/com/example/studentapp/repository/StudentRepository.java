package com.example.studentapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.studentapp.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	// Search by name (case insensitive)
	Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

	// Filter by course
	Page<Student> findByCourse(String course, Pageable pageable);

	// Search and filter combined
	Page<Student> findByNameContainingIgnoreCaseAndCourse(String name, String course, Pageable pageable);

	// Get all distinct courses for filter dropdown
	@Query("SELECT DISTINCT s.course FROM Student s ORDER BY s.course")
	List<String> findAllDistinctCourses();

	// Advanced search with multiple criteria
	@Query("SELECT s FROM Student s WHERE "
			+ "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
			+ "(:course IS NULL OR s.course = :course)")
	Page<Student> searchStudents(@Param("name") String name, @Param("course") String course, Pageable pageable);
}
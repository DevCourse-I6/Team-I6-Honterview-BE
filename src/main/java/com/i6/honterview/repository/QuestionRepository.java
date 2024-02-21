package com.i6.honterview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i6.honterview.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionQueryDslRepository {

	@Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.questionHearts WHERE q.id = ?1")
	Optional<Question> findByIdWithHearts(@Param("id") Long id);

	@Query(value = "SELECT q.* FROM question q JOIN question_category qc ON q.id = qc.question_id WHERE qc.category_id IN :categoryIds AND q.id != :questionId GROUP BY q.id ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<Question> findRandomQuestionsByCategoryIds(@Param("categoryIds") List<Long> categoryIds,
		@Param("questionId") Long questionId);
}

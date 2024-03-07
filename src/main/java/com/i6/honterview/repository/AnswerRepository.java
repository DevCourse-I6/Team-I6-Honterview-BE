package com.i6.honterview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerQueryDslRepository {

	@Query("SELECT DISTINCT a FROM Answer a LEFT JOIN FETCH a.answerHearts WHERE a.id = ?1")
	Optional<Answer> findByIdWithHearts(@Param("id") Long id);

	List<Answer> findByInterviewId(Long id);

	Optional<Answer> findByInterviewAndQuestion(Interview interview, Question question);
}

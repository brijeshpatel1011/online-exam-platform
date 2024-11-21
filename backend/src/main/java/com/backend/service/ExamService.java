package com.backend.service;

import com.backend.model.Exam;
import com.backend.model.MCQ;
import com.backend.repository.ExamRepository;
import com.backend.repository.MCQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MCQRepository mcqRepository;

    // Fetch all exams
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // Fetch an exam by ID
    public Optional<Exam> getExamById(int examId) {
        return examRepository.findById(examId);
    }

    // Create an exam
    public Exam createExam(Exam exam) {

        List<MCQ> selectedQuestions = mcqRepository.findRandomQuestions(exam.getTotalQuestions());
        exam.setMcqs(selectedQuestions);

        int totalMarks = selectedQuestions.stream().mapToInt(MCQ::getMarks).sum();
        exam.setTotalMarks(totalMarks);

        return examRepository.save(exam);
    }


    // Update an existing exam
    public Exam updateExam(int examId, Exam examDetails) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam with ID " + examId + " does not exist."));

        existingExam.setTitle(examDetails.getTitle());
        existingExam.setDescription(examDetails.getDescription());
        existingExam.setTotalQuestions(examDetails.getTotalQuestions());
        existingExam.setPassingScore(examDetails.getPassingScore());
        existingExam.setCollege(examDetails.getCollege());
        existingExam.setDuration(examDetails.getDuration());
        existingExam.setExamStartDate(examDetails.getExamStartDate());
        existingExam.setExamStartTime(examDetails.getExamStartTime());
        existingExam.setExamEndDate(examDetails.getExamEndDate());
        existingExam.setExamEndTime(examDetails.getExamEndTime());
        existingExam.setTotalMarks(examDetails.getTotalMarks());

        return examRepository.save(existingExam);
    }

    // Delete an exam by ID
    public void deleteExamById(int examId) {
        if (examRepository.existsById(examId)) {
            examRepository.deleteById(examId);
        } else {
            throw new IllegalArgumentException("Exam with ID " + examId + " does not exist.");
        }
    }

    // Fetch exams by college
    public List<Exam> getExamsByCollege(String college) {
        return examRepository.findByCollege(college);
    }

    // Fetch exams by start date range
    public List<Exam> getExamsByDateRange(LocalDate startDate, LocalDate endDate) {
        return examRepository.findByExamStartDateBetween(startDate, endDate);
    }
}


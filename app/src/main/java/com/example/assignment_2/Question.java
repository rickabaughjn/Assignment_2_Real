package com.example.assignment_2;

import java.util.ArrayList;

public class Question {

    private String difficulty;
    private String question;
    private String correctAnswer;
    private String userAnswer;
    private ArrayList<String> answers = new ArrayList<>();

    Question(){
        this.difficulty = "medium";
        this.question = "Example Question";
        this.correctAnswer = "Sample Answer A";
        this.userAnswer = "Sample Answer B";
    }

    String getDifficulty() {
        return difficulty;
    }

    void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }

    String getQuestion() {
        return question;
    }

    void setQuestion(String question) {
        this.question = question;
    }

    String getCorrectAnswer() {
        return correctAnswer;
    }

    void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    String getUserAnswer() {
        return userAnswer;
    }

    void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    void addAnswer(String answer) {
        answers.add(answer);
    }
}
package com.example.quiz_app.Models;

import androidx.recyclerview.widget.RecyclerView;

public class Topic {
    private String topicName;
    private String question;
    private String documentId;

    public Topic() {
        // Default constructor required for Firestore
    }

    public Topic(String topicName, String question) {
        this.topicName = topicName;
        this.question = question;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

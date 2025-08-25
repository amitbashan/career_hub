package com.amitbashan.career_hub.entities;

public enum JobApplicationStatus {
    Pending,
    Accepted,
    Rejected;

    @Override
    public String toString() {
        switch (this) {
            case Pending -> {
                return "Pending";
            }
            case Accepted -> {
                return "Accepted";
            }
            case Rejected -> {
                return "Rejected";
            }
        }
        return "";
    }
}

package com.guctechie.datainsertions.models;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

@Getter
@Setter
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private Date dateOfBirth;
    private Date registrationDate;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private String profilePhotoUrl;
    private String phoneNumber;
    private String bio;

    private final Hashtable<String, String> socialMediaLinks = new Hashtable<>();
    private final ArrayList<Integer> roleId = new ArrayList<>();
    private final ArrayList<Quartet<Boolean, Timestamp, String, String>> history = new ArrayList<>();
    private final ArrayList<Triplet<String, String, Timestamp>> problemReports = new ArrayList<>();
    private final ArrayList<Pair<String, Timestamp>> passwordResetRequests = new ArrayList<>();
    private final ArrayList<Pair<String, Timestamp>> userActivityLogs = new ArrayList<>();
}

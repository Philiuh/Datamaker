package com.example.cameraapp;

public class NetworkService {
    private static final NetworkService ourInstance = new NetworkService();

    public static NetworkService getInstance() {
        return ourInstance;
    }

    private NetworkService() {
    }
}

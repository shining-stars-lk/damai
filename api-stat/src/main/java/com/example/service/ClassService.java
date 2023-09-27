package com.example.service;



import java.util.logging.Logger;

public interface ClassService {
    void updateClass(String className, String classPath);

    static ClassService getInstance() {
        return ClassServiceFactory.getInstance();
    }
}

class ClassServiceFactory {
    private static Logger log = Logger.getLogger(ClassServiceFactory.class.toString());

    private static ClassService instance = null;

    public static ClassService getInstance() {
        if (instance == null) {
            synchronized (ClassServiceFactory.class) {
                if (instance == null) {
                    instance = new JvmAttachClassService();
                }
            }
        }
        return instance;
    }
}


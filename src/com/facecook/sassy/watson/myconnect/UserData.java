package com.facecook.sassy.watson.myconnect;

/* package */ class UserData {
    private String mUserName;
    private String mUserId;
    private String mUserMessage;
    public UserData(String name, String id, String message) {
        mUserName = name;
        mUserId = id;
        mUserMessage = message;
    }

    public String getUserName() {
        return mUserName;
    }
    public String getUserId() {
        return mUserId;
    }
    public String getUserMessage() {
        return mUserMessage;
    }
}

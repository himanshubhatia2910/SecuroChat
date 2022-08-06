package com.suspecious.chatmate.Utility;

import android.provider.ContactsContract;

public class Sender {
    public Data data;
    public String to;

    public Sender(Data data, Object to) {
        this.data = data;
        this.to = (String) to;
    }

    public Sender(ContactsContract.Data data, Object token) {

    }
}

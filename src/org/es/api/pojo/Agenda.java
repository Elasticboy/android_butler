package org.es.api.pojo;

/**
 * Created by Cyril Leroux on 19/07/13.
 */
public class Agenda {

    private long mId;
    private String mDisplayName;
    private String mAccountName;
    private String mOwnerName;

    public Agenda(long id, String displayName, String accountName, String ownerName) {
        mId             = id;
        mDisplayName    = displayName;
        mAccountName    = accountName;
        mOwnerName      = ownerName;
    }

    public long getId() {
        return mId;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public String getOwnerName() {
        return mOwnerName;
    }
}

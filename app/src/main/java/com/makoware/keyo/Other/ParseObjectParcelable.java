package com.makoware.keyo.Other;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by doman412 on 4/15/14.
 */
public class ParseObjectParcelable implements Parcelable {

    private ParseObject obj;

    public ParseObjectParcelable(ParseObject obj){
        this.obj = obj;
    }

    public ParseObject getObj(){
        return this.obj;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(obj);
    }

    public static final Parcelable.Creator<ParseObjectParcelable> CREATOR
            = new Parcelable.Creator<ParseObjectParcelable>() {
        public ParseObjectParcelable createFromParcel(Parcel in) {
            return new ParseObjectParcelable(in);
        }

        public ParseObjectParcelable[] newArray(int size) {
            return new ParseObjectParcelable[size];
        }
    };

    private ParseObjectParcelable(Parcel in) {
//        this.obj = in.readValue()
    }

}

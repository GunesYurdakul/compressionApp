package com.example.gunesyurdakul.cp1v2;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by gunesyurdakul on 09/11/2017.
 */

public class TxtFile {
    String sender;//user_name of sender
    String receiver;//user_name of receiver;
    String sourceText;
    String fileName;
    Date    date;
    byte[] zipped;
    int bitSize;
    LinkedHashMap<Character, Integer> frequencies;
    TxtFile(String sender,String receiver,String fileData,String fileName,int bitSize, LinkedHashMap<Character, Integer> frequencies){
        this.sender=sender;
        this.receiver=receiver;
        this.sourceText=fileData;
        this.fileName=fileName;
        this.date = new Date();
        this.bitSize=bitSize;
        this.frequencies=frequencies;
    }
    TxtFile(){}
}


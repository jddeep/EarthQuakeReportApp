package com.example.android.quakereport;

public class Quake {
private double mMagnitude;
private String mPart1;
private String mPart2;
private long mtimeinmilliseonds;
private String mUri;
public Quake(double mag,String Part1,String Part2,long time,String uri)
{
    mMagnitude=mag;
    mPart1=Part1;
    mPart2=Part2;
    mtimeinmilliseonds=time;
    mUri=uri;
}
public double getmMagnitude()
{return mMagnitude;}
public String getmLocation1()
{return mPart1;}
public String getmLocation2(){
    return mPart2;
}
public long getmTime()
{return mtimeinmilliseonds;}
public String getUrl(){
    return mUri;
}
}

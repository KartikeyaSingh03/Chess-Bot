package com.ks.undefeatablebot;

import com.ks.undefeatablebot.Client.LichessClient;


public class App 
{
    public static void main( String[] args )
    {
        LichessClient client = new LichessClient("lip_ZKLrW84w38uiKLcdrrfC");
        System.out.println(client.getMyEmailAddress());
    }
}

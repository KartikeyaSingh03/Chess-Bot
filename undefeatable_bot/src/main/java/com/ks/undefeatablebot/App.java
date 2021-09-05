package com.ks.undefeatablebot;

import com.ks.undefeatablebot.Client.LichessClient;
import com.ks.undefeatablebot.Client.handler.UserEvent;


public class App 
{

    public static void main( String[] args )
    {
        LichessClient client = new LichessClient("lip_ZKLrW84w38uiKLcdrrfC");
        System.out.println(client.getMyEmailAddress());

        client.streamIncomingEvents(new UserEvent(client));

    }
}
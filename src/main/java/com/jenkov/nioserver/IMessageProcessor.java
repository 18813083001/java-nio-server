package com.jenkov.nioserver;

import java.io.UnsupportedEncodingException;

/**
 * Created by jjenkov on 16-10-2015.
 */
public interface IMessageProcessor {

    public void process(Message message, WriteProxy writeProxy) throws UnsupportedEncodingException;

}

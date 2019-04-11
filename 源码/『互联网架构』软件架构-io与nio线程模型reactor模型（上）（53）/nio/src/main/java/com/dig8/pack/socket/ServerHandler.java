package com.dig8.pack.socket;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * @author idig8.com
 */
public class ServerHandler extends SimpleChannelHandler{
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
//		byte[] bs = buffer.array();
		System.out.println("server receive data: " + e.getMessage());
	}
}

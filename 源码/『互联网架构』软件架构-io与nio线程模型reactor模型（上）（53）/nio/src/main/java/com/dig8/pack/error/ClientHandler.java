package com.dig8.pack.error;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * 客户端消息处理类
 * @author idig8.com
 */
public class ClientHandler extends SimpleChannelHandler {
	// 包头
	private static final int HEAD_FLAG = -32323231;
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		String msg = "Hello,idig8.com 重现粘包和分包";
		
		for (int i = 0; i < 1000; i++) {
			channel.write(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
	}
}

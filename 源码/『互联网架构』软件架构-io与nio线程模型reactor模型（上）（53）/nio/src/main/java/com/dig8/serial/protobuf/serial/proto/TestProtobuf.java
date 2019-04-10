/**
 * 
 */
package com.dig8.serial.protobuf.serial.proto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.dig8.serial.protobuf.serial.proto.SubscribeReqProto.SubscribeReq;

/**
 * @author
 */
public class TestProtobuf {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		byte[] result = serialize();
		System.out.println(Arrays.toString(result));
		
		SubscribeReq req = deserialize(result);
		System.out.println(req.getSubReqID());
		System.out.println(req.getUserName());
		System.out.println(req.getProductName());
		System.out.println(req.getAddress(0));
		System.out.println(req.getAddress(1));
	}
	/**
	 * 序列化
	 * @return
	 */
	public static byte[] serialize(){
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(1);
		builder.setUserName("abc");
		builder.setProductName("netty");
		
		List<String> addressList = new ArrayList<String>();
		addressList.add("郑州");
		addressList.add("开封");
		builder.addAllAddress(addressList);
		SubscribeReq subscribeReq = builder.build();
		return subscribeReq.toByteArray();
	}
	
	/**
	 * 反序列化
	 * @return
	 * @throws InvalidProtocolBufferException 
	 */
	public static SubscribeReq deserialize(byte[] bytes) throws InvalidProtocolBufferException{
		SubscribeReq result = SubscribeReq.parseFrom(bytes);
		return result;
	}
}

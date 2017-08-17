package xsh.raindrops.distribution.thrift;

import java.util.Arrays;

import org.apache.thrift.TException;

import xsh.raindrops.distribution.thrift.client.impl.ThriftClientImpl;
import xsh.raindrops.distribution.thrift.client.pool.ThriftServerInfo;

/**
 * Created by poan on 2017/08/15.
 */
public class ThriftTest {

    public static void main(String[] args) {
    	ThriftClientImpl thriftClient = new ThriftClientImpl(() -> Arrays.asList(//
                ThriftServerInfo.of("192.168.30.249", 9090), //
                ThriftServerInfo.of("192.168.30.249", 9091) //
                // or you can return a dynamic result.
        ));
        // get iface and call
        try {
            System.out.println(thriftClient.iface(CalculatorService.Client.class).add(1,4));
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
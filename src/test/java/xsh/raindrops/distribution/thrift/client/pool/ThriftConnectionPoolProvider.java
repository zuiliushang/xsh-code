/**
 * 
 */
package xsh.raindrops.distribution.thrift.client.pool;

import org.apache.thrift.transport.TTransport;

/**
 * <p>
 * ThriftConnectionPoolProvider interface.
 * </p>
 *
 */
public interface ThriftConnectionPoolProvider {

    /**
     * <p>
     * getConnection.
     * </p>
     *
     */
    TTransport getConnection(ThriftServerInfo thriftServerInfo);

    /**
     * <p>
     * returnConnection.
     * </p>
     *
     */
    void returnConnection(ThriftServerInfo thriftServerInfo, TTransport transport);

    /**
     * <p>
     * returnBrokenConnection.
     * </p>
     *
     */
    void returnBrokenConnection(ThriftServerInfo thriftServerInfo, TTransport transport);

}

package me.prettyprint.cassandra.connection;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import me.prettyprint.cassandra.service.Operation;
import me.prettyprint.hector.api.exceptions.ExceptionsTranslator;
import me.prettyprint.hector.api.exceptions.HectorException;

public interface HConnectionManager {

 

  /**
   * Returns true if the host was successfully added. In any sort of failure exceptions are 
   * caught and logged, returning false.
   * @param cassandraHost
   * @return
   */
  public boolean addCassandraHost(HCassandraHost cassandraHost);

  /**
   * Remove the {@link CassandraHost} from the pool, bypassing retry service. This
   * would be called on a host that is known to be going away. Gracefully shuts down
   * the underlying connections via {@link HClientPool#shutdown()}. This method
   * will also shutdown pools in the suspended state, removing them from the underlying
   * suspended map.
   * @param cassandraHost
   */
  public boolean removeCassandraHost(HCassandraHost cassandraHost);
  
  /**
   * Remove the {@link HClientPool} referenced by the {@link CassandraHost} from 
   * the active host pools. This does not shut down the pool, only removes it as a candidate from
   * future operations.
   * @param cassandraHost
   * @return true if the operation was successful.
   */
  public boolean suspendCassandraHost(HCassandraHost cassandraHost);

  /** 
   * The opposite of suspendCassandraHost, places the pool back into selection
   * @param cassandraHost
   * @return true if this operation was successful. A no-op returning false 
   * if there was no such host in the underlying suspendedHostPool map.
   */
  public boolean unsuspendCassandraHost(HCassandraHost cassandraHost);
  
  /**
   * Returns a Set of {@link CassandraHost} which are in the suspended status
   * @return
   */
  public Set<HCassandraHost> getSuspendedCassandraHosts();
  
  public Set<HCassandraHost> getHosts() ;

  public List<String> getStatusPerPool();

  public void operateWithFailover(Operation<?> op) throws HectorException;

  public HClientPool getClientFromLBPolicy(Set<HCassandraHost> excludeHosts);

  void releaseClient(HThriftClient client);

  HThriftClient borrowClient();

  void markHostAsDown(HCassandraHost cassandraHost);

  public Set<HCassandraHost> getDownedHosts();

  public Collection<HClientPool> getActivePools();

  public long createClock();
  
  public String getClusterName();

  public void shutdown();
  
  public ExceptionsTranslator getExceptionsTranslator();


}

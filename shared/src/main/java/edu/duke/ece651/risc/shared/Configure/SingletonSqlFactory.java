package edu.duke.ece651.risc.shared.Configure;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import edu.duke.ece651.risc.shared.Entity.Vision;
import edu.duke.ece651.risc.shared.Mapper.PlayerMapper;
import edu.duke.ece651.risc.shared.Mapper.RoomMapper;
import edu.duke.ece651.risc.shared.Mapper.RoomPlayerMapper;
import edu.duke.ece651.risc.shared.Mapper.TerritoryMapper;
import edu.duke.ece651.risc.shared.Mapper.VisionMapper;

public class SingletonSqlFactory {
  private static volatile SingletonSqlFactory instance;
  private static SqlSessionFactory sqlSessionFactory;

  /**
   * This function is to get the database source
   * @param driver postgres driver
   * @param database database name
   * @param username username
   * @param password password
   * @return return datasource
   */
  public static DataSource getDataSource(String driver, String database, String username, String password) {
    PooledDataSource dataSource = new PooledDataSource();
    dataSource.setDriver(driver);
    dataSource.setUrl(database);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  /**
   * constructor function, set to private so this class could be 
   * ensured to be singleton.
   */
  private SingletonSqlFactory(){
    sqlSessionFactory = getSqlSessionFactory();
  }

  /**
   * This function is to get the singleton instance of this factory
   * @return singleton instance
   */
  public static SingletonSqlFactory getSingletonInstance(){
    SingletonSqlFactory result = instance;
    if (result != null) {
      return result;
    }
    synchronized (SingletonSqlFactory.class) {
      if (instance == null) {
        instance = new SingletonSqlFactory();
      }
      return instance;
    }
  }
  

  /**
   * Connect to the postgresql database.
   * 
   * @return {@link SqlSessionFactory}
   */
  public static SqlSessionFactory getSqlSessionFactory() {
    if (sqlSessionFactory == null) {
      DataSource dataSource = getDataSource("org.postgresql.Driver", "jdbc:postgresql://vcm-25965.vm.duke.edu/postgres",
          "postgres", "Ece568hw4");

      TransactionFactory transactionFactory = new JdbcTransactionFactory();
      Environment environment = new Environment("development", transactionFactory, dataSource);
      Configuration configuration = new Configuration(environment);
      configuration.setMapUnderscoreToCamelCase(true);
      configuration.addMapper(PlayerMapper.class);
      configuration.addMapper(RoomPlayerMapper.class);
      configuration.addMapper(RoomMapper.class);
      configuration.addMapper(TerritoryMapper.class);
      configuration.addMapper(VisionMapper.class);

      
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
    return sqlSessionFactory;
  }

  /**
   * This function is to get a session with designated isolation level
   * @param level transcation level
   * @return new session
   */
  public SqlSession openSession(TransactionIsolationLevel level) {
    return getSqlSessionFactory().openSession(level);
  }
}

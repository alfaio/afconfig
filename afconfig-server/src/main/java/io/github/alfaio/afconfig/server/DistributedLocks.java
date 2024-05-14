package io.github.alfaio.afconfig.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author LimMF
 * @since 2024/5/14
 **/
@Slf4j
@Component
public class DistributedLocks {

    @Autowired
    DataSource dataSource;

    @Getter
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private Connection connection;
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        executor.scheduleWithFixedDelay(this::tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    public void lock() throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        // 设置超时时间，数据库默认是50秒（show variables  like 'innodb_lock_wait_timeout';）
        connection.createStatement().execute("set innodb_lock_wait_timeout = 5");
        connection.createStatement().execute("select app from locks where id = 1 for update");
        if (locked.get()) {
            log.info(" ===>>> reenter this dist lock.");
        } else {
            log.info(" ===>>> get a dist lock.");
        }
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            log.info(" ===>>> tryLock failed");
            locked.set(false);
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (SQLException e) {
            log.info(" ===>>> close connection error: {}", e.getMessage());
        }
    }
}

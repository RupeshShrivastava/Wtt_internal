package com.halodoc.oms.orders.utilities;

import com.halodoc.transformers.havelock.service.Locker;

/**
 * @author Prashant Maroti
 * @since 2020-01-21
 */
public class LockUtil {

    private static final String SERVICE_NAME = "timor_oms";

    private final Locker locker;

    public LockUtil(final Locker locker){
        this.locker = locker;
    }

    public void unlock(final String lockKey) {
        final String serviceLockKey = SERVICE_NAME + lockKey;
        locker.unlock(serviceLockKey);
    }

    public boolean lock(final String lockKey) {
        final String serviceLockKey = SERVICE_NAME + lockKey;
        return locker.lock(serviceLockKey);
    }

}

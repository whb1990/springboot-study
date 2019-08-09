package com.springboot.whb.study.redislockframework;


public interface SeckillInterface {
	@CacheLock(lockedPrefix="TEST_PREFIX")
	public void secKill(String arg1, @LockedObject Long arg2);
}

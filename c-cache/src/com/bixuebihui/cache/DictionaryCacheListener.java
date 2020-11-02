package com.bixuebihui.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.oscache.extra.CacheMapAccessEventListenerImpl;

public class DictionaryCacheListener extends CacheMapAccessEventListenerImpl {
Log log = LogFactory.getLog(DictionaryCacheListener.class);

	public void dump(){
		log.debug("DictionaryCache "+ this.toString());
	}
}

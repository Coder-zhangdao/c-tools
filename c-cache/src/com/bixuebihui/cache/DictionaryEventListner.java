package com.bixuebihui.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.oscache.extra.CacheEntryEventListenerImpl;

public class DictionaryEventListner extends CacheEntryEventListenerImpl {
	Log log = LogFactory.getLog(DictionaryEventListner.class);

	public void dump(){
		log.debug("DictionaryEventListner "+ this.toString());
	}
}

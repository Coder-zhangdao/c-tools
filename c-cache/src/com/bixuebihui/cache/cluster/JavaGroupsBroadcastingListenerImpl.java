package com.bixuebihui.cache.cluster;

import com.opensymphony.oscache.base.events.CacheEntryEvent;
import com.opensymphony.oscache.plugins.clustersupport.ClusterNotification;
import com.opensymphony.oscache.plugins.clustersupport.JavaGroupsBroadcastingListener;

public class JavaGroupsBroadcastingListenerImpl extends
		JavaGroupsBroadcastingListener {
	public void handleClusterNotification(ClusterNotification message) {

		switch (message.getType()) {
		case CacheConstants.CLUSTER_ENTRY_ADD:
			System.out.println("集群新增:" + message.getData());
			if (message.getData() instanceof QflagCacheEvent) {
				QflagCacheEvent event = (QflagCacheEvent) message.getData();
				cache.putInCache(event.getKey(), event.getEntry().getContent(),
						null, null, CLUSTER_ORIGIN);
			}
			break;
		case CacheConstants.CLUSTER_ENTRY_UPDATE:
			System.out.println("集群更新:" + message.getData());
			if (message.getData() instanceof QflagCacheEvent) {
				QflagCacheEvent event = (QflagCacheEvent) message.getData();
				cache.putInCache(event.getKey(), event.getEntry().getContent(),
						null, null, CLUSTER_ORIGIN);
			}
			break;
		case CacheConstants.CLUSTER_ENTRY_DELETE:
			System.out.println("集群删除:" + message.getData());
			if (message.getData() instanceof QflagCacheEvent) {
				QflagCacheEvent event = (QflagCacheEvent) message.getData();
				// cache.removeEntry(event.getKey(),event.getOrigin());
				cache.removeEntry(event.getKey());
			}
			break;
		}

	}

	public void cacheEntryAdded(CacheEntryEvent event) {
		super.cacheEntryAdded(event);
		if (!CLUSTER_ORIGIN.equals(event.getOrigin())) {
			sendNotification(new ClusterNotification(
					CacheConstants.CLUSTER_ENTRY_ADD, new QflagCacheEvent(event
							.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
		}
	}

	// @Override
	// public void cacheEntryFlushed(CacheEntryEvent event) {
	//
	// super.cacheEntryFlushed(event);
	// if(!CLUSTER_ORIGIN.equals(event.getOrigin())) {
	// sendNotification(new
	// ClusterNotification(CacheConstants.CLUSTER_ENTRY_ADD, new
	// UcallCacheEvent(event.getMap(),event.getEntry(),CLUSTER_ORIGIN)));
	// }
	// }

	@Override
	public void cacheEntryRemoved(CacheEntryEvent event) {

		super.cacheEntryRemoved(event);
		if (!CLUSTER_ORIGIN.equals(event.getOrigin())) {
			sendNotification(new ClusterNotification(
					CacheConstants.CLUSTER_ENTRY_DELETE, new QflagCacheEvent(
							event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
		}
	}

	@Override
	public void cacheEntryUpdated(CacheEntryEvent event) {

		super.cacheEntryUpdated(event);
		if (!CLUSTER_ORIGIN.equals(event.getOrigin())) {
			sendNotification(new ClusterNotification(
					CacheConstants.CLUSTER_ENTRY_UPDATE, new QflagCacheEvent(
							event.getMap(), event.getEntry(), CLUSTER_ORIGIN)));
		}
	}

}

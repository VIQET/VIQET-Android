package org.vqeg.viqet.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.AndroidTestCase;

public class PhotoInspectorServiceTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PhotoInspectorService.BROADCAST_METHODOLOGY_FETCHED);// RemoteInfoProvider.BROADCAST_ACTION);
        this.getContext().registerReceiver(testReceiver, filter);
    }

    @Override
    protected void tearDown() throws Exception
    {
        this.getContext().unregisterReceiver(testReceiver);
        super.tearDown();
    }

    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean success = intent.getBooleanExtra(PhotoInspectorService.SUCCESS, false);
            assertEquals(true,success);
        }
    };

    public void testDownloadRemoteInfo() {
        Intent startServiceIntent = new Intent();
        startServiceIntent.setClass(this.getContext(), PhotoInspectorService.class);
        startServiceIntent.setAction(PhotoInspectorService.FETCH_METHODOLOGY_ACTION);
        this.getContext().startService(startServiceIntent);

    }


}

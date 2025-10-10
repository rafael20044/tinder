package io.ionic.starter;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

import io.ionic.starter.plugin.ChatPlugin;
import io.ionic.starter.plugin.MatchingPlugin;

public class MainActivity extends BridgeActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    registerPlugin(MatchingPlugin.class);
    registerPlugin(ChatPlugin.class);
    super.onCreate(savedInstanceState);
  }
}

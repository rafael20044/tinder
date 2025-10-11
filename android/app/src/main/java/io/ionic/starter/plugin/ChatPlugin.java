package io.ionic.starter.plugin;

import android.content.Intent;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import io.ionic.starter.controller.ChatController;
import io.ionic.starter.controller.ChatListController;

@CapacitorPlugin(name = "ChatPlugin")
public class ChatPlugin extends Plugin {

  @PluginMethod
  public void open(PluginCall call){
    var uid = call.getString("uid");
    Intent intent = new Intent(getContext(), ChatListController.class);
    intent.putExtra("uid", uid);
    getActivity().startActivity(intent);
    call.resolve();
  }
}

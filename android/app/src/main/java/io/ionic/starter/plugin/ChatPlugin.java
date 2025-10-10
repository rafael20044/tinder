package io.ionic.starter.plugin;

import android.content.Intent;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import io.ionic.starter.controller.ChatController;

@CapacitorPlugin(name = "ChatPlugin")
public class ChatPlugin extends Plugin {

  @PluginMethod
  public void open(PluginCall call){
    Intent intent = new Intent(getContext(), ChatController.class);
    getActivity().startActivity(intent);
    call.resolve();
  }
}

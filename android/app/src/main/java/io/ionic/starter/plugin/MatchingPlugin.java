package io.ionic.starter.plugin;

import android.content.Intent;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import io.ionic.starter.controller.MatchingController;

@CapacitorPlugin(name = "MatchingPlugin")
public class MatchingPlugin extends Plugin {

  @PluginMethod
  public void open(PluginCall call){
    var uid = call.getString("uid");
    var name = call.getString("name");
    Intent intent = new Intent(getContext(), MatchingController.class);
    intent.putExtra("uid", uid);
    intent.putExtra("name", name);
    getActivity().startActivity(intent);
    call.resolve();
  }
}

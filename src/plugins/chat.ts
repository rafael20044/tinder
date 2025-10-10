import { registerPlugin } from "@capacitor/core";

interface IChatPlugin{
    open:() => Promise<void>;
}

const ChatPlugin:IChatPlugin = registerPlugin("ChatPlugin");

export default ChatPlugin;
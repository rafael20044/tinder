import { registerPlugin } from "@capacitor/core";

interface IChatPlugin{
    open:(option:{uid:string}) => Promise<void>;
}

const ChatPlugin:IChatPlugin = registerPlugin("ChatPlugin");

export default ChatPlugin;
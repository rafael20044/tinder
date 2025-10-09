import { registerPlugin } from '@capacitor/core';

export interface MatchingPlugin {
    open(): Promise<void>;
}

const Matching = registerPlugin<MatchingPlugin>('MatchingPlugin');

export default Matching;
import { registerPlugin } from '@capacitor/core';
import { IUserCreate } from 'src/app/interfaces/iuser-create';
import { IUserMatch } from 'src/app/interfaces/iuser-match';

export interface MatchingPlugin {
    open:(option: {uid:string}) => Promise<void>;
}

const Matching = registerPlugin<MatchingPlugin>('MatchingPlugin');

export default Matching;
import { IUserCreate } from "./iuser-create";

export interface IUserMatch extends Pick<IUserCreate, 'name'| 'lastName' | 'photos' | 'uid' |'showGenderProfile'>{

}
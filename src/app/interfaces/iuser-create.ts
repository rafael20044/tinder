export interface IUserCreate {
    uid:string;
    name: string
    lastName: string
    birthDate: string
    email: string
    password: string
    country: string
    sex: string
    showGenderProfile: boolean
    passions: string[]
    photos: IPhoto[]
}

interface IPhoto{
    path:string;
    url:string;
}
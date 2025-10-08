export interface IUserCreate {
    name: string
    lastName: string
    birthDate: string
    email: string
    password: string
    country: string
    city: string
    sex: string
    showGenderProfile: boolean
    passions: string[]
    photos: string[]
}
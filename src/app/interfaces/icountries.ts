export interface ICountries {
    error: boolean
    msg: string
    data: ICountry[]
}

export interface ICountry {
    name: string
    iso2: string
    iso3: string
    unicodeFlag: string
}
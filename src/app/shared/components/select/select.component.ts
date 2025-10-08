import { Component, Input, OnInit } from '@angular/core';
import { HttpProvider } from '../../provider/http-provider';
import { ICountries, ICountry } from 'src/app/interfaces/icountries';
import { environment } from 'src/environments/environment.prod';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss'],
  standalone: false,
})
export class SelectComponent  implements OnInit {

  @Input() control = new FormControl();
  private countries:ICountries | null = null;
  countriesSort:ICountry[] | null = null;

  constructor(private readonly http:HttpProvider) { }

  ngOnInit() {
    this.loadCountries()
  }


  private async loadCountries(){
    const url = environment.API_COUNTRIES;
    const data = await this.http.get<ICountries>(url);
    if (data) {
      this.countries = data;
      this.countriesSort = this.countries.data.sort((a, b)=> a.name.localeCompare(b.name));
    }
  }

}

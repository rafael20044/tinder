import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { InputComponent } from './components/input/input.component';
import { SelectComponent } from './components/select/select.component';
import { ButtonComponent } from './components/button/button.component';
import { Register1Component } from './components/register1/register1.component';
import { HttpProvider } from './provider/http-provider';
import { Register2Component } from './components/register2/register2.component';
import { Register3Component } from './components/register3/register3.component';
import { Register4Component } from './components/register4/register4.component';
import { Register5Component } from './components/register5/register5.component';
import { CheckboxComponent } from './components/checkbox/checkbox.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';




@NgModule({
  declarations: [
    InputComponent, 
    SelectComponent, 
    ButtonComponent, 
    Register1Component,
    Register2Component,
    Register3Component,
    Register4Component,
    Register5Component,
    CheckboxComponent,
  ],
  providers: [HttpProvider],
  imports: [
    CommonModule,
    IonicModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  exports: [
    InputComponent, 
    SelectComponent, 
    ButtonComponent, 
    Register1Component,
    Register2Component,
    Register3Component,
    Register4Component,
    Register5Component,
    CheckboxComponent,
  ]
})
export class SharedModule { }

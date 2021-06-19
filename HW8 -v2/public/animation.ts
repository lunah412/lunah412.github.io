import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {Component} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl : './search.html',
    styleUrls: ['./style.css']
})

export class AppComponent {
    options:string [] = ['Angular','React','Vue'];
}
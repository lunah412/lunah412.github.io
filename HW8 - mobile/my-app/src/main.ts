import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import { MatSliderModule } from '@angular/material/slider';
import {Component} from '@angular/core';
import {FormControl} from '@angular/forms';

import {Component} from '@angular/core';
import {FormControl} from '@angular/forms';

/**
 * @title Simple autocomplete
 */
@Component({
  selector: 'autocomplete-simple-example',
  templateUrl: 'index.html',
  styleUrls: ['style.css'],
})
export class AutocompleteSimpleExample {
  myControl = new FormControl();
  options: string[] = ['One', 'Two', 'Three'];
}
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LogInComponent } from './components/log-in/log-in.component';
import { RegisterComponent } from './components/register/register.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularMaterialModule } from './angular-material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './components/home/home.component';
import { HttpClientModule } from '@angular/common/http';
import { ArticlesComponent } from './components/articles/articles.component';
import {MatDialogModule} from "@angular/material/dialog";
import {DetailComponent, DialogDataDialog} from './components/detail/detail.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatButtonModule} from "@angular/material/button";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {NgIf} from "@angular/common";
import { D3pieComponent } from './components/d3pie/d3pie.component';

@NgModule({
  declarations: [AppComponent, LogInComponent, RegisterComponent, HomeComponent, ArticlesComponent, DetailComponent, D3pieComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        AngularMaterialModule,
        FlexLayoutModule,
        FormsModule,
        HttpClientModule,
        MatDialogModule,
        MatToolbarModule,
        MatSidenavModule,
        MatButtonModule,
        ReactiveFormsModule,
        RouterOutlet,
        RouterOutlet,
        MatButtonModule,
        NgIf,
        MatButtonModule,
        NgIf,
        RouterLink,
        RouterLinkActive,
    ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
